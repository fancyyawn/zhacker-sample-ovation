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
import top.zhacker.ddd.collaboration.domain.calendar.CalendarId;
import top.zhacker.ddd.collaboration.domain.calendar.CalendarSharer;
import top.zhacker.ddd.collaboration.domain.calendar.event.*;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;

import java.sql.Connection;
import java.sql.PreparedStatement;

//@Component
public class MySQLCalendarProjection
        extends AbstractSqlProjection
        implements EventStreamDispatcher {

    private static final Class<?> understoodEventTypes[] = {
        CalendarCreated.class,
        CalendarDescriptionChanged.class,
        CalendarRenamed.class,
        CalendarShared.class,
        CalendarUnshared.class
    };

    @Autowired
    public MySQLCalendarProjection(@Qualifier("parentEventStreamDispatcher") EventStreamDispatcher aParentEventDispatcher) {
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

    protected void when(CalendarCreated anEvent) throws Exception {
        Connection connection = ConnectionProvider.connection();

        // idempotent operation
        if (this.exists(
                "select calendar_id from tbl_vw_calendar "
                    + "where tenant_id = ? and calendar_id = ?",
                anEvent.tenant().getId(),
                anEvent.calendarId().getId())) {
            return;
        }

        PreparedStatement statement =
                connection.prepareStatement(
                        "insert into tbl_vw_calendar("
                        + "calendar_id, description, name, "
                        + "owner_email_address, owner_identity, owner_name, "
                        + "tenant_id"
                        + ") values(?,?,?,?,?,?,?)");

        statement.setString(1, anEvent.calendarId().getId());
        statement.setString(2, anEvent.description());
        statement.setString(3, anEvent.name());
        statement.setString(4, anEvent.owner().emailAddress());
        statement.setString(5, anEvent.owner().identity());
        statement.setString(6, anEvent.owner().name());
        statement.setString(7, anEvent.tenant().getId());

        this.execute(statement);

        for (CalendarSharer sharer : anEvent.sharedWith()) {
            this.insertCalendarSharer(anEvent.tenant(), anEvent.calendarId(), sharer);
        }
    }

    protected void when(CalendarDescriptionChanged anEvent) throws Exception {
        Connection connection = ConnectionProvider.connection();

        PreparedStatement statement =
                connection.prepareStatement(
                        "update tbl_vw_calendar set description=? "
                        + "where calendar_id = ?");

        statement.setString(1, anEvent.description());
        statement.setString(2, anEvent.calendarId().getId());

        this.execute(statement);
    }

    protected void when(CalendarRenamed anEvent) throws Exception {
        Connection connection = ConnectionProvider.connection();

        PreparedStatement statement =
                connection.prepareStatement(
                        "update tbl_vw_calendar set name=? "
                        + "where calendar_id = ?");

        statement.setString(1, anEvent.name());
        statement.setString(2, anEvent.calendarId().getId());

        this.execute(statement);
    }

    protected void when(CalendarShared anEvent) throws Exception {
        this.insertCalendarSharer(anEvent.tenant(), anEvent.calendarId(), anEvent.calendarSharer());
    }

    protected void when(CalendarUnshared anEvent) throws Exception {
        Connection connection = ConnectionProvider.connection();

        PreparedStatement statement =
                connection.prepareStatement(
                        "delete from tbl_vw_calendar_sharer "
                        + "where tenant_id=? and calendar_id=? and participant_identity=?");

        statement.setString(1, anEvent.tenant().getId());
        statement.setString(2, anEvent.calendarId().getId());
        statement.setString(3, anEvent.calendarSharer().participant().identity());

        this.execute(statement);
    }

    private void insertCalendarSharer(
            Tenant aTenant,
            CalendarId aCalendarId,
            CalendarSharer aCalendarSharer)
    throws Exception {

        Connection connection = ConnectionProvider.connection();

        // idempotent operation
        if (this.exists(
                "select id from tbl_vw_calendar_sharer "
                    + "where tenant_id = ? and calendar_id = ? and participant_identity = ?",
                aTenant.getId(),
                aCalendarId.getId(),
                aCalendarSharer.participant().identity())) {
            return;
        }

        PreparedStatement statement =
                connection.prepareStatement(
                        "insert into tbl_vw_calendar_sharer("
                        + "id, calendar_id, "
                        + "participant_email_address, participant_identity, participant_name, "
                        + "tenant_id"
                        + ") values(?,?,?,?,?,?)");

        statement.setLong(1, 0);
        statement.setString(2, aCalendarId.getId());
        statement.setString(3, aCalendarSharer.participant().emailAddress());
        statement.setString(4, aCalendarSharer.participant().identity());
        statement.setString(5, aCalendarSharer.participant().name());
        statement.setString(6, aTenant.getId());

        this.execute(statement);
    }
}
