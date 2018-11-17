package top.zhacker.ddd.identity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.zhacker.boot.event.notification.publisher.CloudNotificationPublisher;
import top.zhacker.boot.event.notification.publisher.NotificationPublisher;

/**
 * Created by zhacker.
 * Time 2018/11/12 下午1:32
 */
@Slf4j
@Configuration
@EnableDiscoveryClient
@EnableAspectJAutoProxy
@EnableScheduling
public class IdentityConfig implements CommandLineRunner {

    @Override
    public void run(String... strings) throws Exception {
//        Config config = ConfigService.getAppConfig(); //config instance is singleton for each namespace and is never null
//        config.addChangeListener(new ConfigChangeListener() {
//            @Override
//            public void onChange(ConfigChangeEvent changeEvent) {
//                log.info("Changes for namespace " + changeEvent.getNamespace());
//                for (String key : changeEvent.changedKeys()) {
//                    ConfigChange change = changeEvent.getChange(key);
//                    log.info(String.format("Found change - key: %s, oldValue: %s, newValue: %s, changeType: %s", change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType()));
//                }
//            }
//        });
    }


    @Bean
    public NotificationPublisher notificationPublisher(){
        return new CloudNotificationPublisher();
    }

}
