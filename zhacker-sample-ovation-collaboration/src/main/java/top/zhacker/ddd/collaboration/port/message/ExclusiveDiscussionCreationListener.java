package top.zhacker.ddd.collaboration.port.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zhacker.ddd.collaboration.application.ForumApplicationService;


/**
 * Created by zhacker.
 * Time 2018/7/15 上午10:22
 */
@Component
//@EnableBinding(DiscussionChannel.class)
public class ExclusiveDiscussionCreationListener {
  
  @Autowired
  private ForumApplicationService forumApplicationService;
  
//  @StreamListener(DiscussionChannel.DiscussionCreateRequest)
  public void discussionCreateRequest(){
  
  }
  
}
