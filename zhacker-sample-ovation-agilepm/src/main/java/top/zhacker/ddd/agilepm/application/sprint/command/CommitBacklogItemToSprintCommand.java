package top.zhacker.ddd.agilepm.application.sprint.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午9:40
 */
@Data
@Accessors(chain = true)
public class CommitBacklogItemToSprintCommand {
  private String backlogItemId;
  private String sprintId;
  private String tenantId;
}
