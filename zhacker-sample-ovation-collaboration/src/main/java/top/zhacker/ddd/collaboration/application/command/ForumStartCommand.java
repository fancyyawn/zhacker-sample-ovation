package top.zhacker.ddd.collaboration.application.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/7/8 下午3:54
 */
@Data
@Accessors(chain = true)
public class ForumStartCommand {
  String tenantId;
  String creatorId;
  String moderatorId;
  String subject;
  String description;
}
