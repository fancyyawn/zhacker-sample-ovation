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

package top.zhacker.ddd.collaboration.port.persistence.view.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import top.zhacker.boot.event.stream.DispatchableDomainEvent;
import top.zhacker.boot.event.stream.dispatch.EventStreamDispatcher;
import top.zhacker.boot.event.stream.dispatch.sql.AbstractSqlProjection;
import top.zhacker.boot.event.stream.dispatch.sql.ConnectionProvider;
import top.zhacker.ddd.collaboration.domain.forum.event.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

//@Component
public class MySQLForumProjection
        extends AbstractSqlProjection
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
    public MySQLForumProjection(@Qualifier("parentEventStreamDispatcher") EventStreamDispatcher aParentEventDispatcher) {
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
        Connection connection = ConnectionProvider.connection();

        PreparedStatement statement =
                connection.prepareStatement(
                        "update tbl_vw_forum "
                        + "set closed=1 "
                        + "where tenant_id = ? and forum_id = ?");

        statement.setString(1, anEvent.tenant().getId());
        statement.setString(2, anEvent.forumId().getId());

        this.execute(statement);
    }

    protected void when(ForumDescriptionChanged anEvent) throws Exception {
        Connection connection = ConnectionProvider.connection();

        PreparedStatement statement =
                connection.prepareStatement(
                        "update tbl_vw_forum "
                        + "set description=? "
                        + "where tenant_id = ? and forum_id = ?");

        statement.setString(1, anEvent.description());
        statement.setString(2, anEvent.tenant().getId());
        statement.setString(3, anEvent.forumId().getId());

        this.execute(statement);
    }

    protected void when(ForumModeratorChanged anEvent) throws Exception {
        Connection connection = ConnectionProvider.connection();

        PreparedStatement statement =
                connection.prepareStatement(
                        "update tbl_vw_forum "
                        + "set moderator_email_address=?, moderator_identity=?, moderator_name=?  "
                        + "where tenant_id = ? and forum_id = ?");

        statement.setString(1, anEvent.moderator().emailAddress());
        statement.setString(2, anEvent.moderator().identity());
        statement.setString(3, anEvent.moderator().name());
        statement.setString(4, anEvent.tenant().getId());
        statement.setString(5, anEvent.forumId().getId());

        this.execute(statement);
    }

    protected void when(ForumReopened anEvent) throws Exception {
        Connection connection = ConnectionProvider.connection();

        PreparedStatement statement =
                connection.prepareStatement(
                        "update tbl_vw_forum "
                        + "set closed=0 "
                        + "where tenant_id = ? and forum_id = ?");

        statement.setString(1, anEvent.tenant().getId());
        statement.setString(2, anEvent.forumId().getId());

        this.execute(statement);
    }

    protected void when(ForumStarted anEvent) throws Exception {
        Connection connection = ConnectionProvider.connection();

        // idempotent operation
        if (this.exists(
                "select forum_id from tbl_vw_forum "
                    + "where tenant_id = ? and forum_id = ?",
                anEvent.tenant().getId(),
                anEvent.forumId().getId())) {
            return;
        }

        PreparedStatement statement =
                connection.prepareStatement(
                        "insert into tbl_vw_forum( "
                        + "forum_id, closed, "
                        + "creator_email_address, creator_identity, creator_name, "
                        + "description, exclusive_owner, "
                        + "moderator_email_address, moderator_identity, moderator_name, "
                        + "subject, tenant_id"
                        + ") values(?,?,?,?,?,?,?,?,?,?,?,?)");

        statement.setString(1, anEvent.forumId().getId());
        statement.setInt(2, 0);
        statement.setString(3, anEvent.creator().emailAddress());
        statement.setString(4, anEvent.creator().identity());
        statement.setString(5, anEvent.creator().name());
        statement.setString(6, anEvent.description());
        statement.setString(7, anEvent.exclusiveOwner());
        statement.setString(8, anEvent.moderator().emailAddress());
        statement.setString(9, anEvent.moderator().identity());
        statement.setString(10, anEvent.moderator().name());
        statement.setString(11, anEvent.subject());
        statement.setString(12, anEvent.tenant().getId());

        this.execute(statement);
    }

    protected void when(ForumSubjectChanged anEvent) throws Exception {
        Connection connection = ConnectionProvider.connection();

        PreparedStatement statement =
                connection.prepareStatement(
                        "update tbl_vw_forum "
                        + "set subject=? "
                        + "where tenant_id = ? and forum_id = ?");

        statement.setString(1, anEvent.subject());
        statement.setString(2, anEvent.tenant().getId());
        statement.setString(3, anEvent.forumId().getId());

        this.execute(statement);
    }
}
