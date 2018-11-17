package top.zhacker.ddd.identity.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import top.zhacker.boot.event.notification.NotificationLog;
import top.zhacker.boot.event.notification.publisher.NotificationPublisher;
import top.zhacker.ddd.identity.application.NotificationApplicationService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


/**
 * Created by zhacker.
 * Time 2018/6/16 下午9:47
 */
@Slf4j
@RestController
@RequestMapping("/v1/notifications")
public class NotificationApi {
  
  @Autowired
  private NotificationApplicationService notificationApplicationService;
  
  @Autowired
  private NotificationPublisher notificationPublisher;

  @Autowired
  private CuratorFramework curatorFramework;
  
  @GetMapping("current")
  public NotificationLog currentNotificationLog(){
    return notificationApplicationService.currentNotificationLog();
  }
  
  @GetMapping("/{notificationId}")
  public NotificationLog getById(@PathVariable("notificationId") String aNotificationId){
    return notificationApplicationService.notificationLog(aNotificationId);
  }

  private InterProcessMutex notificationPublishMutex;

  @PostConstruct
  public void init(){
    notificationPublishMutex = new InterProcessMutex(curatorFramework, "/ddd-identity/notice-publish");
  }

  @PostMapping("publish")
  @Transactional
  @Scheduled(cron = "*/2 * * * * *")
  public void publish() throws Exception {
    if (notificationPublishMutex.acquire(5, TimeUnit.SECONDS) ) {
      try {
        log.debug("publish event at {}", LocalDateTime.now());
        notificationPublisher.publishNotifications();
      }
      finally {
        notificationPublishMutex.release();
      }
    }
  }

}
