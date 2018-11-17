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

package top.zhacker.ddd.collaboration.domain.calendar.event;

import lombok.Getter;
import top.zhacker.core.model.DomainEvent;
import top.zhacker.ddd.collaboration.domain.calendar.CalendarId;
import top.zhacker.ddd.collaboration.domain.calendar.CalendarSharer;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;

import java.util.Date;

@Getter
public class CalendarUnshared implements DomainEvent {

    private CalendarId calendarId;
    private CalendarSharer calendarSharer;
    private int eventVersion;
    private String name;
    private Date occurredOn;
    private Tenant tenant;

    public CalendarUnshared(
            Tenant aTenant,
            CalendarId aCalendarId,
            String aName,
            CalendarSharer aCalendarSharer) {

        super();

        this.calendarId = aCalendarId;
        this.calendarSharer = aCalendarSharer;
        this.eventVersion = 1;
        this.name = aName;
        this.occurredOn = new Date();
        this.tenant = aTenant;
    }

    public CalendarId calendarId() {
        return this.calendarId;
    }

    public CalendarSharer calendarSharer() {
        return this.calendarSharer;
    }

    
    public int eventVersion() {
        return this.eventVersion;
    }

    public String name() {
        return this.name;
    }

    
    public Date occurredOn() {
        return this.occurredOn;
    }

    public Tenant tenant() {
        return this.tenant;
    }
}
