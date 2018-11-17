package top.zhacker.ddd.identity.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.zhacker.boot.event.publish.DomainEventPublisher;
import top.zhacker.core.model.IdentifiedEntity;
import top.zhacker.boot.registry.DomainRegistry;
import top.zhacker.ddd.identity.domain.group.GroupMember;
import top.zhacker.ddd.identity.domain.group.GroupMemberType;
import top.zhacker.ddd.identity.domain.user.event.UserEnablementChanged;
import top.zhacker.ddd.identity.domain.user.event.UserPasswordChanged;
import top.zhacker.ddd.identity.domain.user.person.ContactInformation;
import top.zhacker.ddd.identity.domain.user.person.FullName;
import top.zhacker.ddd.identity.domain.user.person.Person;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/13 上午11:08
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends IdentifiedEntity {
  
  private static final long serialVersionUID = 1L;
  
  private TenantId tenantId;
  private String username;
  private String password;
  private Enablement enablement;
  private Person person;
  
  public User(
      TenantId aTenantId,
      String aUsername,
      String aPassword,
      Enablement anEnablement,
      Person aPerson) {
    
    
    this.setEnablement(anEnablement);
    this.setPerson(aPerson);
    this.setTenantId(aTenantId);
    this.setUsername(aUsername);
    
    this.protectPassword("", aPassword);
    
    aPerson.internalOnlySetUser(this);
    
    
  }
  
  public void setEnablement(Enablement enablement) {
    this.enablement = enablement;
  }
  
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  
  public void setPerson(Person person) {
    this.person = person;
  }
  
  
  public void setTenantId(TenantId tenantId) {
    this.tenantId = tenantId;
  }
  
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  protected void protectPassword(String aCurrentPassword, String aChangedPassword) {
    this.assertPasswordsNotSame(aCurrentPassword, aChangedPassword);

    this.assertPasswordNotWeak(aChangedPassword);

    this.assertUsernamePasswordNotSame(aChangedPassword);

    this.setPassword(this.asEncryptedValue(aChangedPassword));
  }
  
  public boolean isEnabled(){
    return this.enablement.isEnablementEnabled();
  }
  
  public void changePassword(String aCurrentPassword, String aChangedPassword) {
    this.assertArgumentNotEmpty(
        aCurrentPassword,
        "Current and new password must be provided.");
    
    this.assertArgumentEquals(
        this.password,
        this.asEncryptedValue(aCurrentPassword),
        "Current password not confirmed.");
    
    this.protectPassword(aCurrentPassword, aChangedPassword);
    
    DomainEventPublisher
        .publish(new UserPasswordChanged(
            this.tenantId,
            this.username));
  }
  
  public void changePersonalContactInformation(ContactInformation aContactInformation) {
    this.person.changeContactInformation(aContactInformation);
  }
  
  public void changePersonalName(FullName aPersonalName) {
    this.person.changeName(aPersonalName);
  }
  
  public void defineEnablement(Enablement anEnablement) {
    this.setEnablement(anEnablement);
    
    DomainEventPublisher
        .publish(new UserEnablementChanged(
            this.getTenantId(),
            this.getUsername(),
            this.getEnablement()));
  }
  
  protected String asEncryptedValue(String aPlainTextPassword) {
    
    String encryptedValue =
        DomainRegistry.service(EncryptionService.class)
            .encryptedValue(aPlainTextPassword);
    
    return encryptedValue;
  }
  
  protected void assertPasswordsNotSame(String aCurrentPassword, String aChangedPassword) {
    this.assertArgumentNotEquals(
        aCurrentPassword,
        aChangedPassword,
        "The password is unchanged.");
  }
  
  protected void assertPasswordNotWeak(String aPlainTextPassword) {
    this.assertArgumentFalse(
        DomainRegistry.service(PasswordService.class).isWeak(aPlainTextPassword),
        "The password must be stronger.");
  }
  
  protected void assertUsernamePasswordNotSame(String aPlainTextPassword) {
    this.assertArgumentNotEquals(
        this.username,
        aPlainTextPassword,
        "The username and password must not be the same.");
  }
  
  public GroupMember toGroupMember() {
    GroupMember groupMember =
        new GroupMember(
            this.tenantId,
            this.username,
            GroupMemberType.User);
    
    return groupMember;
  }
  
  public UserDescriptor userDescriptor() {
    return new UserDescriptor(
        this.getTenantId(),
        this.getUsername(),
        this.getPerson().emailAddress().getAddress());
  }
  
}
