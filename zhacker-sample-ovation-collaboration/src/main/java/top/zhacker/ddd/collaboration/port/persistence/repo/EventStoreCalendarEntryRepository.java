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

package top.zhacker.ddd.collaboration.port.persistence.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import top.zhacker.boot.event.stream.EventStream;
import top.zhacker.boot.event.stream.EventStreamId;
import top.zhacker.boot.event.stream.store.EventStreamStore;
import top.zhacker.ddd.collaboration.domain.calendar.entry.CalendarEntry;
import top.zhacker.ddd.collaboration.domain.calendar.entry.CalendarEntryId;
import top.zhacker.ddd.collaboration.domain.calendar.entry.CalendarEntryRepository;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;

import java.util.UUID;


@Repository
public class EventStoreCalendarEntryRepository
        implements CalendarEntryRepository {
    
    @Autowired
    private EventStreamStore eventStreamStore;
    
    private EventStreamStore eventStore(){
        return this.eventStreamStore;
    }

    public EventStoreCalendarEntryRepository() {
        super();
    }

    @Override
    public CalendarEntry calendarEntryOfId(Tenant aTenant, CalendarEntryId aCalendarEntryId) {
        // snapshots not currently supported; always use version 1

        EventStreamId eventId = new EventStreamId(aTenant.getId(), aCalendarEntryId.getId());

        EventStream eventStream = this.eventStore().eventStreamSince(eventId);

        CalendarEntry calendarEntry = new CalendarEntry(eventStream.events(), eventStream.version());

        return calendarEntry;
    }

    @Override
    public CalendarEntryId nextIdentity() {
        return new CalendarEntryId(UUID.randomUUID().toString().toUpperCase());
    }

    @Override
    public void save(CalendarEntry aCalendarEntry) {
        EventStreamId eventId =
                new EventStreamId(
                        aCalendarEntry.tenant().getId(),
                        aCalendarEntry.calendarEntryId().getId(),
                        aCalendarEntry.mutatedVersion());

        this.eventStore().appendWith(eventId, aCalendarEntry.mutatingEvents());
    }
}
