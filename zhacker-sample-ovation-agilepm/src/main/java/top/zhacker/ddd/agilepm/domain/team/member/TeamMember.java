package top.zhacker.ddd.agilepm.domain.team.member;

import lombok.Getter;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;

import java.util.Date;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午10:15
 */
@Getter
public class TeamMember extends Member {
  
  protected TeamMember() {
    super();
  }
  
  public TeamMember(
      TenantId aTenantId,
      String aUsername,
      String aFirstName,
      String aLastName,
      String anEmailAddress,
      Date anInitializedOn) {
    
    super(aTenantId, aUsername, aFirstName, aLastName, anEmailAddress, anInitializedOn);
  }
  
  public TeamMemberId getTeamMemberId() {
    return new TeamMemberId(this.getTenantId(), this.getUsername());
  }
  
  public TeamMemberId teamMemberId() {
    return new TeamMemberId(this.tenantId(), this.username());
  }
  
}
