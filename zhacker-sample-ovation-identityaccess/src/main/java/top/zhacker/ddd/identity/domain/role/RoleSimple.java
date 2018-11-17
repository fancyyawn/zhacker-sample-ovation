package top.zhacker.ddd.identity.domain.role;

import lombok.Data;
import lombok.experimental.Accessors;
import top.zhacker.ddd.identity.domain.group.Group;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/7/7 下午9:09
 */
@Data
@Accessors(chain = true)
public class RoleSimple {
  private TenantId tenantId;
  private String name;
  private String description;
  private boolean supportsNesting = true;
  private Group group;
}
