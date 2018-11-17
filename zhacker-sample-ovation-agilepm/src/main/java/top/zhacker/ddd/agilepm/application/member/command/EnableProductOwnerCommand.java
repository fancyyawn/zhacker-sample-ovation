package top.zhacker.ddd.agilepm.application.member.command;

import java.util.Date;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午3:44
 */
public class EnableProductOwnerCommand extends EnableMemberCommand {
  
  public EnableProductOwnerCommand(String tenantId, String username, String firstName, String lastName, String emailAddress, Date occurredOn) {
    super(tenantId, username, firstName, lastName, emailAddress, occurredOn);
  }
  
  public EnableProductOwnerCommand() {
  
  }
}
