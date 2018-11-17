package top.zhacker.ddd.identity.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import top.zhacker.ddd.identity.application.command.ActivateTenantCommand;
import top.zhacker.ddd.identity.application.command.AddGroupToGroupCommand;
import top.zhacker.ddd.identity.application.command.AddUserToGroupCommand;
import top.zhacker.ddd.identity.application.command.AuthenticateUserCommand;
import top.zhacker.ddd.identity.application.command.ChangeContactInfoCommand;
import top.zhacker.ddd.identity.application.command.ChangeEmailAddressCommand;
import top.zhacker.ddd.identity.application.command.ChangePostalAddressCommand;
import top.zhacker.ddd.identity.application.command.ChangePrimaryTelephoneCommand;
import top.zhacker.ddd.identity.application.command.ChangeSecondaryTelephoneCommand;
import top.zhacker.ddd.identity.application.command.ChangeUserPasswordCommand;
import top.zhacker.ddd.identity.application.command.ChangeUserPersonalNameCommand;
import top.zhacker.ddd.identity.application.command.DeactivateTenantCommand;
import top.zhacker.ddd.identity.application.command.DefineUserEnablementCommand;
import top.zhacker.ddd.identity.application.command.OfferRegistrationInvitationCommand;
import top.zhacker.ddd.identity.application.command.ProvisionGroupCommand;
import top.zhacker.ddd.identity.application.command.ProvisionTenantCommand;
import top.zhacker.ddd.identity.application.command.RegisterUserCommand;
import top.zhacker.ddd.identity.application.command.RemoveGroupFromGroupCommand;
import top.zhacker.ddd.identity.application.command.RemoveUserFromGroupCommand;
import top.zhacker.ddd.identity.domain.group.Group;
import top.zhacker.ddd.identity.domain.group.GroupRepo;
import top.zhacker.ddd.identity.domain.tenant.Tenant;
import top.zhacker.ddd.identity.domain.tenant.TenantId;
import top.zhacker.ddd.identity.domain.tenant.TenantProvisionService;
import top.zhacker.ddd.identity.domain.tenant.TenantRepo;
import top.zhacker.ddd.identity.domain.tenant.invitation.RegistrationInvitation;
import top.zhacker.ddd.identity.domain.user.AuthenticationService;
import top.zhacker.ddd.identity.domain.user.Enablement;
import top.zhacker.ddd.identity.domain.user.User;
import top.zhacker.ddd.identity.domain.user.UserDescriptor;
import top.zhacker.ddd.identity.domain.user.UserRepo;
import top.zhacker.ddd.identity.domain.user.person.ContactInformation;
import top.zhacker.ddd.identity.domain.user.person.EmailAddress;
import top.zhacker.ddd.identity.domain.user.person.FullName;
import top.zhacker.ddd.identity.domain.user.person.Person;
import top.zhacker.ddd.identity.domain.user.person.PostalAddress;
import top.zhacker.ddd.identity.domain.user.person.Telephone;


/**
 * Created by zhacker.
 * Time 2018/6/14 下午6:56
 */
@Service
public class IdentityApplicationService {
  
  @Autowired
  private TenantRepo tenantRepo;
  
  @Autowired
  private GroupRepo groupRepo;
  
  @Autowired
  private UserRepo userRepo;
  
  @Autowired
  private AuthenticationService authenticationService;
  
  @Autowired
  private TenantProvisionService tenantProvisionService;
  
  /************* Tenant **************/
  @Transactional
  public Tenant provisionTenant(ProvisionTenantCommand command){
    
    return this.tenantProvisionService.provisionTenant(
        command.getTenantName(),
        command.getTenantDescription(),
        new FullName(
            command.getAdministorFirstName(),
            command.getAdministorLastName()
        ),
        new EmailAddress(command.getEmailAddress()),
        new PostalAddress(
            command.getAddressStreetAddress(),
            command.getAddressCity(),
            command.getAddressStreetAddress(),
            command.getAddressPostalCode(),
            command.getAddressCountryCode()
        ),
        new Telephone(command.getPrimaryTelephone()),
        new Telephone(command.getSecondaryTelephone())
    );
  }
  
  public Tenant tenant(String tenantId) {
    return tenantRepo.findByTenantId(new TenantId(tenantId));
  }
  
  @Transactional
  public void activateTenant(ActivateTenantCommand aCommand) {
    Tenant tenant = this.existingTenant(aCommand.getTenantId());
    
    tenant.activate();
  }
  
  @Transactional
  public void deactivateTenant(DeactivateTenantCommand aCommand) {
    Tenant tenant = this.existingTenant(aCommand.getTenantId());
    
    tenant.deactivate();
  }
  
  /************* group **************/
  
  @Transactional
  public Group provisionGroup(ProvisionGroupCommand aCommand) {
    Tenant tenant = this.existingTenant(aCommand.getTenantId());
    
    Group group =
        tenant.provisionGroup(
            aCommand.getGroupName(),
            aCommand.getDescription());
    
    this.groupRepo.add(group);
    
    return group;
  }
  
