package top.zhacker.ddd.agilepm.application.team.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/7/7 下午6:52
 */
@Data
@Accessors(chain = true)
public class AssignTeamMemberCommand {
  private String tenantId;
  private String teamName;
  private String username;
}
