package top.zhacker.ddd.agilepm.application.product.command;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午12:27
 */
@Data
@Accessors(chain = true)
public class ScheduleSprintCommand {
  private String tenantId;
  private String productId;
  String name;
  String goals;
  Date begins;
  Date ends;
}
