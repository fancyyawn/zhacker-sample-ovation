package top.zhacker.ddd.agilepm.application.backlogitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhacker.ddd.agilepm.application.backlogitem.command.CommitToSprintCommand;
import top.zhacker.ddd.agilepm.application.backlogitem.command.ScheduleForReleaseCommand;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItem;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemId;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemRepository;
import top.zhacker.ddd.agilepm.domain.product.release.Release;
import top.zhacker.ddd.agilepm.domain.product.release.ReleaseId;
import top.zhacker.ddd.agilepm.domain.product.release.ReleaseRepository;
import top.zhacker.ddd.agilepm.domain.product.sprint.Sprint;
import top.zhacker.ddd.agilepm.domain.product.sprint.SprintId;
import top.zhacker.ddd.agilepm.domain.product.sprint.SprintRepository;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;
import top.zhacker.boot.leveldb.LevelDbTransactional;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午1:13
 */
@Service
public class BacklogItemApplicationService {
  
  @Autowired
  private BacklogItemRepository backlogItemRepository;
  
  @Autowired
  private SprintRepository sprintRepository;
  
  @Autowired
  private ReleaseRepository releaseRepository;
  
  @LevelDbTransactional
  public void commitToSprint(CommitToSprintCommand command){
    
    BacklogItem item = backlogItemRepository.backlogItemOfId(new TenantId(command.getTenantId()), new BacklogItemId(command.getBacklogItemId()));
  
    Sprint sprint = sprintRepository.sprintOfId(new TenantId(command.getTenantId()), new SprintId(command.getSprintId()));
    
    item.commitTo(sprint);
    
    backlogItemRepository.save(item);
  }
  
  @LevelDbTransactional
  public void scheduleForRelease(ScheduleForReleaseCommand command) {
    BacklogItem item = backlogItemRepository.backlogItemOfId(new TenantId(command.getTenantId()), new BacklogItemId(command.getBacklogItemId()));
  
    Release release = releaseRepository.releaseOfId(new TenantId(command.getTenantId()), new ReleaseId(command.getReleaseId()));
    
    item.scheduleFor(release);
    
    backlogItemRepository.save(item);
  }
}
