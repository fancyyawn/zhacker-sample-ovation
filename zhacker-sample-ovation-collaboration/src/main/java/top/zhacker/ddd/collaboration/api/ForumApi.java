package top.zhacker.ddd.collaboration.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zhacker.ddd.collaboration.application.ForumApplicationService;
import top.zhacker.ddd.collaboration.application.ForumQueryService;
import top.zhacker.ddd.collaboration.application.command.ForumAssignModeratorCommand;
import top.zhacker.ddd.collaboration.application.command.ForumChangeDescriptionCommand;
import top.zhacker.ddd.collaboration.application.command.ForumChangeSubjectCommand;
import top.zhacker.ddd.collaboration.application.command.ForumStartCommand;
import top.zhacker.ddd.collaboration.application.vo.ForumCommandResult;
import top.zhacker.ddd.collaboration.application.vo.ForumData;

import java.util.Collection;


/**
 * Created by zhacker.
 * Time 2018/7/8 上午11:12
 */
@RestController
@RequestMapping("/v1/forum")
@Slf4j
public class ForumApi {
  
  @Autowired
  private ForumApplicationService forumApplicationService;
  
  @Autowired
  private ForumQueryService forumQueryService;
  
  @PostMapping("/start")
  public void start(@RequestBody ForumStartCommand command){
    
    ForumCommandResult result = new ForumCommandResult() {
      @Override
      public void resultingForumId(String aForumId) {
        log.info("forumId = {}", aForumId);
      }
      @Override
      public void resultingDiscussionId(String aDiscussionId) {
        throw new UnsupportedOperationException("Should not be reached.");
      }
    };
  
    forumApplicationService.startForum(command, result);
  }
  
  @PostMapping("/close")
  public void closeForum(
      String tenantId,
      String forumId) {
    forumApplicationService.closeForum(tenantId, forumId);
  }
  
  @PostMapping("/reopen")
  public void  reopenForum(
      String tenantId,
      String forumId){
    forumApplicationService.reopenForum(tenantId,forumId);
  }
  
  @GetMapping("/all")
  public Collection<ForumData> allForumsDataOfTenant(String aTenantId){
    return forumQueryService.allForumsDataOfTenant(aTenantId);
  }
  
  @GetMapping("/detail")
  public ForumData forumDataOfId(String aTenantId, String aForumId){
    return forumQueryService.forumDataOfId(aTenantId, aForumId);
  }
  
  @PostMapping("/assign-moderator")
  public void assignModeratorToForum(@RequestBody ForumAssignModeratorCommand command) {
    forumApplicationService.assignModeratorToForum(command);
  }
  
  
  @PostMapping("change-description")
  public void changeForumDescription(@RequestBody ForumChangeDescriptionCommand command) {
    forumApplicationService.changeForumDescription(command);
  }
  
  @PostMapping("change-subject")
  public void changeForumSubject(@RequestBody ForumChangeSubjectCommand command) {
    forumApplicationService.changeForumSubject(command);
  }
  
}
