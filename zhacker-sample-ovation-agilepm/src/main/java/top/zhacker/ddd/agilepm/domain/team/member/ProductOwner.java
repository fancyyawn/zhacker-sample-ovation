package top.zhacker.ddd.agilepm.domain.team.member;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;

import java.util.Date;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午10:16
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class ProductOwner extends Member {
  
  protected ProductOwner() {
    super();
  }
  
  public ProductOwner(TenantId aTenantId,
      String aUsername,
      String aFirstName,
      String aLastName,
      String anEmailAddress,
      Date anInitializedOn) {
    super(aTenantId, aUsername, aFirstName, aLastName, anEmailAddress, anInitializedOn);
  }
  
  public ProductOwnerId getProductOwnerId() {
    return new ProductOwnerId(this.getTenantId(), this.getUsername());
  }
  
  
}
