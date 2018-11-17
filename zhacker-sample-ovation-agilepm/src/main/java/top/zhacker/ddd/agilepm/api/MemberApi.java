package top.zhacker.ddd.agilepm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zhacker.boot.aop.log.ParamLog;
import top.zhacker.ddd.agilepm.application.member.MemberApplicationService;
import top.zhacker.ddd.agilepm.application.member.command.*;
import top.zhacker.ddd.agilepm.domain.team.member.ProductOwner;
import top.zhacker.ddd.agilepm.domain.team.member.TeamMember;

import java.util.Collection;


/**
 * Created by zhacker.
 * Time 2018/7/7 下午7:14
 */
@RestController
@RequestMapping("/v1/member")
@ParamLog
public class MemberApi {
  
  @Autowired
  private MemberApplicationService memberApplicationService;
  
  
  @GetMapping("/product-owner/list")
  public Collection<ProductOwner> productOwners(String tenantId){
    return memberApplicationService.allProductOwners(tenantId);
  }
  
  @GetMapping("/product-owner/detail")
  public ProductOwner productOwner(String tenantId, String username){
    return memberApplicationService.productOwnerOfIdentity(tenantId, username);
  }
  
  @PostMapping("/product-owner/enable")
  public void enableProductOwner(@RequestBody EnableProductOwnerCommand aCommand) {
    memberApplicationService.enableProductOwner(aCommand);
  }
  
  @PostMapping("/product-owner/disable")
  public void disableProductOwner(@RequestBody DisableProductOwnerCommand aCommand) {
    memberApplicationService.disableProductOwner(aCommand);
  }
  
  
  @GetMapping("/normal/list")
  public Collection<TeamMember> allTeamMembersOfTenant(String tenantId){
    return memberApplicationService.allTeamMembersOfTenant(tenantId);
  }
  
  @GetMapping("/normal/detail")
  public TeamMember teamMember(String tenantId, String username){
    return memberApplicationService.teamMemberOfIdentity(tenantId, username);
  }
  
  @PostMapping("/normal/enable")
  public void enableTeamMember(@RequestBody EnableTeamMemberCommand aCommand) {
    memberApplicationService.enableTeamMember(aCommand);
  }
  
  @PostMapping("/normal/disable")
  public void disableTeamMember(DisableTeamMemberCommand aCommand) {
    memberApplicationService.disableTeamMember(aCommand);
  }
  
  @PostMapping("/change-email")
  public void changeTeamMemberEmailAddress(@RequestBody ChangeTeamMemberEmailAddressCommand aCommand) {
    memberApplicationService.changeTeamMemberEmailAddress(aCommand);
  }
  
  @PostMapping("/change-name")
  public void changeTeamMemberName(@RequestBody ChangeTeamMemberNameCommand aCommand) {
    memberApplicationService.changeTeamMemberName(aCommand);
  }
}
