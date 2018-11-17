package top.zhacker.ddd.identity.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

import top.zhacker.ddd.identity.application.AccessApplicationService;
import top.zhacker.ddd.identity.application.command.AssignGroupToRoleCommand;
import top.zhacker.ddd.identity.application.command.AssignUserToRoleCommand;
import top.zhacker.ddd.identity.application.command.ProvisionRoleCommand;
import top.zhacker.ddd.identity.application.command.UnassignGroupFromRoleCommand;
import top.zhacker.ddd.identity.application.command.UnassignUserFromRoleCommand;
import top.zhacker.ddd.identity.domain.group.Group;
import top.zhacker.ddd.identity.domain.role.Role;
import top.zhacker.ddd.identity.domain.role.RoleSimple;


/**
 * Created by zhacker.
 * Time 2018/7/7 下午8:45
 */
@RestController
@RequestMapping("/v1/roles")
public class RoleApi {
  
  @Autowired
  private AccessApplicationService accessApplicationService;
  
  @GetMapping("/list")
  public Collection<RoleSimple> allRoles(String tenantId){
    return accessApplicationService.allRoles(tenantId).stream().map(Role::toSimple).collect(Collectors.toList());
  }
  
  @GetMapping("detail")
  public RoleSimple role(String tenantId, String roleName){
    return accessApplicationService.roleNamed(tenantId, roleName).toSimple();
  }
  
  @GetMapping("/role-group")
  public Group roleGroup(String tenantId, String roleName){
    return accessApplicationService.roleNamed(tenantId, roleName).getGroup();
  }
  
  @PostMapping("provision")
  public RoleSimple create(@RequestBody ProvisionRoleCommand command){
    return accessApplicationService.provisionRole(command).toSimple();
  }
  
  @PostMapping("/assign-user")
  public void assignUserToRole(@RequestBody AssignUserToRoleCommand aCommand){
    accessApplicationService.assignUserToRole(aCommand);
  }
  
  @PostMapping("/unassign-user")
  public void unassignUserToRole(@RequestBody UnassignUserFromRoleCommand aCommand){
    accessApplicationService.unassignUserFromRole(aCommand);
  }
  
  @PostMapping("/assign-group")
  public void assignGroupToRole(@RequestBody AssignGroupToRoleCommand aCommand){
    accessApplicationService.assignGroupToRole(aCommand);
  }
  
  @PostMapping("/unassign-group")
  public void unassignGroupToRole(@RequestBody UnassignGroupFromRoleCommand aCommand){
    accessApplicationService.unassignGroupFromRole(aCommand);
  }
  
  
}
