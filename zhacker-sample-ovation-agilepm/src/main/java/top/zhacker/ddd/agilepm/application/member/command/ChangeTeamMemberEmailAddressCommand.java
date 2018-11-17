package top.zhacker.ddd.agilepm.application.member.command;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午3:47
 */
@Data
@Accessors(chain = true)
public class ChangeTeamMemberEmailAddressCommand {
  private String tenantId;
  private String username;
  private String emailAddress;
  private Date occurredOn;
}
