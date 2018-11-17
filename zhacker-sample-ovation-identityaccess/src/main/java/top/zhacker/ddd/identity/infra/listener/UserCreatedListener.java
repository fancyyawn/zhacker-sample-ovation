package top.zhacker.ddd.identity.infra.listener;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


/**
 * Created by zhacker.
 * Time 2018/6/24 下午4:32
 */
@Component
@Slf4j
@EnableBinding(UserCreatedListener.NotificationSource.class)
public class UserCreatedListener {
  
  @StreamListener(NotificationSource.Notification)
  public void process(String notification){
    log.info("notification={}", notification);
  }
  
  @StreamListener(NotificationSource.TenantProvisioned)
  public void doWhenTenantProvisioned(String notification){
    log.info("tenantProvisioned = {}", notification);
  }
  
  
  public interface NotificationSource {
    
    String Notification = "topic.identity-access.notification";
    
    String TenantProvisioned = "top.zhacker.ddd.identity.domain.tenant.event.TenantProvisioned";
    
    @Input(Notification)
    SubscribableChannel notified();
    
    @Input(TenantProvisioned)
    SubscribableChannel tenantProvisioned();
  }
  
}
