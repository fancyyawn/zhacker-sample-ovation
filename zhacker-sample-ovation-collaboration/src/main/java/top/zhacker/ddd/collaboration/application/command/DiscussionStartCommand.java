package top.zhacker.ddd.collaboration.application.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/7/14 下午7:00
 */
@Data
@Accessors(chain = true)
public class DiscussionStartCommand {
  private String tenantId;
  private String forumId;
  private String authorId;
  private String subject;
}
