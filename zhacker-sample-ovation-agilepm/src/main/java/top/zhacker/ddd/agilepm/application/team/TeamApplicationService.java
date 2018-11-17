package top.zhacker.ddd.agilepm.application.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhacker.ddd.agilepm.application.team.command.AssignProductOwnerCommand;
import top.zhacker.ddd.agilepm.application.team.command.AssignTeamMemberCommand;
import top.zhacker.ddd.agilepm.application.team.command.ProvisionTeamCommand;
import top.zhacker.ddd.agilepm.application.team.command.RemoveTeamMemberCommand;
import top.zhacker.ddd.agilepm.domain.team.Team;
import top.zhacker.ddd.agilepm.domain.team.TeamRepo;
import top.zhacker.ddd.agilepm.domain.team.member.ProductOwner;
import top.zhacker.ddd.agilepm.domain.team.member.ProductOwnerRepository;
import top.zhacker.ddd.agilepm.domain.team.member.TeamMember;
import top.zhacker.ddd.agilepm.domain.team.member.TeamMemberRepo;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;
import top.zhacker.boot.leveldb.LevelDbTransactional;

import java.util.Collection;
import java.util.Set;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午3:40
 */
@Service
public class TeamApplicationService {
  
  @Autowired
  private TeamRepo teamRepo;
  
  @Autowired
  private ProductOwnerRepository productOwnerRepo;
  
  @Autowired
  private TeamMemberRepo teamMemberRepo;
  
  public Collection<Team> allTeamsOfTenant(String aTenantId){
    return teamRepo.allTeamsOfTenant(new TenantId(aTenantId));
  }
  
  public Team teamNamed(String aTenantId, String aName){
    return teamRepo.teamNamed(new TenantId(aTenantId), aName);
  }
  
  @LevelDbTransactional
  public Team provisionTeam(ProvisionTeamCommand command){
    Team team = new Team(new TenantId(command.getTenantId()), command.getName());
    teamRepo.save(team);
    return team;
  }
  
  
  @LevelDbTransactional
  public void assignProductOwner(AssignProductOwnerCommand command){
    TenantId tenantId = new TenantId(command.getTenantId());
    Team team = teamRepo.teamNamed(tenantId, command.getTeamName());
    ProductOwner productOwner = productOwnerRepo.productOwnerOfIdentity(tenantId, command.getUsername());
    
    team.assignProductOwner(productOwner);
    
    teamRepo.save(team);
  }
  
  @LevelDbTransactional
  public void assignTeamMember(AssignTeamMemberCommand command){
    
    TenantId tenantId = new TenantId(command.getTenantId());
    Team team = teamRepo.teamNamed(tenantId, command.getTeamName());
    
    TeamMember teamMember = teamMemberRepo.teamMemberOfIdentity(tenantId, command.getUsername());
  
    team.assignTeamMember(teamMember);
  
    teamRepo.save(team);
  }
  
  @LevelDbTransactional
  public void removeTeamMember(RemoveTeamMemberCommand command){
  
    TenantId tenantId = new TenantId(command.getTenantId());
    
    Team team = teamRepo.teamNamed(tenantId, command.getTeamName());
  
    TeamMember teamMember = teamMemberRepo.teamMemberOfIdentity(tenantId, command.getUsername());
  
    team.removeTeamMember(teamMember);
  
    teamRepo.save(team);
  }
  
  public Set<TeamMember> allTeamMembers(String tenantId, String name){
    
    return teamRepo.teamNamed(new TenantId(tenantId), name)
        .allTeamMembers();
  }
}
