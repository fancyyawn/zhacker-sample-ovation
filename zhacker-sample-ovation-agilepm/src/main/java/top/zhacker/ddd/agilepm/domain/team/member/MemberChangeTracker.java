package top.zhacker.ddd.agilepm.domain.team.member;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import top.zhacker.core.model.AssertionConcern;

import java.util.Date;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午10:19
 */
@ToString
@Getter
@EqualsAndHashCode(callSuper = false)
public class MemberChangeTracker extends AssertionConcern {
  private Date emailAddressChangedOn;
  private Date enablingOn;
  private Date nameChangedOn;
  
  private MemberChangeTracker() {
    super();
  }
  
  protected MemberChangeTracker(
      Date anEnablingOn,
      Date aNameChangedOn,
      Date anEmailAddressChangedOn) {
    
    this();
    
    this.setEmailAddressChangedOn(anEmailAddressChangedOn);
    this.setEnablingOn(anEnablingOn);
    this.setNameChangedOn(aNameChangedOn);
  }
  
  protected MemberChangeTracker(MemberChangeTracker aMemberChangeTracker) {
    this(aMemberChangeTracker.getEnablingOn(),
        aMemberChangeTracker.getNameChangedOn(),
        aMemberChangeTracker.getEmailAddressChangedOn());
  }
  
  public void setEmailAddressChangedOn(Date emailAddressChangedOn) {
    this.assertArgumentNotNull(emailAddressChangedOn, "Email address changed on date must be provided.");
  
    this.emailAddressChangedOn = emailAddressChangedOn;
  }
  
  
  public void setEnablingOn(Date enablingOn) {
    this.assertArgumentNotNull(enablingOn, "Enabling date must be provided.");
  
    this.enablingOn = enablingOn;
  }
  
  
  public void setNameChangedOn(Date nameChangedOn) {
    this.assertArgumentNotNull(nameChangedOn, "Name changed on date must be provided.");
  
    this.nameChangedOn = nameChangedOn;
  }
  
  public boolean canChangeEmailAddress(Date asOfDate) {
    return this.getEmailAddressChangedOn().before(asOfDate);
  }
  
  public boolean canChangeName(Date asOfDate) {
    return this.getNameChangedOn().before(asOfDate);
  }
  
  public boolean canToggleEnabling(Date asOfDate) {
    return this.getEnablingOn().before(asOfDate);
  }
  
  public MemberChangeTracker emailAddressChangedOn(Date asOfDate) {
    return new MemberChangeTracker(this.getEnablingOn(), this.getNameChangedOn(), asOfDate);
  }
  
  public MemberChangeTracker enablingOn(Date asOfDate) {
    return new MemberChangeTracker(asOfDate, this.getNameChangedOn(), this.getEmailAddressChangedOn());
  }
  
  public MemberChangeTracker nameChangedOn(Date asOfDate) {
    return new MemberChangeTracker(this.getEnablingOn(), asOfDate, this.getEmailAddressChangedOn());
  }
  
}
