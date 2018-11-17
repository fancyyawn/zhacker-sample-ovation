package top.zhacker.ddd.collaboration.application.command;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * Created by zhacker.
 * Time 2018/7/14 下午4:20
 */
@Data
@Accessors(chain = true)
public class PostModerateCommand implements Serializable {
  String tenantId;
  String forumId;
  String postId;
  String moderatorId;
  String subject;
  String bodyText;
}
