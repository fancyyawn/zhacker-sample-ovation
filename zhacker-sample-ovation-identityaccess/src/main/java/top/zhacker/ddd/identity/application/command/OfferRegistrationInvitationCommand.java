package top.zhacker.ddd.identity.application.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午10:26
 */
@Data
@Accessors(chain = true)
public class OfferRegistrationInvitationCommand {
  private String tenantId;
  private String description;
}
