package top.zhacker.ddd.identity.domain.group.event;

import lombok.Getter;
import top.zhacker.core.model.BaseDomainEvent;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午5:06
 */
@Getter
public class GroupGroupAdded extends BaseDomainEvent {
  
  private TenantId tenantId;
  private String groupName;
  private String nestedGroupName;
  
  public GroupGroupAdded(TenantId tenantId, String groupName, String nestedGroupName) {
    this.tenantId = tenantId;
    this.groupName = groupName;
    this.nestedGroupName = nestedGroupName;
  }
}
