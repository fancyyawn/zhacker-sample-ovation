package top.zhacker.ddd.identity.infra.repo.impl;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import top.zhacker.ddd.identity.domain.tenant.TenantId;
import top.zhacker.ddd.identity.domain.user.User;


/**
 * Created by zhacker.
 * Time 2018/7/1 上午11:00
 */
@Repository
public interface SpringDataUserRepo extends CrudRepository<User, Long> {
  
  User findByTenantIdAndUsername(TenantId tenantId, String username);
  
  User findByTenantIdAndUsernameAndPassword(TenantId tenantId, String username, String password);
  
}
