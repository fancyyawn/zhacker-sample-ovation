package top.zhacker.ddd.collaboration.port.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Created by zhacker.
 * Time 2018/7/13 下午11:42
 */
@FeignClient(name = Systems.Identity)
@RequestMapping("/v1/users")
public interface UserInRoleClient {

  @GetMapping("/in-role")
  UserVO userInRole(
          @RequestParam("tenantId") String tenantId,
          @RequestParam("username") String username,
          @RequestParam("role") String role);
}
