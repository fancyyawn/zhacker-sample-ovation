package top.zhacker.ddd.agilepm.application.product.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午4:02
 */
@Data
@Accessors(chain = true)
public class RequestProductDiscussionCommand {
  private String tenantId;
  private String productId;
}
