package top.zhacker.ddd.identity.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import jdk.nashorn.internal.objects.annotations.Getter;
import top.zhacker.ddd.identity.api.vo.UserVO;
import top.zhacker.ddd.identity.application.AccessApplicationService;
import top.zhacker.ddd.identity.application.IdentityApplicationService;
import top.zhacker.ddd.identity.application.command.AuthenticateUserCommand;
import top.zhacker.ddd.identity.application.command.RegisterUserCommand;
import top.zhacker.ddd.identity.domain.user.User;
import top.zhacker.ddd.identity.domain.user.UserDescriptor;


/**
 * Created by zhacker.
 * Time 2018/7/6 下午10:30
 */
@RestController
@RequestMapping("/v1/users")
public class UserApi {
  
  @Autowired
  private IdentityApplicationService identityApplicationService;
  
  @Autowired
  private AccessApplicationService accessApplicationService;
  
  @PostMapping("register")
  public User registerUser(@RequestBody RegisterUserCommand aCommand) {
    return identityApplicationService.registerUser(aCommand);
  }
  
  @PostMapping("authenticate")
  public UserDescriptor authenticateUser(@RequestBody AuthenticateUserCommand aCommand){
    return identityApplicationService.authenticateUser(aCommand);
  }
  
  @GetMapping("/list")
  public Collection<User> users(String tenantId){
    return identityApplicationService.users(tenantId);
  }
  
  @GetMapping("/in-role")
  public UserVO userInRole(String tenantId, String username, String role){
    return UserVO.from(accessApplicationService.userInRole(tenantId, username, role)).setRole(role);
  }
  
}
