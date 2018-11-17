package top.zhacker.ddd.agilepm.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zhacker.boot.aop.log.ParamLog;
import top.zhacker.ddd.agilepm.application.backlogitem.BacklogItemApplicationService;
import top.zhacker.ddd.agilepm.application.backlogitem.command.CommitToSprintCommand;
import top.zhacker.ddd.agilepm.application.backlogitem.command.ScheduleForReleaseCommand;
import top.zhacker.ddd.agilepm.domain.product.ProductId;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItem;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemId;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemRepository;
import top.zhacker.ddd.agilepm.domain.product.release.ReleaseId;
import top.zhacker.ddd.agilepm.domain.product.sprint.SprintId;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;

import java.util.Collection;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午12:22
 */
@RestController
@RequestMapping("/v1/backlog-item")
@ParamLog
public class BacklogItemApi {
  
  @Autowired
  private BacklogItemRepository backlogItemRepository;
  
  @Autowired
  private BacklogItemApplicationService backlogItemApplicationService;
  
  
  @PostMapping("/commit-to-sprint")
  public void commitToSprint(@RequestBody CommitToSprintCommand command){
    backlogItemApplicationService.commitToSprint(command);
  }
  
  @PostMapping("/schedule-for-release")
  public void scheduleForRelease(@RequestBody ScheduleForReleaseCommand command){
    backlogItemApplicationService.scheduleForRelease(command);
  }
  
  @GetMapping("/list/all")
  public Collection<BacklogItem> allProductBacklogItems(String tenantId, String productId){
    return backlogItemRepository.allProductBacklogItems(
        new TenantId(tenantId), new ProductId(productId)
    );
  }
  
  @GetMapping("/detail")
  public BacklogItem backlogItem(String tenantId, String backlogItemId){
    return backlogItemRepository.backlogItemOfId(
        new TenantId(tenantId), new BacklogItemId(backlogItemId)
    );
  }
  
  @GetMapping("/list/committed")
  public Collection<BacklogItem> allBacklogItemsComittedToSprint(String tenantId, String sprintId){
    return backlogItemRepository.allBacklogItemsComittedTo(
        new TenantId(tenantId),new SprintId(sprintId)
    );
  }
  
  
  @GetMapping("/list/scheduled")
  public Collection<BacklogItem> allBacklogItemsScheduledForRelease(String tenantId, String releaseId){
    return backlogItemRepository.allBacklogItemsScheduledFor(
        new TenantId(tenantId),new ReleaseId(releaseId)
    );
  }
}
