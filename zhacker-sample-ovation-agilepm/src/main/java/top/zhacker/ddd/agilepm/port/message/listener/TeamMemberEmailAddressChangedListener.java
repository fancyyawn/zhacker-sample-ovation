package top.zhacker.ddd.agilepm.port.message.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import top.zhacker.ddd.agilepm.application.member.MemberApplicationService;
import top.zhacker.ddd.agilepm.application.member.command.ChangeTeamMemberEmailAddressCommand;
import top.zhacker.ddd.agilepm.port.message.dto.PersonContactInformationChanged;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午3:58
 */
@Component
public class TeamMemberEmailAddressChangedListener {
  
  @Autowired
  private MemberApplicationService memberApplicationService;
  
  @StreamListener(ListeningChannels.PersonContactInformationChanged)
  public void process(PersonContactInformationChanged event){
    memberApplicationService.changeTeamMemberEmailAddress(new ChangeTeamMemberEmailAddressCommand()
        .setTenantId(event.getTenantId().getId())
        .setUsername(event.getUsername())
        .setEmailAddress(event.getContactInformation().getEmailAddress().getAddress())
    );
  }
}
