package top.zhacker.ddd.agilepm.application.release;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhacker.ddd.agilepm.application.release.command.ScheduleBacklogItemForReleaseCommand;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItem;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemId;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemRepository;
import top.zhacker.ddd.agilepm.domain.product.release.Release;
import top.zhacker.ddd.agilepm.domain.product.release.ReleaseId;
import top.zhacker.ddd.agilepm.domain.product.release.ReleaseRepository;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;
import top.zhacker.boot.leveldb.LevelDbTransactional;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午3:03
 */
@Service
public class ReleaseApplicationService {
  
  @Autowired
  private ReleaseRepository releaseRepository;
  
  @Autowired
  private BacklogItemRepository backlogItemRepository;
  
  @LevelDbTransactional
  public void scheduleBacklogItem(ScheduleBacklogItemForReleaseCommand command){
    Release release = releaseRepository.releaseOfId(new TenantId(command.getTenantId()), new ReleaseId(command.getReleaseId()));
    
    BacklogItem item = backlogItemRepository.backlogItemOfId(
        new TenantId(command.getTenantId()), new BacklogItemId(command.getBacklogItemId())
    );
    
    release.schedule(item);
    
    releaseRepository.save(release);
  }
}
