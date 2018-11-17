package top.zhacker.ddd.collaboration.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhacker.ddd.collaboration.application.command.*;
import top.zhacker.ddd.collaboration.application.vo.ForumCommandResult;
import top.zhacker.ddd.collaboration.domain.collaborator.Author;
import top.zhacker.ddd.collaboration.domain.collaborator.CollaboratorService;
import top.zhacker.ddd.collaboration.domain.collaborator.Creator;
import top.zhacker.ddd.collaboration.domain.collaborator.Moderator;
import top.zhacker.ddd.collaboration.domain.forum.Forum;
import top.zhacker.ddd.collaboration.domain.forum.ForumId;
import top.zhacker.ddd.collaboration.domain.forum.ForumRepository;
import top.zhacker.ddd.collaboration.domain.forum.discussion.Discussion;
import top.zhacker.ddd.collaboration.domain.forum.discussion.DiscussionId;
import top.zhacker.ddd.collaboration.domain.forum.discussion.DiscussionRepository;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;


/**
 * Created by zhacker.
 * Time 2018/7/8 上午11:06
 */
@Service
public class ForumApplicationService {
  
  @Autowired
  private ForumRepository forumRepository;
  
  @Autowired
  private DiscussionRepository discussionRepository;
  
  @Autowired
  private CollaboratorService collaboratorService;
  
  @Autowired
  private ForumQueryService forumQueryService;
  
  @Autowired
  private DiscussionQueryService discussionQueryService;
  
  public void startForum(
      ForumStartCommand command,
      ForumCommandResult aResult) {
    
    Forum forum =
        this.startNewForum(
            new Tenant(command.getTenantId()),
            command.getCreatorId(),
            command.getModeratorId(),
            command.getSubject(),
            command.getDescription(),
            null);
    
    if (aResult != null) {
      aResult.resultingForumId(forum.forumId().getId());
    }
  }
  
  private Forum startNewForum(
      Tenant aTenant,
      String aCreatorId,
      String aModeratorId,
      String aSubject,
      String aDescription,
      String anExclusiveOwner) {
    
    Creator creator =
        this.collaboratorService.creatorFrom(aTenant, aCreatorId);

    Moderator moderator =
        this.collaboratorService.moderatorFrom(aTenant, aModeratorId);
    
    Forum newForum =
        new Forum(
            aTenant,
            forumRepository.nextIdentity(),
            creator,
            moderator,
            aSubject,
            aDescription,
            anExclusiveOwner);
    
    forumRepository.save(newForum);
    
    return newForum;
  }
  
  public void startExclusiveForum(
      String aTenantId,
      String anExclusiveOwner,
      String aCreatorId,
      String aModeratorId,
      String aSubject,
      String aDescription,
      ForumCommandResult aResult) {
    
    Tenant tenant = new Tenant(aTenantId);
    
    String forumId =
        this.forumQueryService
            .forumIdOfExclusiveOwner(
                aTenantId,
                anExclusiveOwner);
    
    Forum forum = null;
    
    if (forumId != null) {
      forum = this.forumRepository()
          .forumOfId(
              tenant,
              new ForumId(forumId));
    }
    
    if (forum == null) {
      forum =
          this.startNewForum(
              tenant,
              aCreatorId,
              aModeratorId,
              aSubject,
              aDescription,
              anExclusiveOwner);
    }
    
    if (aResult != null) {
      aResult.resultingForumId(forum.forumId().getId());
    }
  }
  
  
  public void startExclusiveForumWithDiscussion(
      String aTenantId,
      String anExclusiveOwner,
      String aCreatorId,
      String aModeratorId,
      String anAuthorId,
      String aForumSubject,
      String aForumDescription,
      String aDiscussionSubject,
      ForumCommandResult aResult) {
    
    Tenant tenant = new Tenant(aTenantId);
    
    String forumId =
        this.forumQueryService
            .forumIdOfExclusiveOwner(
                aTenantId,
                anExclusiveOwner);
    
    Forum forum = null;
    
    if (forumId != null) {
      forum = this.forumRepository()
          .forumOfId(
              tenant,
              new ForumId(forumId));
    }
    
    if (forum == null) {
      forum = this.startNewForum(
          tenant,
          aCreatorId,
          aModeratorId,
          aForumSubject,
          aForumDescription,
          anExclusiveOwner);
    }
    
    String discussionId =
        this.discussionQueryService
            .discussionIdOfExclusiveOwner(
                aTenantId,
                anExclusiveOwner);
    
    Discussion discussion = null;
    
    if (discussionId != null) {
      discussion = this.discussionRepository
          .discussionOfId(
              tenant,
              new DiscussionId(discussionId));
    }
    
    if (discussion == null) {
      Author author =
          this.collaboratorService.authorFrom(tenant, anAuthorId);
      
      discussion =
          forum.startDiscussionFor(
              author,
              aDiscussionSubject,
              anExclusiveOwner);
      
      this.discussionRepository.save(discussion);
    }
    
    if (aResult != null) {
      aResult.resultingForumId(forum.forumId().getId());
      aResult.resultingDiscussionId(discussion.getDiscussionId().getId());
    }
  }
  
  public void startDiscussion(DiscussionStartCommand command){
    
    Tenant tenant = new Tenant(command.getTenantId());
    
    Forum forum = forumRepository.forumOfId(tenant, new ForumId(command.getForumId()));
    
    Author author = this.collaboratorService.authorFrom(tenant, command.getAuthorId());
    
    Discussion discussion = forum.startDiscussion(author, command.getSubject());
    
    this.discussionRepository.save(discussion);
  }
  
  private ForumRepository forumRepository(){
    return this.forumRepository;
  }
  
  public void closeForum(
      String aTenantId,
      String aForumId) {
    
    Tenant tenant = new Tenant(aTenantId);
    
    Forum forum =
        this.forumRepository()
            .forumOfId(
                tenant,
                new ForumId(aForumId));
    
    forum.close();
    
    this.forumRepository().save(forum);
  }
  
  public void reopenForum(
      String aTenantId,
      String aForumId) {
    
    Tenant tenant = new Tenant(aTenantId);
    
    Forum forum =
        this.forumRepository()
            .forumOfId(
                tenant,
                new ForumId(aForumId));
    
    forum.reopen();
    
    this.forumRepository().save(forum);
  }
  
  public void assignModeratorToForum(ForumAssignModeratorCommand command) {
    
    Tenant tenant = new Tenant(command.getTenantId());
    
    Forum forum =
        this.forumRepository()
            .forumOfId(
                tenant,
                new ForumId(command.getForumId()));
    
    Moderator moderator =
        this.collaboratorService.moderatorFrom(tenant, command.getModeratorId());
    
    forum.assignModerator(moderator);
    
    this.forumRepository().save(forum);
  }
  
  public void changeForumDescription(ForumChangeDescriptionCommand command) {
    
    Tenant tenant = new Tenant(command.getTenantId());
    
    Forum forum =
        this.forumRepository()
            .forumOfId(
                tenant,
                new ForumId(command.getForumId()));
    
    forum.changeDescription(command.getDescription());
    
    this.forumRepository().save(forum);
  }
  
  public void changeForumSubject(ForumChangeSubjectCommand command) {
    
    Tenant tenant = new Tenant(command.getTenantId());
    
    Forum forum =
        this.forumRepository()
            .forumOfId(
                tenant,
                new ForumId(command.getForumId()));
    
    forum.changeSubject(command.getSubject());
    
    this.forumRepository().save(forum);
  }
}
