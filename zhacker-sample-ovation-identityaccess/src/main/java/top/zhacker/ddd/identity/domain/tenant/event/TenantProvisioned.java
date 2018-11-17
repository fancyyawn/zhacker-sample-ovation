package top.zhacker.ddd.identity.domain.tenant.event;

import java.util.Date;

import lombok.Getter;
import top.zhacker.core.model.DomainEvent;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/12 下午1:38
 */
@Getter
public class TenantProvisioned implements DomainEvent {
  
  private int eventVersion;
  private Date occurredOn;
  private TenantId tenantId;
  
  public TenantProvisioned(TenantId aTenantId) {
    super();
    
    this.eventVersion = 1;
    this.occurredOn = new Date();
    this.tenantId = aTenantId;
  }
}
