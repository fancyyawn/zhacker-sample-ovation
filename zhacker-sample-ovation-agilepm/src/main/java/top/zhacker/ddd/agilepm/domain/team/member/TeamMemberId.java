package top.zhacker.ddd.agilepm.domain.team.member;

import lombok.Getter;
import top.zhacker.core.model.ValueObject;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午10:17
 */
@Getter
public class TeamMemberId extends ValueObject {
  
  private TenantId tenantId;
  private String id;
  
  
  public TeamMemberId(TenantId tenantId, String id) {
    this.tenantId = tenantId;
    this.id = id;
  }
}
