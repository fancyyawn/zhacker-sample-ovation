package top.zhacker.ddd.identity.infra.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

import javax.persistence.EntityManager;

import top.zhacker.ddd.identity.domain.group.Group;
import top.zhacker.ddd.identity.domain.group.GroupRepo;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午4:51
 */
@Component
public class HibernateGroupRepo implements GroupRepo {
  
  @Autowired
  private EntityManager entityManager;
  
  
  @Override
  public void add(Group group) {
    entityManager.persist(group);
  }
  
  
  @Override
  public Collection<Group> allGroups(TenantId tenantId) {
    return entityManager.createQuery(
        "select g from top.zhacker.ddd.identity.domain.group.Group g where g.tenantId = :tenantId",
        Group.class)
        .setParameter("tenantId", tenantId)
        .getResultList();
  }
  
  
  @Override
  public Group groupNamed(TenantId tenantId, String name) {
    return entityManager.createQuery(
        "select g from top.zhacker.ddd.identity.domain.group.Group g where g.tenantId = :tenantId and g.name = :name", Group.class)
        .setParameter("tenantId", tenantId)
        .setParameter("name", name)
        .getResultList()
        .stream()
        .findFirst()
        .orElse(null);
  }
  
  
  @Override
  public void remove(Group group) {
    entityManager.remove(group);
  }
}
