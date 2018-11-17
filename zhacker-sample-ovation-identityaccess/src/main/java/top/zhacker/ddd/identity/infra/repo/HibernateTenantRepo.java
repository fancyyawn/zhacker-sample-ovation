package top.zhacker.ddd.identity.infra.repo;

import com.alibaba.fastjson.JSON;

import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import lombok.extern.slf4j.Slf4j;
import top.zhacker.ddd.identity.domain.tenant.Tenant;
import top.zhacker.ddd.identity.domain.tenant.TenantId;
import top.zhacker.ddd.identity.domain.tenant.TenantRepo;


/**
 * Created by zhacker.
 * Time 2018/6/13 上午10:05
 */
@Slf4j
@Repository
public class HibernateTenantRepo implements TenantRepo {
  
  @Autowired
  private EntityManager entityManager;
  
  @Autowired(required = false)
  private HibernateEntityManager hibernateEntityManager;
  
  @PostConstruct
  public void init(){
    log.info("{}", entityManager instanceof  HibernateEntityManager);
    
  }
  
  @Override
  public void add(Tenant tenant) {
    log.info("added Tenant={}", JSON.toJSONString(tenant));
    entityManager.persist(tenant);
  }
  
  
  @Override
  public Tenant findByTenantId(TenantId tenantId) {
    Query query = entityManager.createQuery("select t from top.zhacker.ddd.identity.domain.tenant.Tenant t where t.tenantId = :tenantId");
    query.setParameter("tenantId", tenantId);
    
    return (Tenant)query.getResultList().stream().findFirst().orElse(null);
  }
  
  
  @Override
  public TenantId nextIdentity() {
    return new TenantId(UUID.randomUUID().toString());
  }

  @Override
  public List<Tenant> findAll() {
    return entityManager.createQuery("select t from top.zhacker.ddd.identity.domain.tenant.Tenant t", Tenant.class)
            .getResultList();
  }
}
