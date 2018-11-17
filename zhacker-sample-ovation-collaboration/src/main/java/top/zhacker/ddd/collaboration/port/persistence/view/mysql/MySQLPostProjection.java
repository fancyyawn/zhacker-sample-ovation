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
import org.springframework.stereotype.Repository;
import top.zhacker.boot.event.stream.DispatchableDomainEvent;
import top.zhacker.boot.event.stream.dispatch.EventStreamDispatcher;
import top.zhacker.boot.event.stream.dispatch.sql.AbstractSqlProjection;
import top.zhacker.boot.event.stream.dispatch.sql.ConnectionProvider;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.event.PostContentAltered;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.event.PostedToDiscussion;

import java.sql.Connection;
import java.sql.PreparedStatement;


//@Repository
public class MySQLPostProjection extends AbstractSqlProjection implements EventStreamDispatcher {

    private static final Class<?> understoodEventTypes[] = {
        PostContentAltered.class,
        PostedToDiscussion.class
    };

    @Autowired
    public MySQLPostProjection(@Qualifier("parentEventStreamDispatcher") EventStreamDispatcher aParentEventDispatcher) {
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
        Connection connection = ConnectionProvider.connection();

        PreparedStatement statement =
                connection.prepareStatement(
                        "update tbl_vw_post "
                        + "set body_text=?, subject=?, changed_on=? "
                        + "where tenant_id = ? and forum_id = ?");

        statement.setString(1, anEvent.bodyText());
        statement.setString(2, anEvent.subject());
        statement.setDate(3, new java.sql.Date(anEvent.getOccurredOn().getTime()));
        statement.setString(4, anEvent.tenant().getId());
        statement.setString(5, anEvent.postId().getId());

        this.execute(statement);
    }

    protected void when(PostedToDiscussion anEvent) throws Exception {
        Connection connection = ConnectionProvider.connection();

        // idempotent operation
        if (this.exists(
                "select post_id from tbl_vw_post "
                    + "where tenant_id = ? and post_id = ?",
                anEvent.tenant().getId(),
                anEvent.postId().getId())) {
            return;
        }

        PreparedStatement statement =
                connection.prepareStatement(
                        "insert into tbl_vw_post( "
                        + "post_id, "
                        + "author_email_address, author_identity, author_name, "
                        + "body_text, changed_on, created_on, "
                        + "discussion_id, forum_id, reply_to_post_id, "
                        + "subject, tenant_id"
                        + ") values(?,?,?,?,?,?,?,?,?,?,?,?)");

        statement.setString(1, anEvent.postId().getId());
        statement.setString(2, anEvent.author().emailAddress());
        statement.setString(3, anEvent.author().identity());
        statement.setString(4, anEvent.author().name());
        statement.setString(5, anEvent.bodyText());
        statement.setTimestamp(6, new java.sql.Timestamp(anEvent.getOccurredOn().getTime()));
        statement.setTimestamp(7, new java.sql.Timestamp(anEvent.getOccurredOn().getTime()));
        statement.setString(8, anEvent.discussionId().getId());
        statement.setString(9, anEvent.forumId().getId());
        statement.setString(10, anEvent.replyToPost() == null ? null : anEvent.replyToPost().getId());
        statement.setString(11, anEvent.subject());
        statement.setString(12, anEvent.tenant().getId());

        this.execute(statement);
    }
}
