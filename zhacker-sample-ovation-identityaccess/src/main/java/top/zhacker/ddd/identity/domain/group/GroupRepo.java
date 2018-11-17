package top.zhacker.ddd.identity.domain.group;

import java.util.Collection;

import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午1:45
 */
public interface GroupRepo {
  
  void add(Group group);
  
  Collection<Group> allGroups(TenantId tenantId);
  
  Group groupNamed(TenantId tenantId, String name);
  
  void remove(Group group);
}
