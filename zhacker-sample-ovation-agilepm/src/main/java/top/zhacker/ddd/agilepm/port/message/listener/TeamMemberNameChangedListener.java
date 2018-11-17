package top.zhacker.ddd.agilepm.port.message.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import top.zhacker.ddd.agilepm.application.member.MemberApplicationService;
import top.zhacker.ddd.agilepm.application.member.command.ChangeTeamMemberNameCommand;
import top.zhacker.ddd.agilepm.port.message.dto.PersonNameChanged;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午3:58
 */
@Component
public class TeamMemberNameChangedListener {
  
  @Autowired
  private MemberApplicationService memberApplicationService;
  
  @StreamListener(ListeningChannels.PersonNameChanged)
  public void process(PersonNameChanged event){
    memberApplicationService.changeTeamMemberName(
        new ChangeTeamMemberNameCommand(
            event.getTenantId().getId(),
            event.getUsername(),
            event.getName().getFirstName(),
            event.getName().getLastName(),
            event.getOccurredOn())
    );
  }
}
