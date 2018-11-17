package top.zhacker.ddd.agilepm.application.member.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午3:43
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class EnableMemberCommand {
  private String tenantId;
  private String username;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private Date occurredOn;
  
  
}
