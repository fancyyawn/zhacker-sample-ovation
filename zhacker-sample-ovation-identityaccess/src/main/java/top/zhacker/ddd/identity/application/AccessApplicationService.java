package top.zhacker.ddd.identity.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import top.zhacker.ddd.identity.application.command.AssignGroupToRoleCommand;
import top.zhacker.ddd.identity.application.command.AssignUserToRoleCommand;
import top.zhacker.ddd.identity.application.command.ProvisionRoleCommand;
import top.zhacker.ddd.identity.application.command.UnassignGroupFromRoleCommand;
import top.zhacker.ddd.identity.application.command.UnassignUserFromRoleCommand;
import top.zhacker.ddd.identity.domain.group.Group;
import top.zhacker.ddd.identity.domain.group.GroupRepo;
import top.zhacker.ddd.identity.domain.role.Role;
import top.zhacker.ddd.identity.domain.role.RoleRepo;
import top.zhacker.ddd.identity.domain.tenant.Tenant;
import top.zhacker.ddd.identity.domain.tenant.TenantId;
import top.zhacker.ddd.identity.domain.tenant.TenantRepo;
import top.zhacker.ddd.identity.domain.user.User;
import top.zhacker.ddd.identity.domain.user.UserRepo;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午10:43
 */
@Component
public class AccessApplicationService {
  
  @Autowired
  private GroupRepo groupRepo;
  
  @Autowired
  private RoleRepo roleRepo;
  
  @Autowired
  private TenantRepo tenantRepo;
  
  @Autowired
  private UserRepo userRepo;
  
  @Transactional(readOnly=true)
  public Collection<Role> allRoles(String tenantId){
    return roleRepo.allRoles(new TenantId(tenantId));
    
  }
  
  @Transactional(readOnly=true)
  public Role roleNamed(String tenantId, String roleName){
    return roleRepo.roleNamed(new TenantId(tenantId), roleName);
  }
  
  @Transactional
  public Role provisionRole(ProvisionRoleCommand aCommand) {
    
    TenantId tenantId = new TenantId(aCommand.getTenantId());
    
    Tenant tenant = this.tenantRepo.findByTenantId(tenantId);
    
    Role role =
        tenant.provisionRole(
            aCommand.getRoleName(),
            aCommand.getDescription(),
            aCommand.isSupportsNesting());
    
    this.roleRepo.add(role);
    
    return role;
  }
  
  
  @Transactional
  public void assignUserToRole(AssignUserToRoleCommand aCommand) {
    
    TenantId tenantId = new TenantId(aCommand.getTenantId());
    
    User user =
        this.userRepo
            .userWithUsername(
                tenantId,
                aCommand.getUsername());
    
    if (user != null) {
      Role role =
          this.roleRepo
              .roleNamed(
                  tenantId,
                  aCommand.getRoleName());
      
      if (role != null) {
        role.assignUser(user);
      }
    }
  }
  
  @Transactional
  public void assignGroupToRole(AssignGroupToRoleCommand aCommand) {
    
    TenantId tenantId = new TenantId(aCommand.getTenantId());
    
    Group group =
        this.groupRepo
            .groupNamed(
                tenantId,
                aCommand.getGroupName());
    
    if (group != null) {
      Role role =
          this.roleRepo
              .roleNamed(
                  tenantId,
                  aCommand.getRoleName());
      
      if (role != null) {
        role.assignGroup(group);
      }
    }
  }
  
  
  
  @Transactional
  public void unassignGroupFromRole(UnassignGroupFromRoleCommand aCommand) {
    
    TenantId tenantId = new TenantId(aCommand.getTenantId());
    
    Group group =
        this.groupRepo
            .groupNamed(
                tenantId,
                aCommand.getGroupName());
    
    if (group != null) {
      Role role =
          this.roleRepo
              .roleNamed(
                  tenantId,
                  aCommand.getRoleName());
      
      if (role != null) {
        role.unassignGroup(group);
      }
    }
  }
  
  @Transactional
  public void unassignUserFromRole(UnassignUserFromRoleCommand aCommand) {
    
    TenantId tenantId = new TenantId(aCommand.getTenantId());
    
    User user =
        this.userRepo
            .userWithUsername(
                tenantId,
                aCommand.getUsername());
    
    if (user != null) {
      Role role =
          this.roleRepo
              .roleNamed(
                  tenantId,
                  aCommand.getRoleName());
      
      if (role != null) {
        role.unassignUser(user);
      }
    }
  }
  
  @Transactional(readOnly=true)
  public boolean isUserInRole(
      String aTenantId,
      String aUsername,
      String aRoleName) {
    
    User user = this.userInRole(aTenantId, aUsername, aRoleName);
    
    return user != null;
  }
  
  @Transactional(readOnly=true)
  public User userInRole(
      String aTenantId,
      String aUsername,
      String aRoleName) {
    
    User userInRole = null;
    
    TenantId tenantId = new TenantId(aTenantId);
    
    User user =
        this.userRepo
            .userWithUsername(
                tenantId,
                aUsername);
    
    if (user != null) {
      Role role =
          this.roleRepo
              .roleNamed(tenantId, aRoleName);
      
      if (role != null) {
        if (role.isInRole(user)) {
          userInRole = user;
        }
      }
    }
    
    return userInRole;
  }
  
  
}
