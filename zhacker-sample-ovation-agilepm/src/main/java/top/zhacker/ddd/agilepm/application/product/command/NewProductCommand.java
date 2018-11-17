package top.zhacker.ddd.agilepm.application.product.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午4:00
 */
@Data
@Accessors(chain = true)
public class NewProductCommand {
  private String tenantId;
  private String productOwnerId;
  private String name;
  private String description;
}
