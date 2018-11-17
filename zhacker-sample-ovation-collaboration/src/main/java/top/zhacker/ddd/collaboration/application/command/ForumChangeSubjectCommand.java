package top.zhacker.ddd.collaboration.application.command;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/7/14 下午6:00
 */
@Data
@Accessors(chain = true)
public class ForumChangeSubjectCommand {
  String tenantId;
  String forumId;
  String subject;
}
