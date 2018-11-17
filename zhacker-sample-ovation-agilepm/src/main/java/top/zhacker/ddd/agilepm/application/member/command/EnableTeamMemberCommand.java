package top.zhacker.ddd.agilepm.application.member.command;

import java.util.Date;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午3:44
 */
public class EnableTeamMemberCommand extends EnableMemberCommand {
  
  public EnableTeamMemberCommand(String tenantId, String username, String firstName, String lastName, String emailAddress, Date occurredOn) {
    super(tenantId, username, firstName, lastName, emailAddress, occurredOn);
  }
  
  public EnableTeamMemberCommand() {
  }
}
