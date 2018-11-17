package top.zhacker.ddd.collaboration.port.client;

import lombok.Data;
import lombok.experimental.Accessors;
import top.zhacker.core.exception.BusinessException;
import top.zhacker.ddd.collaboration.domain.collaborator.Collaborator;

import java.io.Serializable;
import java.lang.reflect.Constructor;


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
  
  public <T extends Collaborator> T to(Class<T> targetClass){
    
    try {
      Constructor<T> ctor = targetClass.getConstructor(String.class, String.class, String.class);
  
      T collaborator = ctor.newInstance(username, (firstName + " " + lastName).trim(), emailAddress);
  
      return collaborator;
      
    }catch (Exception e){
      throw new BusinessException("user.convert.fail");
    }
  }
}
