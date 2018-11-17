package top.zhacker.ddd.agilepm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zhacker.boot.aop.log.ParamLog;
import top.zhacker.ddd.agilepm.application.product.ProductApplicationService;
import top.zhacker.ddd.agilepm.application.product.command.NewProductCommand;
import top.zhacker.ddd.agilepm.application.product.command.PlanBacklogItemCommand;
import top.zhacker.ddd.agilepm.application.product.command.ScheduleReleaseCommand;
import top.zhacker.ddd.agilepm.application.product.command.ScheduleSprintCommand;
import top.zhacker.ddd.agilepm.domain.product.Product;
import top.zhacker.ddd.agilepm.domain.product.ProductId;
import top.zhacker.ddd.agilepm.domain.product.ProductRepository;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;

import java.util.Collection;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午12:15
 */
@RestController
@RequestMapping("/v1/product")
@ParamLog
public class ProductApi {
  
  @Autowired
  private ProductApplicationService productApplicationService;
  
  @Autowired
  private ProductRepository productRepository;
  
  @PostMapping("/create")
  public String newProduct(@RequestBody NewProductCommand command) {
    return productApplicationService.newProductWithDiscussion(command);
  }
  
  @PostMapping("/schedule-release")
  public String scheduleRelease(@RequestBody ScheduleReleaseCommand command){
    return productApplicationService.scheduleRelease(command);
  }
  
  @PostMapping("/schedule-sprint")
  public String scheduleSprint(@RequestBody ScheduleSprintCommand command){
    return productApplicationService.scheduleSprint(command);
  }
  
  @PostMapping("/plan-backlog-item")
  public String planBacklogItem(@RequestBody PlanBacklogItemCommand command){
    return productApplicationService.planBacklogItem(command);
  }
  
  @GetMapping("/detail")
  public Product productOfId(String tenantId, String productId){
    return productRepository.productOfId(new TenantId(tenantId), new ProductId(productId));
  }
  
  @GetMapping("/list")
  public Collection<Product> allProducts(String tenantId){
    return productRepository.allProductsOfTenant(new TenantId(tenantId));
  }
}
