package top.zhacker.ddd.agilepm.application.backlogitem.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午1:15
 */
@Data
@Accessors(chain = true)
public class CommitToSprintCommand {
  private String tenantId;
  private String sprintId;
  private String backlogItemId;
}
