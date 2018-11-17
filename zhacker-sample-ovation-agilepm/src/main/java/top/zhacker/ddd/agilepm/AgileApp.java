package top.zhacker.ddd.agilepm;

//import com.dianping.cat.servlet.CatFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.zhacker.boot.aop.log.ParamLogAspect;
import top.zhacker.boot.event.notification.publisher.CloudNotificationPublisher;
import top.zhacker.boot.event.notification.publisher.NotificationPublisher;
import top.zhacker.ddd.agilepm.port.message.listener.ListeningChannels;


/**
 * Created by zhacker.
 * Time 2018/6/13 上午9:59
 */
@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
@EnableBinding(ListeningChannels.class)
@EnableDiscoveryClient
public class AgileApp {
  
  @Autowired
  private NotificationPublisher notificationPublisher;
  
  @Bean
  public ParamLogAspect paramLogAspect(){
    return new ParamLogAspect();
  }
  
  public static void main(String[] args){
      SpringApplication.run(AgileApp.class, args);
  }
  
  @Scheduled(cron = "*/2 * * * * *")
  public void publish() {
    notificationPublisher.publishNotifications();
  }

  @Bean
  public NotificationPublisher cloudNotificationPublisher(){
    return new CloudNotificationPublisher();
  }
  
  
//  @Bean
//  public FilterRegistrationBean catFilter() {
//    FilterRegistrationBean registration = new FilterRegistrationBean();
//    CatFilter filter = new CatFilter();
//    registration.setFilter(filter);
//    registration.addUrlPatterns("/*");
//    //   registration.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
//    registration.setName("cat-filter");
//    registration.setOrder(2);
//    return registration;
//  }
  
}
