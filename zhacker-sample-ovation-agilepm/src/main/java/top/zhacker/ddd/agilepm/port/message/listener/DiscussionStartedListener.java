package top.zhacker.ddd.agilepm.port.message.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zhacker.ddd.agilepm.application.product.ProductApplicationService;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午9:09
 */
@Component
public class DiscussionStartedListener {
  
  @Autowired
  private ProductApplicationService productApplicationService;
  
  public void process(){
    productApplicationService.initiateDiscussion(null);
  }
}
