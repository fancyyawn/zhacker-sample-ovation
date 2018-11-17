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
import top.zhacker.boot.event.stream.dispatch.sql.ConnectionProvider;
import top.zhacker.ddd.collaboration.domain.forum.discussion.event.DiscussionClosed;
import top.zhacker.ddd.collaboration.domain.forum.discussion.event.DiscussionReopened;
import top.zhacker.ddd.collaboration.domain.forum.discussion.event.DiscussionStarted;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Repository
public class JdbcTemplateDiscussionProjection
        extends AbstractProjection
        implements EventStreamDispatcher {

    private static final Class<?> understoodEventTypes[] = {
        DiscussionClosed.class,
        DiscussionReopened.class,
        DiscussionStarted.class
    };

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateDiscussionProjection(@Qualifier("parentEventStreamDispatcher") EventStreamDispatcher aParentEventDispatcher) {
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

    protected void when(DiscussionClosed anEvent) throws Exception {
        jdbcTemplate.update("update tbl_vw_discussion "
                + "set closed=1 "
                + "where tenant_id = ? and discussion_id = ?",
                anEvent.getTenant().getId(),
                anEvent.getDiscussionId().getId()
        );
    }

    protected void when(DiscussionReopened anEvent) throws Exception {
        jdbcTemplate.update("update tbl_vw_discussion "
                        + "set closed=0 "
                        + "where tenant_id = ? and discussion_id = ?",
                anEvent.getTenant().getId(),
                anEvent.getDiscussionId().getId()
        );
    }

    protected void when(DiscussionStarted anEvent) throws Exception {
        Integer count = jdbcTemplate.queryForObject("select count(*) from tbl_vw_discussion "
                + "where tenant_id = ? and discussion_id = ?",
                Integer.class,
                anEvent.getTenant().getId(),
                anEvent.getDiscussionId().getId()
                );

        if(count>0){
            return;
        }

        jdbcTemplate.update("insert into tbl_vw_discussion( "
                + "discussion_id, author_email_address, author_identity, author_name, "
                + "closed, exclusive_owner, forum_id, "
                + "subject, tenant_id"
                + ") values(?,?,?,?,?,?,?,?,?)",
                anEvent.getDiscussionId().getId(),
                anEvent.getAuthor().emailAddress(),
                anEvent.getAuthor().identity(),
                anEvent.getAuthor().name(),
                0,
                anEvent.getExclusiveOwner(),
                anEvent.getForumId().getId(),
                anEvent.getSubject(),
                anEvent.getTenant().getId()
                );
    }
}
