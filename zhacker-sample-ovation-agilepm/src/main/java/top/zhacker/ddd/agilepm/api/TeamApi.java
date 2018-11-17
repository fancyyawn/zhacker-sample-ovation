package top.zhacker.ddd.agilepm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zhacker.boot.aop.log.ParamLog;
import top.zhacker.ddd.agilepm.application.team.TeamApplicationService;
import top.zhacker.ddd.agilepm.application.team.command.AssignProductOwnerCommand;
import top.zhacker.ddd.agilepm.application.team.command.AssignTeamMemberCommand;
import top.zhacker.ddd.agilepm.application.team.command.ProvisionTeamCommand;
import top.zhacker.ddd.agilepm.application.team.command.RemoveTeamMemberCommand;
import top.zhacker.ddd.agilepm.domain.team.Team;
import top.zhacker.ddd.agilepm.domain.team.member.TeamMember;

import java.util.Collection;
import java.util.Set;


/**
 * Created by zhacker.
 * Time 2018/7/7 上午10:09
 */
@RestController
@RequestMapping("/v1/teams")
@ParamLog
public class TeamApi {
  
  @Autowired
  private TeamApplicationService teamApplicationService;
  
  
  @GetMapping("/list")
  public Collection<Team> allTeamsOfTenant(String aTenantId){
    return teamApplicationService.allTeamsOfTenant(aTenantId);
  }
  
  @GetMapping("detail")
  public Team team(String tenantId, String name){
    return teamApplicationService.teamNamed(tenantId, name);
  }
  
  @PostMapping("provision")
  public Team create(@RequestBody ProvisionTeamCommand command){
    return teamApplicationService.provisionTeam(command);
  }
  
  @PostMapping("/product-owner/assign")
  public void assignProductOwner(@RequestBody AssignProductOwnerCommand command){
    teamApplicationService.assignProductOwner(command);
  }
  
  @PostMapping("/member/assign")
  public void assignTeamMember(@RequestBody AssignTeamMemberCommand command){
    teamApplicationService.assignTeamMember(command);
  }
  
  @PostMapping("/member/remove")
  public void removeTeamMember(@RequestBody RemoveTeamMemberCommand command){
    teamApplicationService.removeTeamMember(command);
  }
  
  @GetMapping("/members-of-team")
  public Set<TeamMember> allTeamMembers(String tenantId, String name){
    return teamApplicationService.allTeamMembers(tenantId, name);
  }
  
}
