package top.zhacker.ddd.collaboration.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zhacker.ddd.collaboration.application.DiscussionApplicationService;
import top.zhacker.ddd.collaboration.application.PostApplicationService;
import top.zhacker.ddd.collaboration.application.PostQueryService;
import top.zhacker.ddd.collaboration.application.command.PostCreateCommand;
import top.zhacker.ddd.collaboration.application.command.PostModerateCommand;
import top.zhacker.ddd.collaboration.application.vo.DiscussionCommandResult;
import top.zhacker.ddd.collaboration.application.vo.PostData;

import java.util.Collection;


/**
 * Created by zhacker.
 * Time 2018/7/14 下午4:17
 */
@RestController
@RequestMapping("/v1/posts")
public class PostApi {
  
  @Autowired
  private DiscussionApplicationService discussionApplicationService;
  
  @Autowired
  private PostApplicationService postApplicationService;
  
  @Autowired
  private PostQueryService postQueryService;
  
  @GetMapping("/all")
  public Collection<PostData> allPostsDataOfDiscussion(String aTenantId, String aDiscussionId){
    return postQueryService.allPostsDataOfDiscussion(aTenantId, aDiscussionId);
  }
  
  @GetMapping("/detail")
  public PostData postDataOfId(String aTenantId, String aPostId) {
    return postQueryService.postDataOfId(aTenantId,aPostId);
  }
  
  @PostMapping("/create")
  public void create(@RequestBody PostCreateCommand command){
    
    DiscussionCommandResult result = new DiscussionCommandResult() {
      @Override
      public void resultingDiscussionId(String aDiscussionId) {
    
      }
  
      @Override
      public void resultingPostId(String aPostId) {
    
      }
  
      @Override
      public void resultingInReplyToPostId(String aReplyToPostId) {
    
      }
    };
    
    discussionApplicationService.postToDiscussion(command, result);
  }
  
  @PostMapping("/modify")
  public void moderatePost(@RequestBody PostModerateCommand command) {
    postApplicationService.moderatePost(command);
  }
}
