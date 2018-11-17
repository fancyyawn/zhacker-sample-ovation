package top.zhacker.ddd.identity.domain.tenant.event;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import top.zhacker.core.model.DomainEvent;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/11 下午7:06
 */
@Getter
@ToString
@EqualsAndHashCode
public class TenantDeactivated implements DomainEvent {
  
  private int eventVersion;
  private Date occurredOn;
  private TenantId tenantId;
  
  public TenantDeactivated(TenantId tenantId) {
    this.tenantId = tenantId;
    this.occurredOn = new Date();
    this.eventVersion = 1;
  }
}
