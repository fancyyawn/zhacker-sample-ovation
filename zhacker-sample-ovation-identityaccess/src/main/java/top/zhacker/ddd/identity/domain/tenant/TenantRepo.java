package top.zhacker.ddd.identity.domain.tenant;

import java.util.List;

/**
 * Created by zhacker.
 * Time 2018/6/11 下午7:04
 */
public interface TenantRepo {
  
  void add(Tenant tenant);
  
  Tenant findByTenantId(TenantId tenantId);
  
  TenantId nextIdentity();

  List<Tenant> findAll();

}
