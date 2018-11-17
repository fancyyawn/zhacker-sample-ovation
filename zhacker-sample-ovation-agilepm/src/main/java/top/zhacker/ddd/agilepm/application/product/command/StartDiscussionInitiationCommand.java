package top.zhacker.ddd.agilepm.application.product.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午4:03
 */
@Data
@Accessors(chain = true)
public class StartDiscussionInitiationCommand {
  private String tenantId;
  private String productId;
}
