package top.zhacker.ddd.collaboration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


/**
 * Created by zhacker.
 * Time 2018/6/13 上午9:59
 */
@EnableFeignClients
@EnableAspectJAutoProxy
@SpringBootApplication
@EnableDiscoveryClient
public class CollaborationApp {
  
  public static void main(String[] args){
      SpringApplication.run(CollaborationApp.class, args);
  }
}
