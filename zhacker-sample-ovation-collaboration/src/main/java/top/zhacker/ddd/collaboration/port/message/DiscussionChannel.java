package top.zhacker.ddd.collaboration.port.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;


/**
 * Created by zhacker.
 * Time 2018/7/15 上午10:24
 */
public interface DiscussionChannel {
  
  String DiscussionCreateRequest = "";
  
  @Input(DiscussionCreateRequest)
  SubscribableChannel discussionCreateRequest();
}
