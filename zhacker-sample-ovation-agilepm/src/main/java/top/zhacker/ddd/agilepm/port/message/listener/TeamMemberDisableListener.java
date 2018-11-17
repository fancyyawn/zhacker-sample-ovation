package top.zhacker.ddd.agilepm.port.message.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import top.zhacker.ddd.agilepm.application.member.MemberApplicationService;
import top.zhacker.ddd.agilepm.application.member.command.DisableProductOwnerCommand;
import top.zhacker.ddd.agilepm.application.member.command.DisableTeamMemberCommand;
import top.zhacker.ddd.agilepm.port.message.dto.UserUnassignedFromRole;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午3:57
 */
@Component
public class TeamMemberDisableListener {
  
  @Autowired
  private MemberApplicationService memberApplicationService;
  
  @StreamListener(ListeningChannels.UserUnassignedFromRole)
  public void process(UserUnassignedFromRole event){
    if(event.getRoleName().equals("ScrumProductOwner")){
      memberApplicationService.disableProductOwner(
          (DisableProductOwnerCommand)
          new DisableProductOwnerCommand()
          .setTenantId(event.getTenantId().getId())
          .setUsername(event.getUsername())
          .setOccurredOn(event.getOccurredOn())
      );
    }else if(event.getRoleName().equals("ScrumTeamMember")){
      memberApplicationService.disableTeamMember(
          (DisableTeamMemberCommand)
              new DisableTeamMemberCommand()
                  .setTenantId(event.getTenantId().getId())
                  .setUsername(event.getUsername())
                  .setOccurredOn(event.getOccurredOn())
      );
    }
  }
}
