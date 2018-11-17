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
import org.springframework.stereotype.Component;
import top.zhacker.boot.event.stream.DispatchableDomainEvent;
import top.zhacker.boot.event.stream.dispatch.AbstractProjection;
import top.zhacker.boot.event.stream.dispatch.EventStreamDispatcher;
import top.zhacker.ddd.collaboration.domain.forum.event.*;

@Component
public class JdbcTemplateForumProjection
        extends AbstractProjection
        implements EventStreamDispatcher {

    private static final Class<?> understoodEventTypes[] = {
        ForumClosed.class,
        ForumDescriptionChanged.class,
        ForumModeratorChanged.class,
        ForumReopened.class,
        ForumStarted.class,
        ForumSubjectChanged.class
    };

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateForumProjection(@Qualifier("parentEventStreamDispatcher") EventStreamDispatcher aParentEventDispatcher) {
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

    protected void when(ForumClosed anEvent) throws Exception {

        jdbcTemplate.update( "update tbl_vw_forum "
                + "set closed=1 "
                + "where tenant_id = ? and forum_id = ?",
                anEvent.tenant().getId(),
                anEvent.forumId().getId());
    }

    protected void when(ForumReopened anEvent) throws Exception {

        jdbcTemplate.update( "update tbl_vw_forum "
                        + "set closed=0 "
                        + "where tenant_id = ? and forum_id = ?",
                anEvent.tenant().getId(),
                anEvent.forumId().getId());
    }

    protected void when(ForumDescriptionChanged anEvent) throws Exception {

        jdbcTemplate.update("update tbl_vw_forum "
                + "set description=? "
                + "where tenant_id = ? and forum_id = ?",
                anEvent.description(),
                anEvent.tenant().getId(),
                anEvent.forumId().getId()
                );
    }

    protected void when(ForumModeratorChanged anEvent) throws Exception {

        jdbcTemplate.update("update tbl_vw_forum "
                        + "set moderator_email_address=?, moderator_identity=?, moderator_name=?  "
                        + "where tenant_id = ? and forum_id = ?",
                anEvent.moderator().emailAddress(),
                anEvent.moderator().identity(),
                anEvent.moderator().name(),
                anEvent.tenant().getId(),
                anEvent.forumId().getId()
                );
    }


    protected void when(ForumStarted anEvent) throws Exception {

        Integer count = jdbcTemplate.queryForObject("select count(*) from tbl_vw_forum "
                + "where tenant_id = ? and forum_id = ? limit 1",
                Integer.class,
                anEvent.tenant().getId(),
                anEvent.forumId().getId());

        if(count>0){
            return;
        }

        jdbcTemplate.update("insert into tbl_vw_forum( "
                        + "forum_id, closed, "
                        + "creator_email_address, creator_identity, creator_name, "
                        + "description, exclusive_owner, "
                        + "moderator_email_address, moderator_identity, moderator_name, "
                        + "subject, tenant_id"
                        + ") values(?,?,?,?,?,?,?,?,?,?,?,?)",
                anEvent.forumId().getId(),
                0,
                anEvent.creator().emailAddress(),
                anEvent.creator().identity(),
                anEvent.creator().name(),
                anEvent.description(),
                anEvent.exclusiveOwner(),
                anEvent.moderator().emailAddress(),
                anEvent.moderator().identity(),
                anEvent.moderator().name(),
                anEvent.subject(),
                anEvent.tenant().getId()
                );
    }

    protected void when(ForumSubjectChanged anEvent) throws Exception {
        jdbcTemplate.update("update tbl_vw_forum "
                + "set subject=? "
                + "where tenant_id = ? and forum_id = ?",
                anEvent.subject(),
                anEvent.tenant().getId(),
                anEvent.forumId().getId()
                );
    }
}
