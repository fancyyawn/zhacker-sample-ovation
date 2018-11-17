package top.zhacker.ddd.agilepm.port.message.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zhacker.ddd.agilepm.application.product.ProductApplicationService;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午9:13
 */
@Component
public class ProductDiscussionRetryListener {
  
  @Autowired
  private ProductApplicationService productApplicationService;
  
  public void process(){
    productApplicationService.timeOutProductDiscussionRequest(null);
    productApplicationService.retryProductDiscussionRequest(null);
  }
}
