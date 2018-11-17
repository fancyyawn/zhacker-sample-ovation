package top.zhacker.ddd.identity.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import top.zhacker.ddd.identity.application.IdentityApplicationService;
import top.zhacker.ddd.identity.application.command.AddGroupToGroupCommand;
import top.zhacker.ddd.identity.application.command.AddUserToGroupCommand;
import top.zhacker.ddd.identity.application.command.ProvisionGroupCommand;
import top.zhacker.ddd.identity.application.command.RemoveGroupFromGroupCommand;
import top.zhacker.ddd.identity.application.command.RemoveUserFromGroupCommand;
import top.zhacker.ddd.identity.domain.group.Group;


/**
 * Created by zhacker.
 * Time 2018/7/6 下午10:23
 */
@RestController
@RequestMapping("/v1/groups")
public class GroupApi {
  
  @Autowired
  private IdentityApplicationService applicationService;
  
  @PostMapping("/provision")
  public Group provisionGroup(@RequestBody ProvisionGroupCommand aCommand) {
    return applicationService.provisionGroup(aCommand);
  }
  
  @PostMapping("/add-group")
  public void addGroupToGroup(@RequestBody AddGroupToGroupCommand aCommand) {
    applicationService.addGroupToGroup(aCommand);
  }
  
  @PostMapping("/add-user")
  public void addUserToGroup(@RequestBody AddUserToGroupCommand aCommand) {
    applicationService.addUserToGroup(aCommand);
  }
  
  @PostMapping("/remove-group")
  public void removeGroupFromGroup(@RequestBody RemoveGroupFromGroupCommand aCommand) {
    applicationService.removeGroupFromGroup(aCommand);
  }
  
  @PostMapping("/remove-user")
  public void removeUserFromGroup(RemoveUserFromGroupCommand aCommand) {
    applicationService.removeUserFromGroup(aCommand);
  }
  
  @GetMapping("detail")
  public Group group(String tenantId, String groupName){
    return applicationService.group(tenantId, groupName);
  }
  
  @GetMapping("/list")
  public Collection<Group> allGroups(String tenantId){
    return applicationService.allGroups(tenantId);
  }
}
