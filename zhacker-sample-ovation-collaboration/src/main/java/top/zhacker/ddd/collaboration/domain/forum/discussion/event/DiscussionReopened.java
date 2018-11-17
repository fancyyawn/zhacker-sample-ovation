package top.zhacker.ddd.collaboration.domain.forum.discussion.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.zhacker.core.model.BaseDomainEvent;
import top.zhacker.ddd.collaboration.domain.forum.ForumId;
import top.zhacker.ddd.collaboration.domain.forum.discussion.DiscussionId;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;


/**
 * Created by zhacker.
 * Time 2018/7/14 上午8:14
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DiscussionReopened extends BaseDomainEvent {
  private Tenant tenant;
  private ForumId forumId;
  private DiscussionId discussionId;
  private String exclusiveOwner;
}
