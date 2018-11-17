package top.zhacker.ddd.identity.domain.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import top.zhacker.core.model.AssertionConcern;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午9:15
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@ToString
public class UserDescriptor extends AssertionConcern {
  
  private TenantId tenantId;
  private String username;
  private String emailAddress;
  
  public static UserDescriptor nullDescriptorInstance() {
    return new UserDescriptor();
  }
  
}
