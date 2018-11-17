package top.zhacker.ddd.agilepm.port.message.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import top.zhacker.ddd.agilepm.application.sprint.SprintApplicationService;
import top.zhacker.ddd.agilepm.application.sprint.command.CommitBacklogItemToSprintCommand;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.event.BacklogItemCommitted;


/**
 * Created by zhacker.
 * Time 2018/7/15 上午11:19
 */
@Component
public class BacklogItemCommittedListener {
  
  @Autowired
  private SprintApplicationService sprintApplicationService;
  
  @StreamListener(ListeningChannels.BacklogItemCommitted)
  public void process(BacklogItemCommitted event){
    sprintApplicationService.commitBacklogItemToSprint(
        new CommitBacklogItemToSprintCommand()
        .setTenantId(event.tenantId().getId())
        .setSprintId(event.committedToSprintId().id())
        .setBacklogItemId(event.backlogItemId().id())
    );
  }
}