  @Transactional(readOnly=true)
  public Group group(String aTenantId, String aGroupName) {
    Group group =
        this.groupRepo
            .groupNamed(new TenantId(aTenantId), aGroupName);
    
    return group;
  }
  
  public Collection<Group> allGroups(String tenantId){
    return groupRepo.allGroups(new TenantId(tenantId));
  }
  
  @Transactional
  public void addGroupToGroup(AddGroupToGroupCommand aCommand) {
    Group parentGroup =
        this.existingGroup(
            aCommand.getTenantId(),
            aCommand.getParentGroupName());
    
    Group childGroup =
        this.existingGroup(
            aCommand.getTenantId(),
            aCommand.getChildGroupName());
    
    parentGroup.addGroup(childGroup);
  }
  
  @Transactional
  public void addUserToGroup(AddUserToGroupCommand aCommand) {
    Group group =
        this.existingGroup(
            aCommand.getTenantId(),
            aCommand.getGroupName());
    
    User user =
        this.existingUser(
            aCommand.getTenantId(),
            aCommand.getUsername());
    
    group.addUser(user);
  }
  
  @Transactional(readOnly=true)
  public boolean isGroupMember(String aTenantId, String aGroupName, String aUsername) {
    Group group =
        this.existingGroup(
            aTenantId,
            aGroupName);
    
    User user =
        this.existingUser(
            aTenantId,
            aUsername);
    
    return group.isMember(user);
  }
  
  @Transactional
  public void removeGroupFromGroup(RemoveGroupFromGroupCommand aCommand) {
    Group parentGroup =
        this.existingGroup(
            aCommand.getTenantId(),
            aCommand.getParentGroupName());
    
    Group childGroup =
        this.existingGroup(
            aCommand.getTenantId(),
            aCommand.getChildGroupName());
    
    parentGroup.removeGroup(childGroup);
  }
  
  @Transactional
  public void removeUserFromGroup(RemoveUserFromGroupCommand aCommand) {
    Group group =
        this.existingGroup(
            aCommand.getTenantId(),
            aCommand.getGroupName());
    
    User user =
        this.existingUser(
            aCommand.getTenantId(),
            aCommand.getUsername());
    
    group.removeUser(user);
    
  }
  
  /************* User **************/
  
  @Transactional
  public RegistrationInvitation offerRegistrationInvitation(OfferRegistrationInvitationCommand command) {
    Tenant tenant = tenantRepo.findByTenantId(new TenantId(command.getTenantId()));
    RegistrationInvitation invitation = tenant.offerRegistrationInvitation(command.getDescription());
    return invitation;
  }
  
  @Transactional
  public User registerUser(RegisterUserCommand aCommand) {
    Tenant tenant = this.existingTenant(aCommand.getTenantId());
    
    User user =
        tenant.registerUser(
            aCommand.getInvitationIdentifier(),
            aCommand.getUsername(),
            aCommand.getPassword(),
            new Enablement(
                aCommand.isEnabled(),
                aCommand.getStartDate(),
                aCommand.getEndDate()),
            new Person(
                new TenantId(aCommand.getTenantId()),
                new FullName(aCommand.getFirstName(), aCommand.getLastName()),
                new ContactInformation(
                    new EmailAddress(aCommand.getEmailAddress()),
                    new PostalAddress(
                        aCommand.getAddressStateProvince(),
                        aCommand.getAddressCity(),
                        aCommand.getAddressStateProvince(),
                        aCommand.getAddressPostalCode(),
                        aCommand.getAddressCountryCode()),
                    new Telephone(aCommand.getPrimaryTelephone()),
                    new Telephone(aCommand.getSecondaryTelephone()))));
    
    if (user == null) {
      throw new IllegalStateException("User not registered.");
    }
    
    this.userRepo.save(user);
    
    tenant.withdrawInvitation(aCommand.getInvitationIdentifier());
    
    return user;
  }
  
  @Transactional(readOnly=true)
  public UserDescriptor authenticateUser(AuthenticateUserCommand aCommand) {
    UserDescriptor userDescriptor =
        this.authenticationService
            .authenticate(
                new TenantId(aCommand.getTenantId()),
                aCommand.getUsername(),
                aCommand.getPassword());
    
    return userDescriptor;
  }
  
  @Transactional(readOnly=true)
  public User user(String aTenantId, String aUsername) {
    User user =
        this.userRepo
            .userWithUsername(
                new TenantId(aTenantId),
                aUsername);
    
    return user;
  }
  
  
  @Transactional(readOnly=true)
  public Collection<User> users(String aTenantId) {

    return this.userRepo.allUsers(new TenantId(aTenantId));
    
  }
  
  
  @Transactional(readOnly=true)
  public UserDescriptor userDescriptor(
      String aTenantId,
      String aUsername) {
    
    UserDescriptor userDescriptor = null;
    
    User user = this.user(aTenantId, aUsername);
    
    if (user != null) {
      userDescriptor = user.userDescriptor();
    }
    
    return userDescriptor;
  }
  
