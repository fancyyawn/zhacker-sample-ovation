package top.zhacker.ddd.agilepm.domain.team.member;

import top.zhacker.ddd.agilepm.domain.tenant.TenantId;

import java.util.Collection;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午10:19
 */
public interface TeamMemberRepo {
  
  public Collection<TeamMember> allTeamMembersOfTenant(TenantId aTenantId);
  
  public void remove(TeamMember aTeamMember);
  
  public void removeAll(Collection<TeamMember> aTeamMemberCollection);
  
  public void save(TeamMember aTeamMember);
  
  public void saveAll(Collection<TeamMember> aTeamMemberCollection);
  
  public TeamMember teamMemberOfIdentity(TenantId aTenantId, String aUsername);
}
