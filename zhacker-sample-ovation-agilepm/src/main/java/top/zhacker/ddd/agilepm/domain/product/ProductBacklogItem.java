package top.zhacker.ddd.agilepm.domain.product;

import lombok.Getter;
import top.zhacker.core.model.ConcurrencySafeEntity;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemId;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/20 下午9:55
 */
@Getter
public class ProductBacklogItem extends ConcurrencySafeEntity {
  
  private BacklogItemId backlogItemId;
  private int ordering;
  private ProductId productId;
  private TenantId tenantId;
  
  
  public BacklogItemId backlogItemId() {
    return this.backlogItemId;
  }
  
  public int ordering() {
    return this.ordering;
  }
  
  public ProductId productId() {
    return this.productId;
  }
  
  public TenantId tenantId() {
    return this.tenantId;
  }
  
  protected ProductBacklogItem(
      TenantId aTenantId,
      ProductId aProductId,
      BacklogItemId aBacklogItemId,
      int anOrdering) {
    
    this();
    
    this.setBacklogItemId(aBacklogItemId);
    this.setOrdering(anOrdering);
    this.setProductId(aProductId);
    this.setTenantId(aTenantId);
  }
  
  protected ProductBacklogItem() {
    super();
  }
  
  protected void setBacklogItemId(BacklogItemId aBacklogItemId) {
    this.assertArgumentNotNull(aBacklogItemId, "The backlog item id must be provided.");
    
    this.backlogItemId = aBacklogItemId;
  }
  
  protected void setOrdering(int anOrdering) {
    this.ordering = anOrdering;
  }
  
  protected void setProductId(ProductId aProductId) {
    this.assertArgumentNotNull(aProductId, "The product id must be provided.");
    
    this.productId = aProductId;
  }
  
  protected void setTenantId(TenantId aTenantId) {
    this.assertArgumentNotNull(aTenantId, "The tenant id must be provided.");
    
    this.tenantId = aTenantId;
  }
  
  protected void reorderFrom(BacklogItemId anId, int anOrdering) {
    if (this.backlogItemId().equals(anId)) {
      this.setOrdering(anOrdering);
    } else if (this.ordering() >= anOrdering) {
      this.setOrdering(this.ordering() + 1);
    }
  }
}
