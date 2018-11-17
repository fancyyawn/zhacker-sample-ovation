package top.zhacker.ddd.identity.domain.user.person.event;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import top.zhacker.core.model.BaseDomainEvent;
import top.zhacker.ddd.identity.domain.tenant.TenantId;
import top.zhacker.ddd.identity.domain.user.person.ContactInformation;


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
}
