package top.zhacker.ddd.agilepm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhacker.boot.aop.log.ParamLog;
import top.zhacker.ddd.agilepm.domain.product.ProductId;
import top.zhacker.ddd.agilepm.domain.product.release.Release;
import top.zhacker.ddd.agilepm.domain.product.release.ReleaseId;
import top.zhacker.ddd.agilepm.domain.product.release.ReleaseRepository;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;

import java.util.Collection;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午1:20
 */
@RestController
@RequestMapping("/v1/release")
@ParamLog
public class ReleaseApi {
  
  @Autowired
  private ReleaseRepository releaseRepository;
  
  @GetMapping("list")
  public Collection<Release> allProductReleases(String tenantId, String productId){
    return releaseRepository.allProductReleases(
        new TenantId(tenantId),new ProductId(productId)
    );
  }
  
  @GetMapping("detail")
  public Release releaseOfId(String tenantId, String releaseId){
    return releaseRepository.releaseOfId(
        new TenantId(tenantId), new ReleaseId(releaseId)
    );
  }
}
