package top.zhacker.ddd.agilepm.port.message.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import top.zhacker.ddd.agilepm.application.release.ReleaseApplicationService;
import top.zhacker.ddd.agilepm.application.release.command.ScheduleBacklogItemForReleaseCommand;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.event.BacklogItemScheduled;


/**
 * Created by zhacker.
 * Time 2018/7/15 上午11:19
 */
@Component
public class BacklogItemScheduledListener {
  
  @Autowired
  private ReleaseApplicationService releaseApplicationService;
  
  @StreamListener(ListeningChannels.BacklogItemScheduled)
  public void process(BacklogItemScheduled event){
    
    releaseApplicationService.scheduleBacklogItem(
        new ScheduleBacklogItemForReleaseCommand()
        .setTenantId(event.tenantId().getId())
        .setReleaseId(event.scheduledForReleaseId().id())
        .setBacklogItemId(event.backlogItemId().id())
    );
  }
}
