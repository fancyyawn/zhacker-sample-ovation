package top.zhacker.ddd.agilepm.application.team.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/6/20 下午9:05
 */
@Data
@Accessors(chain = true)
public class ProvisionTeamCommand {
  private String tenantId;
  private String name;
}
