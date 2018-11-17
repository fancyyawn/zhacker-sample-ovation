/*
package top.zhacker.ddd.identity.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.zhacker.boot.event.notification.publisher.NotificationPublisher;


*/
/**
 * Created by zhacker.
 * Time 2018/8/2 下午1:20
 *//*

//@Job(name="NotificationPublishJob")
@Component
public class NotificationPublishJob implements SimpleJob {
  
  @Autowired
  private NotificationPublisher notificationPublisher;
  
  @Transactional
  @Override
  public void execute(ShardingContext shardingContext) {
    notificationPublisher.publishNotifications();
  }
}
*/
