package top.zhacker.ddd.identity.infra.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import lombok.extern.slf4j.Slf4j;
import top.zhacker.ddd.identity.domain.tenant.TenantId;
import top.zhacker.ddd.identity.domain.user.User;
import top.zhacker.ddd.identity.domain.user.UserRepo;
import top.zhacker.ddd.identity.infra.repo.impl.SpringDataUserRepo;


/**
 * Created by zhacker.
 * Time 2018/6/13 上午11:30
 */
@Slf4j
@Component
public class HibernateUserRepo implements UserRepo {
  
  @Autowired
  private SpringDataUserRepo springDataUserRepo;
  
  @Autowired
  private EntityManager entityManager;
  
  @Override
  public User save(User user) {
    return springDataUserRepo.save(user);
  }
  
  
  @Override
  public User userWithUsername(TenantId tenantId, String username) {
    return springDataUserRepo.findByTenantIdAndUsername(tenantId, username);
  }
  
  
  @Override
  public User userFromAuthenticCredentials(TenantId aTenantId, String aUsername, String anEncryptedPassword) {
    return springDataUserRepo.findByTenantIdAndUsernameAndPassword(aTenantId, aUsername, anEncryptedPassword);
  }
  
  
  @Override
  public void remove(User aUser) {
    springDataUserRepo.delete(aUser);
  }
  
  
  @Override
  public Collection<User> allSimilarlyNamedUsers(TenantId aTenantId, String aFirstNamePrefix, String aLastNamePrefix) {
    
    if (aFirstNamePrefix.endsWith("%") || aLastNamePrefix.endsWith("%")) {
      throw new IllegalArgumentException("Name prefixes must not include %.");
    }
  
    TypedQuery<User> query = entityManager.createQuery(
        "select _obj_ from top.zhacker.ddd.identity.domain.user.User as _obj_ "
            + "where _obj_.tenantId = :tenantId "
            +   "and _obj_.person.name.firstName like :firstName "
            +   "and _obj_.person.name.lastName like :lastName", User.class);
  
    query.setParameter("tenantId", aTenantId);
    query.setParameter("firstName", aFirstNamePrefix + "%");
    query.setParameter("lastName", aLastNamePrefix + "%");
  
    return query.getResultList();
  }
  
  @Override
  public Collection<User> allUsers(TenantId aTenantId) {
    
    TypedQuery<User> query = entityManager.createQuery(
        "select _obj_ from top.zhacker.ddd.identity.domain.user.User as _obj_ "
            + "where _obj_.tenantId = :tenantId "
        , User.class);
    
    query.setParameter("tenantId", aTenantId);
    
    return query.getResultList();
  }
}
