package top.zhacker.ddd.agilepm.domain.team;

import top.zhacker.ddd.agilepm.domain.tenant.TenantId;

import java.util.Collection;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午10:18
 */
public interface TeamRepo {
  
  void save(Team aTeam);
  
  void saveAll(Collection<Team> aTeamCollection);
  
  void remove(Team aTeam);
  
  void removeAll(Collection<Team> aTeamCollection);
  
  Collection<Team> allTeamsOfTenant(TenantId aTenantId);
  
  Team teamNamed(TenantId aTenantId, String aName);
}
