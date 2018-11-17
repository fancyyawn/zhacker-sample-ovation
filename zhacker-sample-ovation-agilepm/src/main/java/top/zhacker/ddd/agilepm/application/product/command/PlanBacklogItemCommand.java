package top.zhacker.ddd.agilepm.application.product.command;

import lombok.Data;
import lombok.experimental.Accessors;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemType;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.StoryPoints;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午12:25
 */
@Data
@Accessors(chain = true)
public class PlanBacklogItemCommand {
  private String tenantId;
  private String productId;
  String summary;
  String category;
  BacklogItemType type;
  StoryPoints storyPoints;
}
