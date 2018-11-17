//   Copyright 2012,2013 Vaughn Vernon
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

package top.zhacker.ddd.collaboration.port.persistence.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import top.zhacker.boot.event.stream.DispatchableDomainEvent;
import top.zhacker.boot.event.stream.dispatch.AbstractProjection;
import top.zhacker.boot.event.stream.dispatch.EventStreamDispatcher;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.event.PostContentAltered;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.event.PostedToDiscussion;


@Repository
public class JdbcTemplatePostProjection extends AbstractProjection implements EventStreamDispatcher {

    private static final Class<?> understoodEventTypes[] = {
        PostContentAltered.class,
        PostedToDiscussion.class
    };

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplatePostProjection(@Qualifier("parentEventStreamDispatcher") EventStreamDispatcher aParentEventDispatcher) {
        super();

        aParentEventDispatcher.registerEventDispatcher(this);
    }

    @Override
    public void dispatch(DispatchableDomainEvent aDispatchableDomainEvent) {
        this.projectWhen(aDispatchableDomainEvent);
    }

    @Override
    public void registerEventDispatcher(EventStreamDispatcher anEventDispatcher) {
        throw new UnsupportedOperationException("Cannot register additional dispatchers.");
    }

    @Override
    public boolean understands(DispatchableDomainEvent aDispatchableDomainEvent) {
        return this.understandsAnyOf(
                aDispatchableDomainEvent.domainEvent().getClass(),
                understoodEventTypes);
    }

    protected void when(PostContentAltered anEvent) throws Exception {
        jdbcTemplate.update("update tbl_vw_post "
                + "set body_text=?, subject=?, changed_on=? "
                + "where tenant_id = ? and forum_id = ?",
                anEvent.bodyText(),
                anEvent.subject(),
                new java.sql.Date(anEvent.getOccurredOn().getTime()),
                anEvent.tenant().getId(),
                anEvent.postId().getId()
                );
    }

    protected void when(PostedToDiscussion anEvent) throws Exception {
        Integer count = jdbcTemplate.queryForObject("select count(*) from tbl_vw_post "
                + "where tenant_id = ? and post_id = ?", Integer.class,
                anEvent.tenant().getId(),
                anEvent.postId().getId()
                );
        if(count>0){
            return;
        }

        jdbcTemplate.update("insert into tbl_vw_post( "
                + "post_id, "
                + "author_email_address, author_identity, author_name, "
                + "body_text, changed_on, created_on, "
                + "discussion_id, forum_id, reply_to_post_id, "
                + "subject, tenant_id"
                + ") values(?,?,?,?,?,?,?,?,?,?,?,?)",
                anEvent.postId().getId(),
                anEvent.author().emailAddress(),
                anEvent.author().identity(),
                anEvent.author().name(),
                anEvent.bodyText(),
                new java.sql.Timestamp(anEvent.getOccurredOn().getTime()),
                new java.sql.Timestamp(anEvent.getOccurredOn().getTime()),
                anEvent.discussionId().getId(),
                anEvent.forumId().getId(),
                anEvent.replyToPost() == null ? null : anEvent.replyToPost().getId(),
                anEvent.subject(),
                anEvent.tenant().getId()
                );
    }
}