  @Transactional
  public void changeUserContactInformation(ChangeContactInfoCommand aCommand) {
    User user = this.existingUser(aCommand.getTenantId(), aCommand.getUsername());
    
    this.internalChangeUserContactInformation(
        user,
        new ContactInformation(
            new EmailAddress(aCommand.getEmailAddress()),
            new PostalAddress(
                aCommand.getAddressStreetAddress(),
                aCommand.getAddressCity(),
                aCommand.getAddressStateProvince(),
                aCommand.getAddressPostalCode(),
                aCommand.getAddressCountryCode()),
            new Telephone(aCommand.getPrimaryTelephone()),
            new Telephone(aCommand.getSecondaryTelephone())));
  }
  
  @Transactional
  public void changeUserEmailAddress(ChangeEmailAddressCommand aCommand) {
    User user = this.existingUser(aCommand.getTenantId(), aCommand.getUsername());
    
    this.internalChangeUserContactInformation(
        user,
        user.getPerson()
            .contactInformation()
            .changeEmailAddress(new EmailAddress(aCommand.getEmailAddress())));
  }
  
  @Transactional
  public void changeUserPostalAddress(ChangePostalAddressCommand aCommand) {
    User user = this.existingUser(aCommand.getTenantId(), aCommand.getUsername());
    
    this.internalChangeUserContactInformation(
        user,
        user.getPerson()
            .contactInformation()
            .changePostalAddress(
                new PostalAddress(
                    aCommand.getAddressStreetAddress(),
                    aCommand.getAddressCity(),
                    aCommand.getAddressStateProvince(),
                    aCommand.getAddressPostalCode(),
                    aCommand.getAddressCountryCode())));
  }
  
  @Transactional
  public void changeUserPrimaryTelephone(ChangePrimaryTelephoneCommand aCommand) {
    User user = this.existingUser(aCommand.getTenantId(), aCommand.getUsername());
    
    this.internalChangeUserContactInformation(
        user,
        user.getPerson()
            .contactInformation()
            .changePrimaryTelephone(new Telephone(aCommand.getTelephone())));
  }
  
  @Transactional
  public void changeUserSecondaryTelephone(ChangeSecondaryTelephoneCommand aCommand) {
    User user = this.existingUser(aCommand.getTenantId(), aCommand.getUsername());
    
    this.internalChangeUserContactInformation(
        user,
        user.getPerson()
            .contactInformation()
            .changeSecondaryTelephone(new Telephone(aCommand.getTelephone())));
  }
  
  @Transactional
  public void changeUserPassword(ChangeUserPasswordCommand aCommand) {
    User user = this.existingUser(aCommand.getTenantId(), aCommand.getUsername());
    
    user.changePassword(aCommand.getCurrentPassword(), aCommand.getChangedPassword());
  }
  
  @Transactional
  public void changeUserPersonalName(ChangeUserPersonalNameCommand aCommand) {
    User user = this.existingUser(aCommand.getTenantId(), aCommand.getUsername());
    
    user.getPerson().changeName(new FullName(aCommand.getFirstName(), aCommand.getLastName()));
  }
  
  @Transactional
  public void defineUserEnablement(DefineUserEnablementCommand aCommand) {
    User user = this.existingUser(aCommand.getTenantId(), aCommand.getUsername());
    
    user.defineEnablement(
        new Enablement(
            aCommand.isEnabled(),
            aCommand.getStartDate(),
            aCommand.getEndDate()));
  }
  
  private void internalChangeUserContactInformation(
      User aUser,
      ContactInformation aContactInformation) {
    
    if (aUser == null) {
      throw new IllegalArgumentException("User must exist.");
    }
    
    aUser.getPerson().changeContactInformation(aContactInformation);
  }
  
  
  
  private Tenant existingTenant(String aTenantId) {
    Tenant tenant = this.tenant(aTenantId);
    
    if (tenant == null) {
      throw new IllegalArgumentException(
          "Tenant does not exist for: " + aTenantId);
    }
    return tenant;
  }
  
  private Group existingGroup(String aTenantId, String aGroupName) {
    Group group = this.group(aTenantId, aGroupName);
    
    if (group == null) {
      throw new IllegalArgumentException(
          "Group does not exist for: "
              + aTenantId + " and: " + aGroupName);
    }
    
    return group;
  }
  
  
  private User existingUser(String aTenantId, String aUsername) {
    User user = this.user(aTenantId, aUsername);
    
    if (user == null) {
      throw new IllegalArgumentException(
          "User does not exist for: "
              + aTenantId + " and " + aUsername);
    }
    
    return user;
  }
  
}
