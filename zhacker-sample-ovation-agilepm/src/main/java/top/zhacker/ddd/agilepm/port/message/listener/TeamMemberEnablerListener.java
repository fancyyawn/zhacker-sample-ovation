package top.zhacker.ddd.agilepm.port.message.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import top.zhacker.ddd.agilepm.application.member.MemberApplicationService;
import top.zhacker.ddd.agilepm.application.member.command.EnableProductOwnerCommand;
import top.zhacker.ddd.agilepm.application.member.command.EnableTeamMemberCommand;
import top.zhacker.ddd.agilepm.port.message.dto.UserAssignedToRole;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午3:58
 */
@Component
public class TeamMemberEnablerListener {
  
  @Autowired
  private MemberApplicationService memberApplicationService;
  
  @StreamListener(ListeningChannels.UserAssignedToRole)
  public void process(UserAssignedToRole event){
    
    if (event.getRoleName().equals("ScrumProductOwner")) {
      this.memberApplicationService.enableProductOwner(
          new EnableProductOwnerCommand(
              event.getTenantId().getId(),
              event.getUsername(),
              event.getFirstName(),
              event.getLastName(),
              event.getEmailAddress(),
              event.getOccurredOn()));
    } else if(event.getRoleName().equals("ScrumTeamMember")){
      this.memberApplicationService.enableTeamMember(
          new EnableTeamMemberCommand(
              event.getTenantId().getId(),
              event.getUsername(),
              event.getFirstName(),
              event.getLastName(),
              event.getEmailAddress(),
              event.getOccurredOn()));
    }
  }
}
