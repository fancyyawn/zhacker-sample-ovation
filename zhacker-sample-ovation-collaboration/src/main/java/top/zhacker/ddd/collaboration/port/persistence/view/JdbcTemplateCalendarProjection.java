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
import top.zhacker.ddd.collaboration.domain.calendar.CalendarId;
import top.zhacker.ddd.collaboration.domain.calendar.CalendarSharer;
import top.zhacker.ddd.collaboration.domain.calendar.event.*;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;

@Component
public class JdbcTemplateCalendarProjection
        extends AbstractProjection
        implements EventStreamDispatcher {

    private static final Class<?> understoodEventTypes[] = {
        CalendarCreated.class,
        CalendarDescriptionChanged.class,
        CalendarRenamed.class,
        CalendarShared.class,
        CalendarUnshared.class
    };

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateCalendarProjection(@Qualifier("parentEventStreamDispatcher") EventStreamDispatcher aParentEventDispatcher) {
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
        Integer count = jdbcTemplate.queryForObject( "select count(*) from tbl_vw_calendar "
                        + "where tenant_id = ? and calendar_id = ?",
                Integer.class,
                anEvent.tenant().getId(),
                anEvent.calendarId().getId());
        if(count>0){
            return;
        }

        jdbcTemplate.update("insert into tbl_vw_calendar("
                + "calendar_id, description, name, "
                + "owner_email_address, owner_identity, owner_name, "
                + "tenant_id"
                + ") values(?,?,?,?,?,?,?)",
                anEvent.calendarId().getId(),
                anEvent.description(),
                anEvent.name(),
                anEvent.owner().emailAddress(),
                anEvent.owner().identity(),
                anEvent.owner().name(),
                anEvent.tenant().getId()
                );

        for (CalendarSharer sharer : anEvent.sharedWith()) {
            this.insertCalendarSharer(anEvent.tenant(), anEvent.calendarId(), sharer);
        }
    }

    private void insertCalendarSharer(
            Tenant aTenant,
            CalendarId aCalendarId,
            CalendarSharer aCalendarSharer)
            throws Exception {

        Integer count = jdbcTemplate.queryForObject( "select count(*) from tbl_vw_calendar_sharer "
                        + "where tenant_id = ? and calendar_id = ? and participant_identity = ?",
                Integer.class,
                aTenant.getId(),
                aCalendarId.getId(),
                aCalendarSharer.participant().identity());
        if(count>0){
            return;
        }

        jdbcTemplate.update("insert into tbl_vw_calendar_sharer("
                + "id, calendar_id, "
                + "participant_email_address, participant_identity, participant_name, "
                + "tenant_id"
                + ") values(?,?,?,?,?,?)",
                0,
                aCalendarId.getId(),
                aCalendarSharer.participant().emailAddress(),
                aCalendarSharer.participant().identity(),
                aCalendarSharer.participant().name(),
                aTenant.getId()
                );
    }


    protected void when(CalendarDescriptionChanged anEvent) throws Exception {
        jdbcTemplate.update("update tbl_vw_calendar set description=? "
                + "where calendar_id = ?",
                anEvent.description(),
                anEvent.calendarId().getId()
                );
    }

    protected void when(CalendarRenamed anEvent) throws Exception {
        jdbcTemplate.update("update tbl_vw_calendar set name=? "
                + "where calendar_id = ?",
                anEvent.name(),
                anEvent.calendarId().getId()
        );
    }

    protected void when(CalendarShared anEvent) throws Exception {
        this.insertCalendarSharer(anEvent.tenant(), anEvent.calendarId(), anEvent.calendarSharer());
    }

    protected void when(CalendarUnshared anEvent) throws Exception {
        jdbcTemplate.update("delete from tbl_vw_calendar_sharer "
                + "where tenant_id=? and calendar_id=? and participant_identity=?",
                anEvent.tenant().getId(),
                anEvent.calendarId().getId(),
                anEvent.calendarSharer().participant().identity()
                );
    }

}
