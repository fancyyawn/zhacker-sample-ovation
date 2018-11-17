package top.zhacker.ddd.identity.domain.group.event;

import lombok.Getter;
import top.zhacker.core.model.BaseDomainEvent;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午5:22
 */
@Getter
public class GroupUserAdded extends BaseDomainEvent {
  
  private TenantId tenantId;
  private String groupName;
  private String username;
  
  public GroupUserAdded(TenantId tenantId, String groupName, String username) {
    this.tenantId = tenantId;
    this.groupName = groupName;
    this.username = username;
  }
}
