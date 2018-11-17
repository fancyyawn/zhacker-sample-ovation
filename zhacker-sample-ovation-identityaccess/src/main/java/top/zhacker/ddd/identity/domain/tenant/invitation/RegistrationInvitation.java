package top.zhacker.ddd.identity.domain.tenant.invitation;

import java.util.Date;

import lombok.Getter;
import top.zhacker.core.model.IdentifiedEntity;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/13 上午10:46
 */
@Getter
public class RegistrationInvitation extends IdentifiedEntity {
  
  private TenantId tenantId;
  private String invitationId;
  private String description;
  private Date startingOn;
  private Date until;
  
  public RegistrationInvitation(
      TenantId aTenantId,
      String anInvitationId,
      String aDescription) {
    
    
    this.setDescription(aDescription);
    this.setInvitationId(anInvitationId);
    this.setTenantId(aTenantId);
  }
  
  
  public RegistrationInvitation() {
  }
  
  
  public void setTenantId(TenantId tenantId) {
    this.tenantId = tenantId;
  }
  
  
  public void setInvitationId(String invitationId) {
    this.invitationId = invitationId;
  }
  
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  
  public void setStartingOn(Date startingOn) {
    this.startingOn = startingOn;
  }
  
  
  public void setUntil(Date until) {
    this.until = until;
  }
  
  
  public RegistrationInvitation openEnded() {
    this.setStartingOn(null);
    this.setUntil(null);
    return this;
  }
  
  public boolean isIdentifiedBy(String anInvitationIdentifier) {
    boolean isIdentified = this.getInvitationId().equals(anInvitationIdentifier);
    if (!isIdentified && this.getDescription() != null) {
      isIdentified = this.getDescription().equals(anInvitationIdentifier);
    }
    return isIdentified;
  }
  
  public boolean isAvailable() {
    boolean isAvailable = false;
    if (this.getStartingOn() == null && this.getUntil() == null) {
      isAvailable = true;
    } else {
      long time = (new Date()).getTime();
      if (time >= this.getStartingOn().getTime() && time <= this.getUntil().getTime()) {
        isAvailable = true;
      }
    }
    return isAvailable;
  }
  
  public InvitationDescriptor toDescriptor() {
    return
        new InvitationDescriptor(
            this.tenantId,
            this.invitationId,
            this.description,
            this.startingOn,
            this.until);
  }
}
