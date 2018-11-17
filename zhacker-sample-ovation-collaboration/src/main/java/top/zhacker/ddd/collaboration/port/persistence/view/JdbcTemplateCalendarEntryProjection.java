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
import top.zhacker.ddd.collaboration.domain.calendar.entry.CalendarEntryId;
import top.zhacker.ddd.collaboration.domain.calendar.entry.event.*;
import top.zhacker.ddd.collaboration.domain.collaborator.Participant;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;

@Component
public class JdbcTemplateCalendarEntryProjection
        extends AbstractProjection
        implements EventStreamDispatcher {

    private static final Class<?> understoodEventTypes[] = {
        CalendarEntryDescriptionChanged.class,
        CalendarEntryParticipantInvited.class,
        CalendarEntryParticipantUninvited.class,
        CalendarEntryRelocated.class,
        CalendarEntryRescheduled.class,
        CalendarEntryScheduled.class
    };

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateCalendarEntryProjection(@Qualifier("parentEventStreamDispatcher") EventStreamDispatcher aParentEventDispatcher) {
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

    protected void when(CalendarEntryDescriptionChanged anEvent) throws Exception {
        jdbcTemplate.update("update tbl_vw_calendar_entry set description=? "
                + " where calendar_entry_id = ?",
                anEvent.description(),
                anEvent.calendarEntryId().getId()
                );
    }

    protected void when(CalendarEntryParticipantInvited anEvent) throws Exception {
        this.insertInvitee(anEvent.tenant(), anEvent.calendarEntryId(), anEvent.participant());
    }

    protected void when(CalendarEntryParticipantUninvited anEvent) throws Exception {
        jdbcTemplate.update("delete from tbl_vw_calendar_entry_invitee "
                + "where tenant_id = ? and calendar_entry_id = ? and participant_identity = ?",
                anEvent.tenant().getId(),
                anEvent.calendarEntryId().getId(),
                anEvent.participant().identity()
                );
    }

    protected void when(CalendarEntryRelocated anEvent) throws Exception {
        jdbcTemplate.update("update tbl_vw_calendar_entry set location=? "
                + " where calendar_entry_id = ?",
                anEvent.location(),
                anEvent.calendarEntryId().getId()
        );
    }

    protected void when(CalendarEntryRescheduled anEvent) throws Exception {
        jdbcTemplate.update("update tbl_vw_calendar_entry "
                + "set alarm_alarm_units = ?, alarm_alarm_units_type = ?, "
                + "repetition_ends = ?, repetition_type = ?, "
                + "time_span_begins = ?, time_span_ends = ? "
                + " where tenant_id = ? and calendar_entry_id = ?",
                anEvent.alarm().alarmUnits(),
                anEvent.alarm().alarmUnitsType().name(),
                new java.sql.Date(anEvent.repetition().ends().getTime()),
                anEvent.repetition().repeats().name(),
                new java.sql.Date(anEvent.timeSpan().begins().getTime()),
                new java.sql.Date(anEvent.timeSpan().ends().getTime()),
                anEvent.tenant().getId(),
                anEvent.calendarEntryId().getId()
        );
    }

    protected void when(CalendarEntryScheduled anEvent) throws Exception {
        Integer count = jdbcTemplate.queryForObject( "select calendar_entry_id from tbl_vw_calendar_entry "
                        + "where tenant_id = ? and calendar_entry_id = ?",
                Integer.class,
                anEvent.tenant().getId(),
                anEvent.calendarEntryId().getId());
        if(count>0){
            return;
        }
        jdbcTemplate.update("insert into tbl_vw_calendar_entry( "
                + "calendar_entry_id, alarm_alarm_units, alarm_alarm_units_type, "
                + "calendar_id, description, location, "
                + "owner_email_address, owner_identity, owner_name, "
                + "repetition_ends, repetition_type, "
                + "tenant_id, time_span_begins, time_span_ends"
                + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                anEvent.calendarEntryId().getId(),
                anEvent.alarm().alarmUnits(),
                anEvent.alarm().alarmUnitsType().name(),
                anEvent.calendarId().getId(),
                anEvent.description(),
                anEvent.location(),
                anEvent.owner().emailAddress(),
                anEvent.owner().identity(),
                anEvent.owner().name(),
                new java.sql.Date(anEvent.repetition().ends().getTime()),
                anEvent.repetition().repeats().name(),
                anEvent.tenant().getId(),
                new java.sql.Date(anEvent.timeSpan().begins().getTime()),
                new java.sql.Date(anEvent.timeSpan().ends().getTime())
        );

        for (Participant participant : anEvent.invitees()) {
            this.insertInvitee(anEvent.tenant(), anEvent.calendarEntryId(), participant);
        }
    }

    private void insertInvitee(
            Tenant aTenant,
            CalendarEntryId aCalendarEntryId,
            Participant aParticipant)
    throws Exception {

        Integer count = jdbcTemplate.queryForObject("select id from tbl_vw_calendar_entry_invitee "
                        + "where tenant_id = ? and calendar_entry_id = ? and participant_identity = ?",
                Integer.class,
                aTenant.getId(),
                aCalendarEntryId.getId(),
                aParticipant.identity());
        if(count>0){
            return;
        }
        jdbcTemplate.update("insert into tbl_vw_calendar_entry_invitee( "
                + "id, calendar_entry_id, "
                + "participant_email_address, participant_identity, participant_name, "
                + "tenant_id"
                + ") values(?,?,?,?,?,?)",
                0,
                aCalendarEntryId.getId(),
                aParticipant.emailAddress(),
                aParticipant.identity(),
                aParticipant.name(),
                aTenant.getId()
        );
    }
}
