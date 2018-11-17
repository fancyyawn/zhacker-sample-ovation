package top.zhacker.ddd.agilepm.port.message.dto;

import lombok.*;
import top.zhacker.core.model.BaseDomainEvent;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午9:59
 */
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class PersonContactInformationChanged extends BaseDomainEvent {
  private TenantId tenantId;
  private String username;
  private ContactInformation contactInformation;
  
  @Data
  public static class ContactInformation{
    private EmailAddress emailAddress;
  }
  
  @Data
  public static class EmailAddress{
    private String address;
  }
}
