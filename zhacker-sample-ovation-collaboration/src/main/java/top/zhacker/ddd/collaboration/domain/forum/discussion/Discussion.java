package top.zhacker.ddd.collaboration.domain.forum.discussion;

import lombok.*;
import top.zhacker.boot.registry.DomainRegistry;
import top.zhacker.core.model.DomainEvent;
import top.zhacker.core.model.EventSourcedRootEntity;
import top.zhacker.ddd.collaboration.domain.collaborator.Author;
import top.zhacker.ddd.collaboration.domain.forum.ForumId;
import top.zhacker.ddd.collaboration.domain.forum.discussion.event.DiscussionClosed;
import top.zhacker.ddd.collaboration.domain.forum.discussion.event.DiscussionReopened;
import top.zhacker.ddd.collaboration.domain.forum.discussion.event.DiscussionStarted;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.Post;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.PostId;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.PostRepository;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;

import java.util.List;


/**
 * Created by zhacker.
 * Time 2018/7/14 上午8:10
 */
@Setter(AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Discussion extends EventSourcedRootEntity {
  
  private Author author;
  private boolean closed;
  private DiscussionId discussionId;
  private String exclusiveOwner;
  private ForumId forumId;
  private String subject;
  private Tenant tenant;
  
  protected Discussion() {
    super();
  }
  
  public Discussion(List<DomainEvent> anEventStream, int aStreamVersion) {
    super(anEventStream, aStreamVersion);
  }
  
  public Discussion(
      Tenant aTenantId,
      ForumId aForumId,
      DiscussionId aDiscussionId,
      Author anAuthor,
      String aSubject,
      String anExclusiveOwner) {
    
    this();
    
    this.assertArgumentNotNull(anAuthor, "The author must be provided.");
    this.assertArgumentNotNull(aDiscussionId, "The discussion id must be provided.");
    this.assertArgumentNotNull(aForumId, "The forum id must be provided.");
    this.assertArgumentNotEmpty(aSubject, "The subject must be provided.");
    this.assertArgumentNotNull(aTenantId, "The tenant must be provided.");
    
    this.apply(new DiscussionStarted(aTenantId, aForumId, aDiscussionId,
        anAuthor, aSubject, anExclusiveOwner));
  }
  
  
  public void close() {
    if (this.isClosed()) {
      throw new IllegalStateException("This discussion is already closed.");
    }
    
    this.apply(new DiscussionClosed(this.getTenant(), this.getForumId(),
        this.getDiscussionId(), this.getExclusiveOwner()));
  }
  
  public boolean isClosed() {
    return this.closed;
  }
  
  public Post post(
      Author anAuthor,
      String aSubject,
      String aBodyText) {
    
    return this.post(null, anAuthor, aSubject, aBodyText);
  }
  
  public Post post(
      PostId aReplyToPost,
      Author anAuthor,
      String aSubject,
      String aBodyText) {
    
    Post post =
        new Post(
            this.getTenant(),
            this.getForumId(),
            this.getDiscussionId(),
            aReplyToPost,
            DomainRegistry.repo(PostRepository.class).nextIdentity(),
            anAuthor,
            aSubject,
            aBodyText);
    
    return post;
  }
  
  
  public void reopen() {
    if (!this.isClosed()) {
      throw new IllegalStateException("The discussion is not closed.");
    }
    
    this.apply(new DiscussionReopened(this.getTenant(), this.getForumId(),
        this.getDiscussionId(), this.getExclusiveOwner()));
  }
  
  
  protected void when(DiscussionClosed anEvent) {
    this.setClosed(true);
  }
  
  protected void when(DiscussionReopened anEvent) {
    this.setClosed(false);
  }
  
  protected void when(DiscussionStarted anEvent) {
    this.setAuthor(anEvent.getAuthor());
    this.setDiscussionId(anEvent.getDiscussionId());
    this.setExclusiveOwner(anEvent.getExclusiveOwner());
    this.setForumId(anEvent.getForumId());
    this.setSubject(anEvent.getSubject());
    this.setTenant(anEvent.getTenant());
  }
  
}
