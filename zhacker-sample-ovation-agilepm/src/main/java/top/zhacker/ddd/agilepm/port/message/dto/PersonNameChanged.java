package top.zhacker.ddd.agilepm.port.message.dto;

import lombok.*;
import top.zhacker.core.model.BaseDomainEvent;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午9:58
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class PersonNameChanged extends BaseDomainEvent {
  private TenantId tenantId;
  private String username;
  private FullName name;
  
  @Data
  public static class FullName {
    private String firstName;
    private String lastName;
  }
}
