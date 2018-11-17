package top.zhacker.ddd.agilepm.application.product.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午4:01
 */
@Data
@Accessors(chain = true)
public class InitiateDiscussionCommand {
  private String tenantId;
  private String productId;
  private String discussionId;
}
