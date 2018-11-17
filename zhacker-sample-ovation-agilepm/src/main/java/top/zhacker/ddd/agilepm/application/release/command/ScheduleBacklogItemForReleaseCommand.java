package top.zhacker.ddd.agilepm.application.release.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午3:05
 */
@Data
@Accessors(chain = true)
public class ScheduleBacklogItemForReleaseCommand {
  private String backlogItemId;
  private String releaseId;
  private String tenantId;
}
