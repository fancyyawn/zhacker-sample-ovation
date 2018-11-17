package top.zhacker.ddd.agilepm.domain.team.member;

import top.zhacker.ddd.agilepm.domain.tenant.TenantId;

import java.util.Collection;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午10:18
 */
public interface ProductOwnerRepository {
  
  public Collection<ProductOwner> allProductOwnersOfTenant(TenantId aTenantId);
  
  public ProductOwner productOwnerOfIdentity(TenantId aTenantId, String aUsername);
  
  public void remove(ProductOwner aProductOwner);
  
  public void removeAll(Collection<ProductOwner> aProductOwnerCollection);
  
  public void save(ProductOwner aProductOwner);
  
  public void saveAll(Collection<ProductOwner> aProductOwnerCollection);
}
