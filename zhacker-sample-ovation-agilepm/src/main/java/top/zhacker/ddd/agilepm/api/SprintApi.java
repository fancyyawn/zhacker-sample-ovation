package top.zhacker.ddd.agilepm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhacker.boot.aop.log.ParamLog;
import top.zhacker.ddd.agilepm.domain.product.ProductId;
import top.zhacker.ddd.agilepm.domain.product.sprint.Sprint;
import top.zhacker.ddd.agilepm.domain.product.sprint.SprintId;
import top.zhacker.ddd.agilepm.domain.product.sprint.SprintRepository;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;

import java.util.Collection;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午1:20
 */
@ParamLog
@RestController
@RequestMapping("/v1/sprint")
public class SprintApi {
  
  @Autowired
  private SprintRepository sprintRepository;
  
  @GetMapping("list")
  public Collection<Sprint> allProductSprints(String tenantId, String productId){
    return sprintRepository.allProductSprints(new TenantId(tenantId), new ProductId(productId));
  }
  
  @GetMapping("detail")
  public Sprint sprintOfId(String tenantId, String sprintId){
    return sprintRepository.sprintOfId(
        new TenantId(tenantId),new SprintId(sprintId)
    );
  }
}
