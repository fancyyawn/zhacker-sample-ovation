package top.zhacker.ddd.identity.domain.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import top.zhacker.core.model.AssertionConcern;
import top.zhacker.core.model.AssertionConcern;
import top.zhacker.ddd.identity.domain.group.GroupRepo;
import top.zhacker.ddd.identity.domain.tenant.TenantId;
import top.zhacker.ddd.identity.domain.user.User;
import top.zhacker.ddd.identity.domain.user.UserRepo;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午5:45
 */
@Component
public class AuthorizationService extends AssertionConcern {
  
  private GroupRepo groupRepo;
  private RoleRepo roleRepo;
  private UserRepo userRepo;
  
  @Autowired
  public AuthorizationService(GroupRepo groupRepo, RoleRepo roleRepo, UserRepo userRepo) {
    this.groupRepo = groupRepo;
    this.roleRepo = roleRepo;
    this.userRepo = userRepo;
  }
  
  public boolean isUserInRole(TenantId tenantId, String username, String roleName){
    this.assertArgumentNotNull(tenantId, "TenantId must not be null.");
    this.assertArgumentNotEmpty(username, "Username must not be provided.");
    this.assertArgumentNotEmpty(roleName, "Role name must not be null.");
  
    User user = this.userRepo.userWithUsername(tenantId, username);
  
    return user == null ? false : this.isUserInRole(user, roleName);
  }
  
  public boolean isUserInRole(User user, String roleName){
    this.assertArgumentNotNull(user, "User must not be null.");
    this.assertArgumentNotEmpty(roleName, "Role name must not be null.");
  
    boolean authorized = false;
  
    if (user.isEnabled()) {
      Role role = this.roleRepo.roleNamed(user.getTenantId(), roleName);
    
      if (role != null) {
        authorized = role.isInRole(user);
      }
    }
  
    return authorized;
  }
}
