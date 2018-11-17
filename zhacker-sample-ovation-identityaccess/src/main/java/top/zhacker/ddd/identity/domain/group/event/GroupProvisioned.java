package top.zhacker.ddd.identity.domain.group.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.zhacker.core.model.BaseDomainEvent;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午9:03
 */
@Getter
@AllArgsConstructor
public class GroupProvisioned extends BaseDomainEvent {
  private TenantId tenantId;
  private String name;
}
