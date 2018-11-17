package top.zhacker.ddd.agilepm.port.message.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import top.zhacker.ddd.agilepm.application.product.ProductApplicationService;
import top.zhacker.ddd.agilepm.domain.product.event.ProductBacklogItemPlanned;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午1:00
 */
@Component
public class ProductBacklogItemPlannedListener {
  
  @Autowired
  private ProductApplicationService productApplicationService;
  
  @StreamListener(ListeningChannels.ProductBacklogItemPlanned)
  public void process(ProductBacklogItemPlanned event){
    if(event.tenantId()==null){
      return;
    }
    productApplicationService.plannedProductBacklogItem(
        event.tenantId().getId(),
        event.productId().id(),
        event.backlogItemId().id()
    );
  }
}
