package top.zhacker.ddd.identity.domain.user;

import java.util.Collection;

import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/13 上午11:29
 */
public interface UserRepo {
  
  User save(User user);
  
  User userWithUsername(TenantId tenantId, String username);
  
  User userFromAuthenticCredentials(
          TenantId aTenantId,
          String aUsername,
          String anEncryptedPassword);
  
  
  void remove(User aUser);
  
  
  Collection<User> allSimilarlyNamedUsers(
          TenantId aTenantId,
          String aFirstNamePrefix,
          String aLastNamePrefix);
  
  Collection<User> allUsers(TenantId aTenantId);
  
}
