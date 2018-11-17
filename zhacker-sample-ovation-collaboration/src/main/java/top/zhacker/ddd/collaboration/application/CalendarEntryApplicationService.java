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

package top.zhacker.ddd.collaboration.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhacker.ddd.collaboration.domain.calendar.entry.*;
import top.zhacker.ddd.collaboration.domain.collaborator.CollaboratorService;
import top.zhacker.ddd.collaboration.domain.collaborator.Participant;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Service
public class CalendarEntryApplicationService {

    private CalendarEntryRepository calendarEntryRepository;
    private CollaboratorService collaboratorService;

    @Autowired
    public CalendarEntryApplicationService(
            CalendarEntryRepository aCalendarEntryRepository,
            CollaboratorService aCollaboratorService) {

        super();

        this.calendarEntryRepository = aCalendarEntryRepository;
        this.collaboratorService = aCollaboratorService;
    }

    public void changeCalendarEntryDescription(
            String aTenantId,
            String aCalendarEntryId,
            String aDescription) {

        Tenant tenant = new Tenant(aTenantId);

        CalendarEntry calendarEntry =
                this.calendarEntryRepository()
                    .calendarEntryOfId(
                            tenant,
                            new CalendarEntryId(aCalendarEntryId));

        calendarEntry.changeDescription(aDescription);

        this.calendarEntryRepository().save(calendarEntry);
    }

    public void inviteCalendarEntryParticipant(
            String aTenantId,
            String aCalendarEntryId,
            Set<String> aParticipantsToInvite) {

        Tenant tenant = new Tenant(aTenantId);

        CalendarEntry calendarEntry =
                this.calendarEntryRepository()
                    .calendarEntryOfId(
                            tenant,
                            new CalendarEntryId(aCalendarEntryId));

        for (Participant participant : this.inviteesFrom(tenant, aParticipantsToInvite)) {
            calendarEntry.invite(participant);
        }

        this.calendarEntryRepository().save(calendarEntry);
    }

    public void relocateCalendarEntry(
            String aTenantId,
            String aCalendarEntryId,
            String aLocation) {

        Tenant tenant = new Tenant(aTenantId);

        CalendarEntry calendarEntry =
                this.calendarEntryRepository()
                    .calendarEntryOfId(
                            tenant,
                            new CalendarEntryId(aCalendarEntryId));

        calendarEntry.relocate(aLocation);

        this.calendarEntryRepository().save(calendarEntry);
    }

    public void rescheduleCalendarEntry(
            String aTenantId,
            String aCalendarEntryId,
            String aDescription,
            String aLocation,
            Date aTimeSpanBegins,
            Date aTimeSpanEnds,
            String aRepeatType,
            Date aRepeatEndsOnDate,
            String anAlarmType,
            int anAlarmUnits) {

        Tenant tenant = new Tenant(aTenantId);

        CalendarEntry calendarEntry =
                this.calendarEntryRepository()
                    .calendarEntryOfId(
                            tenant,
                            new CalendarEntryId(aCalendarEntryId));

        calendarEntry.reschedule(
                aDescription,
                aLocation,
                new TimeSpan(aTimeSpanBegins, aTimeSpanEnds),
                new Repetition(RepeatType.valueOf(aRepeatType), aRepeatEndsOnDate),
                new Alarm(AlarmUnitsType.valueOf(anAlarmType), anAlarmUnits));

        this.calendarEntryRepository().save(calendarEntry);
    }

    public void uninviteCalendarEntryParticipant(
            String aTenantId,
            String aCalendarEntryId,
            Set<String> aParticipantsToInvite) {

        Tenant tenant = new Tenant(aTenantId);

        CalendarEntry calendarEntry =
                this.calendarEntryRepository()
                    .calendarEntryOfId(
                            tenant,
                            new CalendarEntryId(aCalendarEntryId));

        for (Participant participant : this.inviteesFrom(tenant, aParticipantsToInvite)) {
            calendarEntry.uninvite(participant);
        }

        this.calendarEntryRepository().save(calendarEntry);
    }

    private CalendarEntryRepository calendarEntryRepository() {
        return this.calendarEntryRepository;
    }

    private CollaboratorService collaboratorService() {
        return this.collaboratorService;
    }

    private Set<Participant> inviteesFrom(
            Tenant aTenant,
            Set<String> aParticipantsToInvite) {

        Set<Participant> invitees = new HashSet<Participant>();

        for (String participatnId : aParticipantsToInvite) {
            Participant participant =
                    this.collaboratorService().participantFrom(aTenant, participatnId);

            invitees.add(participant);
        }

        return invitees;
    }
}
