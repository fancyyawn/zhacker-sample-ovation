package top.zhacker.ddd.identity.api.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;
import top.zhacker.ddd.identity.domain.user.User;


/**
 * Created by zhacker.
 * Time 2018/7/13 下午11:45
 */
@Data
@Accessors(chain = true)
public class UserVO implements Serializable {
  private String emailAddress;
  private String firstName;
  private String lastName;
  private String role;
  private String tenantId;
  private String username;

  public static UserVO from(User user){
    if(user==null){
      return null;
    }
    return new UserVO()
        .setTenantId(user.getTenantId().getId())
        .setUsername(user.getUsername())
        .setFirstName(user.getPerson().getName().getFirstName())
        .setLastName(user.getPerson().getName().getLastName())
        .setEmailAddress(user.getPerson().emailAddress().getAddress());
  }

}
