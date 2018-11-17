package top.zhacker.ddd.identity.domain.user.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import top.zhacker.core.model.BaseDomainEvent;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午9:12
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserPasswordChanged extends BaseDomainEvent {
  private TenantId tenantId;
  private String username;
}
