package top.zhacker.ddd.agilepm.domain.team.member;

import lombok.Getter;
import lombok.ToString;
import top.zhacker.core.model.ConcurrencySafeEntity;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;

import java.util.Date;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午10:13
 */
@Getter
@ToString
public class Member extends ConcurrencySafeEntity {

  private TenantId tenantId;
  private String username;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private boolean enabled = true;
  private MemberChangeTracker changeTracker;
  
  protected Member() {
    super();
  }
  
  protected Member(
      TenantId aTenantId,
      String aUsername,
      String aFirstName,
      String aLastName,
      String anEmailAddress) {
    
    this();
    
    this.setEmailAddress(anEmailAddress);
    this.setFirstName(aFirstName);
    this.setLastName(aLastName);
    this.setTenantId(aTenantId);
    this.setUsername(aUsername);
  }
  
  public Member(
      TenantId aTenantId,
      String aUsername,
      String aFirstName,
      String aLastName,
      String anEmailAddress,
      Date anInitializedOn) {
    
    this(aTenantId, aUsername, aFirstName, aLastName, anEmailAddress);
    
    this.setChangeTracker(
        new MemberChangeTracker(
            anInitializedOn,
            anInitializedOn,
            anInitializedOn));
  }
  
  public void setTenantId(TenantId tenantId) {
    this.tenantId = tenantId;
  }
  
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  
  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }
  
  
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  
  
  public void setChangeTracker(MemberChangeTracker changeTracker) {
    this.changeTracker = changeTracker;
  }
  
  public void changeEmailAddress(String anEmailAddress, Date asOfDate) {
    if (this.getChangeTracker().canChangeEmailAddress(asOfDate) &&
        !this.getEmailAddress().equals(anEmailAddress)) {
      this.setEmailAddress(anEmailAddress);
      this.setChangeTracker(this.getChangeTracker().emailAddressChangedOn(asOfDate));
    }
  }
  
  public void changeName(String aFirstName, String aLastName, Date asOfDate) {
    if (this.getChangeTracker().canChangeName(asOfDate)) {
      this.setFirstName(aFirstName);
      this.setLastName(aLastName);
      this.setChangeTracker(this.getChangeTracker().nameChangedOn(asOfDate));
    }
  }
  
  public void disable(Date asOfDate) {
    if (this.getChangeTracker().canToggleEnabling(asOfDate)) {
      this.setEnabled(false);
      this.setChangeTracker(this.getChangeTracker().enablingOn(asOfDate));
    }
  }
  
  public void enable(Date asOfDate) {
    if (this.getChangeTracker().canToggleEnabling(asOfDate)) {
      this.setEnabled(true);
      this.setChangeTracker(this.getChangeTracker().enablingOn(asOfDate));
    }
  }
  
  public String emailAddress() {
    return this.emailAddress;
  }
  
  public boolean isEnabled() {
    return this.enabled;
  }
  
  public String firstName() {
    return this.firstName;
  }
  
  public String lastName() {
    return this.lastName;
  }
  
  public TenantId tenantId() {
    return this.tenantId;
  }
  
  public String username() {
    return this.username;
  }
}
