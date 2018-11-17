package top.zhacker.ddd.identity.domain.tenant.invitation;

import java.util.Date;

import lombok.Getter;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/13 上午10:52
 */
@Getter
public class InvitationDescriptor {
  private String description;
  private String invitationId;
  private Date startingOn;
  private TenantId tenantId;
  private Date until;
  
  
  public InvitationDescriptor(
      TenantId aTenantId,
      String anInvitationId,
      String aDescription,
      Date aStartingOn,
      Date anUntil) {
    
    super();
    
    this.setDescription(aDescription);
    this.setInvitationId(anInvitationId);
    this.setStartingOn(aStartingOn);
    this.setTenantId(aTenantId);
    this.setUntil(anUntil);
  }
  
  public InvitationDescriptor(InvitationDescriptor anInvitationDescriptor) {
    this(anInvitationDescriptor.getTenantId(),
        anInvitationDescriptor.getInvitationId(),
        anInvitationDescriptor.getDescription(),
        anInvitationDescriptor.getStartingOn(),
        anInvitationDescriptor.getUntil());
  }
  
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  
  public void setInvitationId(String invitationId) {
    this.invitationId = invitationId;
  }
  
  
  public void setStartingOn(Date startingOn) {
    this.startingOn = startingOn;
  }
  
  
  public void setTenantId(TenantId tenantId) {
    this.tenantId = tenantId;
  }
  
  
  public void setUntil(Date until) {
    this.until = until;
  }
}
