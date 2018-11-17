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
import top.zhacker.ddd.collaboration.application.vo.CalendarCommandResult;
import top.zhacker.ddd.collaboration.domain.calendar.Calendar;
import top.zhacker.ddd.collaboration.domain.calendar.CalendarId;
import top.zhacker.ddd.collaboration.domain.calendar.CalendarRepository;
import top.zhacker.ddd.collaboration.domain.calendar.CalendarSharer;
import top.zhacker.ddd.collaboration.domain.calendar.entry.*;
import top.zhacker.ddd.collaboration.domain.collaborator.CollaboratorService;
import top.zhacker.ddd.collaboration.domain.collaborator.Owner;
import top.zhacker.ddd.collaboration.domain.collaborator.Participant;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class CalendarApplicationService {

    private CalendarRepository calendarRepository;
    private CalendarEntryRepository calendarEntryRepository;
    private CollaboratorService collaboratorService;

    @Autowired
    public CalendarApplicationService(
            CalendarRepository aCalendarRepository,
            CalendarEntryRepository aCalendarEntryRepository,
            CollaboratorService aCollaboratorService) {

        super();

        this.calendarRepository = aCalendarRepository;
        this.calendarEntryRepository = aCalendarEntryRepository;
        this.collaboratorService = aCollaboratorService;
    }

    public void changeCalendarDescription(
            String aTenantId,
            String aCalendarId,
            String aDescription) {

        Tenant tenant = new Tenant(aTenantId);

        Calendar calendar =
                this.calendarRepository()
                    .calendarOfId(
                            tenant,
                            new CalendarId(aCalendarId));

        calendar.changeDescription(aDescription);

        this.calendarRepository().save(calendar);
    }

    public void createCalendar(
            String aTenantId,
            String aName,
            String aDescription,
            String anOwnerId,
            Set<String> aParticipantsToSharedWith,
            CalendarCommandResult aCalendarCommandResult) {

        Tenant tenant = new Tenant(aTenantId);

        Owner owner = this.collaboratorService().ownerFrom(tenant, anOwnerId);

        Set<CalendarSharer> sharers = this.sharersFrom(tenant, aParticipantsToSharedWith);

        Calendar calendar =
                new Calendar(
                        tenant,
                        this.calendarRepository.nextIdentity(),
                        aName,
                        aDescription,
                        owner,
                        sharers);

        this.calendarRepository().save(calendar);

        aCalendarCommandResult.resultingCalendarId(calendar.calendarId().getId());
    }

    public void renameCalendar(
            String aTenantId,
            String aCalendarId,
            String aName) {

        Tenant tenant = new Tenant(aTenantId);

        Calendar calendar =
                this.calendarRepository()
                    .calendarOfId(
                            tenant,
                            new CalendarId(aCalendarId));

        calendar.rename(aName);

        this.calendarRepository().save(calendar);
    }

    public void scheduleCalendarEntry(
            String aTenantId,
            String aCalendarId,
            String aDescription,
            String aLocation,
            String anOwnerId,
            Date aTimeSpanBegins,
            Date aTimeSpanEnds,
            String aRepeatType,
            Date aRepeatEndsOnDate,
            String anAlarmType,
            int anAlarmUnits,
            Set<String> aParticipantsToInvite,
            CalendarCommandResult aCalendarCommandResult) {

        Tenant tenant = new Tenant(aTenantId);

        Calendar calendar =
                this.calendarRepository()
                    .calendarOfId(
                            tenant,
                            new CalendarId(aCalendarId));

        CalendarEntry calendarEntry =
                calendar.scheduleCalendarEntry(
                    aDescription,
                    aLocation,
                    this.collaboratorService().ownerFrom(tenant, anOwnerId),
                    new TimeSpan(aTimeSpanBegins, aTimeSpanEnds),
                    new Repetition(RepeatType.valueOf(aRepeatType), aRepeatEndsOnDate),
                    new Alarm(AlarmUnitsType.valueOf(anAlarmType), anAlarmUnits),
                    this.inviteesFrom(tenant, aParticipantsToInvite));

        this.calendarEntryRepository().save(calendarEntry);

        aCalendarCommandResult.resultingCalendarId(aCalendarId);
        aCalendarCommandResult.resultingCalendarEntryId(calendarEntry.calendarEntryId().getId());
    }

    public void shareCalendarWith(
            String aTenantId,
            String aCalendarId,
            Set<String> aParticipantsToSharedWith) {

        Tenant tenant = new Tenant(aTenantId);

        Calendar calendar =
                this.calendarRepository()
                    .calendarOfId(
                            tenant,
                            new CalendarId(aCalendarId));

        for (CalendarSharer sharer : this.sharersFrom(tenant, aParticipantsToSharedWith)) {
            calendar.shareCalendarWith(sharer);
        }

        this.calendarRepository().save(calendar);
    }

    public void unshareCalendarWith(
            String aTenantId,
            String aCalendarId,
            Set<String> aParticipantsToUnsharedWith) {

        Tenant tenant = new Tenant(aTenantId);

        Calendar calendar =
                this.calendarRepository()
                    .calendarOfId(
                            tenant,
                            new CalendarId(aCalendarId));

        for (CalendarSharer sharer : this.sharersFrom(tenant, aParticipantsToUnsharedWith)) {
            calendar.unshareCalendarWith(sharer);
        }

        this.calendarRepository().save(calendar);
    }

    private CalendarRepository calendarRepository() {
        return this.calendarRepository;
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

    private Set<CalendarSharer> sharersFrom(
            Tenant aTenant,
            Set<String> aParticipantsToSharedWith) {

        Set<CalendarSharer> sharers =
                new HashSet<CalendarSharer>(aParticipantsToSharedWith.size());

        for (String participatnId : aParticipantsToSharedWith) {
            Participant participant =
                    this.collaboratorService().participantFrom(aTenant, participatnId);

            sharers.add(new CalendarSharer(participant));
        }

        return sharers;
    }
}
