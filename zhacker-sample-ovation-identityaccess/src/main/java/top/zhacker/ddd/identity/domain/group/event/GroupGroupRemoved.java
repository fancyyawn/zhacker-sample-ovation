package top.zhacker.ddd.identity.domain.group.event;

import lombok.Getter;
import top.zhacker.core.model.BaseDomainEvent;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午5:12
 */
@Getter
public class GroupGroupRemoved extends BaseDomainEvent {
  
  private TenantId tenantId;
  private String groupName;
  private String nestedGroupName;
  
  
  public GroupGroupRemoved(TenantId tenantId, String groupName, String nestedGroupName) {
    this.tenantId = tenantId;
    this.groupName = groupName;
    this.nestedGroupName = nestedGroupName;
  }
}
