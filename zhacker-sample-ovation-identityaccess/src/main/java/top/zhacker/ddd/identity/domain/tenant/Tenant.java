package top.zhacker.ddd.identity.domain.tenant;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import top.zhacker.boot.event.publish.DomainEventPublisher;

//import top.zhacker.core.model.IdentifiedEntity;

//import top.zhacker.boot.registry.DomainRegistry;
import top.zhacker.core.model.IdentifiedEntity;
import top.zhacker.ddd.identity.domain.group.Group;
import top.zhacker.ddd.identity.domain.group.event.GroupProvisioned;
import top.zhacker.ddd.identity.domain.role.event.RoleProvisioned;
import top.zhacker.ddd.identity.domain.tenant.event.TenantActivated;
import top.zhacker.ddd.identity.domain.tenant.event.TenantDeactivated;
import top.zhacker.ddd.identity.domain.user.UserRepo;
import top.zhacker.ddd.identity.domain.user.event.UserRegistered;
import top.zhacker.ddd.identity.domain.user.person.Person;
import top.zhacker.ddd.identity.domain.role.Role;
import top.zhacker.ddd.identity.domain.tenant.invitation.InvitationDescriptor;
import top.zhacker.ddd.identity.domain.tenant.invitation.RegistrationInvitation;
import top.zhacker.ddd.identity.domain.user.Enablement;
import top.zhacker.ddd.identity.domain.user.User;


/**
 * Created by zhacker.
 * Time 2018/6/11 下午7:03
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"tenantId", "name"}, callSuper = false)
public class Tenant extends IdentifiedEntity {
  
  private static final long serialVersionUID = 1L;
  
  private TenantId tenantId;
  private String name;
  private String description;
  private boolean active;
  
  private Set<RegistrationInvitation> registrationInvitations;
  
  
  /*********** constructor **************/
  protected Tenant() {
    super();
    this.setRegistrationInvitations(new HashSet<RegistrationInvitation>(0));
  }
  
  public Tenant(TenantId aTenantId, String aName, String aDescription, boolean anActive) {
    this();
    
    this.setActive(anActive);
    this.setDescription(aDescription);
    this.setName(aName);
    this.setTenantId(aTenantId);
  }
  
  /*********** setter **************/
  
  public void setTenantId(TenantId tenantId) {
    this.tenantId = tenantId;
  }
  
  
  public void setName(String name) {
    this.name = name;
  }
  
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  
  public void setActive(boolean active) {
    this.active = active;
  }
  
  
  public void setRegistrationInvitations(Set<RegistrationInvitation> registrationInvitations) {
    this.registrationInvitations = registrationInvitations;
  }
  
  /*********** query **************/
  public Collection<InvitationDescriptor> allAvailableRegistrationInvitations() {
    this.assertStateTrue(this.isActive(), "Tenant is not active.");
  
    return this.allRegistrationInvitationsFor(true);
  }
  
  public Collection<InvitationDescriptor> allUnavailableRegistrationInvitations() {
    this.assertStateTrue(this.isActive(), "Tenant is not active.");
  
    return this.allRegistrationInvitationsFor(false);
  }
  
  protected Collection<InvitationDescriptor> allRegistrationInvitationsFor(boolean isAvailable) {
    Set<InvitationDescriptor> allInvitations = new HashSet<InvitationDescriptor>();
    
    for (RegistrationInvitation invitation : this.registrationInvitations) {
      if (invitation.isAvailable() == isAvailable) {
        allInvitations.add(invitation.toDescriptor());
      }
    }
    
    return Collections.unmodifiableSet(allInvitations);
  }
  
  public boolean isRegistrationAvailableThrough(String anInvitationIdentifier) {
  
    this.assertStateTrue(this.isActive(), "Tenant is not active.");
  
    RegistrationInvitation invitation =
        this.invitation(anInvitationIdentifier);
  
    return invitation == null ? false : invitation.isAvailable();
  }
  
  protected RegistrationInvitation invitation(String anInvitationIdentifier) {
    for (RegistrationInvitation invitation : this.getRegistrationInvitations()) {
      if (invitation.isIdentifiedBy(anInvitationIdentifier)) {
        return invitation;
      }
    }
    return null;
  }
  
  
  
  /*********** command **************/
  
  public void activate(){
    
    if(! this.isActive()){
      this.setActive(true);
        DomainEventPublisher.publish(
          new TenantActivated(this.tenantId)
      );
    }
  }
  
  public void deactivate(){
    
    if(this.isActive()){
      this.setActive(false);
        DomainEventPublisher.publish(
          new TenantDeactivated(this.tenantId)
      );
    }
  }
  
  public RegistrationInvitation offerRegistrationInvitation(String aDescription) {
    this.assertStateTrue(this.isActive(), "Tenant is not active.");
  
    this.assertStateFalse(
        this.isRegistrationAvailableThrough(aDescription),
        "Invitation already exists.");
  
    RegistrationInvitation invitation =
        new RegistrationInvitation(
            this.getTenantId(),
            UUID.randomUUID().toString().toUpperCase(),
            aDescription);
  
    boolean added = this.getRegistrationInvitations().add(invitation);
  
    this.assertStateTrue(added, "The invitation should have been added.");
  
    return invitation;
  }
  
  public void withdrawInvitation(String anInvitationIdentifier) {
    RegistrationInvitation invitation =
        this.invitation(anInvitationIdentifier);
  
    if (invitation != null) {
      this.getRegistrationInvitations().remove(invitation);
    }
  }
  
  public RegistrationInvitation redefineRegistrationInvitationAs(String anInvitationIdentifier) {
    return null;
  }
  
  public Group provisionGroup(String aName, String aDescription) {
    this.assertStateTrue(this.isActive(), "Tenant is not active.");
  
    Group group = new Group(this.tenantId, aName, aDescription);
  
    return group;
  }
  
  public Role provisionRole(String aName, String aDescription) {
    return provisionRole(aName, aDescription, false);
  }
  
  public Role provisionRole(String aName, String aDescription, boolean aSupportsNesting) {
    
    this.assertStateTrue(this.isActive(), "Tenant is not active.");
  
    Role role = new Role(this.tenantId, aName, aDescription, aSupportsNesting);
  
    return role;
  }
  
  public User registerUser(
      String anInvitationIdentifier,
      String aUsername,
      String aPassword,
      Enablement anEnablement,
      Person aPerson) {
    
    this.assertStateTrue(this.isActive(), "Tenant is not active.");
  
    User user = null;
  
    if (this.isRegistrationAvailableThrough(anInvitationIdentifier)) {
    
      // ensure same tenant
      aPerson.setTenantId(this.getTenantId());
    
      user = new User(
          this.getTenantId(),
          aUsername,
          aPassword,
          anEnablement,
          aPerson);
    }
  
//    DomainRegistry.repo(UserRepo.class).save(user);
  
    
     DomainEventPublisher
        .publish(new UserRegistered(
            this.getTenantId(),
            aUsername,
            aPerson.name(),
            aPerson.contactInformation().emailAddress()));
  
    return user;
  }
  
  
  
}
