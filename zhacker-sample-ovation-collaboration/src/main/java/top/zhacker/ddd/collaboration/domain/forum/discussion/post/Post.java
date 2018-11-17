package top.zhacker.ddd.collaboration.domain.forum.discussion.post;

import lombok.*;
import top.zhacker.core.model.DomainEvent;
import top.zhacker.core.model.EventSourcedRootEntity;
import top.zhacker.ddd.collaboration.domain.collaborator.Author;
import top.zhacker.ddd.collaboration.domain.forum.ForumId;
import top.zhacker.ddd.collaboration.domain.forum.discussion.DiscussionId;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.event.PostContentAltered;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.event.PostedToDiscussion;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;

import java.util.Date;
import java.util.List;


/**
 * Created by zhacker.
 * Time 2018/7/14 上午8:19
 */
@Setter(AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Post extends EventSourcedRootEntity {
  private Author author;
  private String bodyText;
  private Date changedOn;
  private Date createdOn;
  private DiscussionId discussionId;
  private ForumId forumId;
  private PostId postId;
  private PostId replyToPostId;
  private String subject;
  private Tenant tenant;
  
  public Post(List<DomainEvent> anEventStream, int aStreamVersion) {
    super(anEventStream, aStreamVersion);
  }
  
  
  public Post(
      Tenant aTenant,
      ForumId aForumId,
      DiscussionId aDiscussionId,
      PostId aPostId,
      Author anAuthor,
      String aSubject,
      String aBodyText) {
    
    this(aTenant, aForumId, aDiscussionId, null, aPostId, anAuthor, aSubject, aBodyText);
  }
  
  public Post(
      Tenant aTenant,
      ForumId aForumId,
      DiscussionId aDiscussionId,
      PostId aReplyToPost,
      PostId aPostId,
      Author anAuthor,
      String aSubject,
      String aBodyText) {
    
    this.assertArgumentNotNull(anAuthor, "The author must be provided.");
    this.assertArgumentNotEmpty(aBodyText, "The body text must be provided.");
    this.assertArgumentNotNull(aDiscussionId, "The discussion id must be provided.");
    this.assertArgumentNotNull(aForumId, "The forum id must be provided.");
    this.assertArgumentNotNull(aPostId, "The post id must be provided.");
    this.assertArgumentNotEmpty(aSubject, "The subject must be provided.");
    this.assertArgumentNotNull(aTenant, "The tenant must be provided.");
    
    this.apply(new PostedToDiscussion(aTenant, aForumId, aDiscussionId,
        aReplyToPost, aPostId, anAuthor, aSubject, aBodyText));
  }
  
  protected Post() {
    super();
  }
  
  public void alterPostContent(String aSubject, String aBodyText) {
    
    this.assertArgumentNotEmpty(aSubject, "The subject must be provided.");
    this.assertArgumentNotEmpty(aBodyText, "The body text must be provided.");
    
    this.apply(new PostContentAltered(this.getTenant(), this.getForumId(), this.getDiscussionId(),
        this.getPostId(), aSubject, aBodyText));
  }
  
  
  protected void when(PostContentAltered anEvent) {
    this.setBodyText(anEvent.bodyText());
    this.setChangedOn(anEvent.getOccurredOn());
    this.setSubject(anEvent.subject());
  }
  
  protected void when(PostedToDiscussion anEvent) {
    this.setAuthor(anEvent.author());
    this.setBodyText(anEvent.bodyText());
    this.setChangedOn(anEvent.getOccurredOn());
    this.setCreatedOn(anEvent.getOccurredOn());
    this.setDiscussionId(anEvent.discussionId());
    this.setForumId(anEvent.forumId());
    this.setPostId(anEvent.postId());
    this.setReplyToPostId(anEvent.replyToPost());
    this.setSubject(anEvent.subject());
    this.setTenant(anEvent.tenant());
  }
  
  
}
