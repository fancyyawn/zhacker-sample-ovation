package top.zhacker.ddd.identity.domain.tenant.event;

import java.util.Date;

import lombok.Getter;
//import top.zhacker.core.model.DomainEvent;
import top.zhacker.core.model.DomainEvent;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/11 下午7:05
 */
@Getter
public class TenantActivated implements DomainEvent {
  
  private int eventVersion;
  private Date occurredOn;
  private TenantId tenantId;
  
  public TenantActivated(TenantId aTenantId) {
    super();
    
    this.eventVersion = 1;
    this.occurredOn = new Date();
    this.tenantId = aTenantId;
  }
}
