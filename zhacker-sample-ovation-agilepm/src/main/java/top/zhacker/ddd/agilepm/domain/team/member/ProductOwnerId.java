package top.zhacker.ddd.agilepm.domain.team.member;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import top.zhacker.core.model.AssertionConcern;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午10:17
 */
@Getter
@EqualsAndHashCode(callSuper = false)
public class ProductOwnerId extends AssertionConcern {
  
  private TenantId tenantId;
  private String id;
  
  public ProductOwnerId(TenantId tenantId, String id) {
    this.tenantId = tenantId;
    this.id = id;
  }
  
  public void setTenantId(TenantId tenantId) {
    this.tenantId = tenantId;
  }
  
  public void setId(String id) {
    this.id = id;
  }
}
