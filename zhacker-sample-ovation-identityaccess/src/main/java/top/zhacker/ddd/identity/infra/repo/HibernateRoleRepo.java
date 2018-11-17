package top.zhacker.ddd.identity.infra.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import top.zhacker.ddd.identity.domain.role.Role;
import top.zhacker.ddd.identity.domain.role.RoleRepo;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/13 上午11:30
 */
@Component
public class HibernateRoleRepo implements RoleRepo {
  
  @Autowired
  private EntityManager entityManager;
  
  @Override
  public void add(Role role) {
    entityManager.persist(role);
  }
  
  
  @Override
  public Collection<Role> allRoles(TenantId tenantId) {
    TypedQuery<Role> query = entityManager.createQuery(
        "select role from top.zhacker.ddd.identity.domain.role.Role role where role.tenantId = :tenantId",
        Role.class);
    query.setParameter("tenantId", tenantId);
    return query.getResultList();
  }
  
  
  @Override
  public void remove(Role role) {
    entityManager.remove(role);
  }
  
  
  @Override
  public Role roleNamed(TenantId tenantId, String roleName) {
    
    return entityManager.createQuery(
        "select role from top.zhacker.ddd.identity.domain.role.Role role " +
            "where role.tenantId = :tenantId and role.name = :name",
        Role.class
    ).setParameter("tenantId", tenantId)
        .setParameter("name", roleName)
        .getResultList()
        .stream()
        .findFirst()
        .orElse(null);
  }
}
