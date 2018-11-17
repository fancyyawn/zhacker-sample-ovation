package top.zhacker.ddd.identity.domain.role;

import java.util.Collection;

import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/13 上午11:28
 */
public interface RoleRepo {
  
  void add(Role role);
  
  Collection<Role> allRoles(TenantId tenantId);
  
  void remove(Role role);
  
  Role roleNamed(TenantId tenantId, String roleName);
  
}
