package top.zhacker.ddd.collaboration.application.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/7/14 下午4:34
 */
@Data
@Accessors(chain = true)
public class PostCreateCommand {
  String tenantId;
  String discussionId;
  String replyToPostId;
  String authorId;
  String subject;
  String bodyText;
}
