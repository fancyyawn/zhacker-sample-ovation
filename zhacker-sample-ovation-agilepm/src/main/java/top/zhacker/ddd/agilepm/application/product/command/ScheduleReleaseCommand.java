package top.zhacker.ddd.agilepm.application.product.command;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午12:25
 */
@Data
@Accessors(chain = true)
public class ScheduleReleaseCommand {
  private String tenantId;
  private String productId;
  String name;
  String description;
  Date begins;
  Date ends;
}
