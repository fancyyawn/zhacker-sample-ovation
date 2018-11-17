package top.zhacker.ddd.agilepm.application.backlogitem.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午2:56
 */
@Data
@Accessors(chain = true)
public class ScheduleForReleaseCommand {
  private String tenantId;
  private String releaseId;
  private String backlogItemId;
}
