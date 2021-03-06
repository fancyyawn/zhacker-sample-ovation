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

package top.zhacker.ddd.agilepm.domain.product.blacklogitem.task.event;

import top.zhacker.core.model.DomainEvent;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemId;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.task.TaskId;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;

import java.util.Date;


public class TaskDefined implements DomainEvent {

    private BacklogItemId backlogItemId;
    private String description;
    private int eventVersion;
    private int hoursRemaining;
    private String name;
    private Date occurredOn;
    private TaskId taskId;
    private TenantId tenantId;
    private String volunteerMemberId;

    public TaskDefined(
            TenantId aTenantId,
            BacklogItemId aBacklogItemId,
            TaskId aTaskId,
            String aVolunteerMemberId,
            String aName,
            String aDescription,
            int aHoursRemaining) {

        super();

        this.backlogItemId = aBacklogItemId;
        this.description = aDescription;
        this.eventVersion = 1;
        this.hoursRemaining = aHoursRemaining;
        this.name = aName;
        this.occurredOn = new Date();
        this.taskId = aTaskId;
        this.tenantId = aTenantId;
        this.volunteerMemberId = aVolunteerMemberId;
    }

    public BacklogItemId backlogItemId() {
        return this.backlogItemId;
    }

    public String description() {
        return this.description;
    }

    @Override
    public int eventVersion() {
        return this.eventVersion;
    }

    public int hoursRemaining() {
        return this.hoursRemaining;
    }

    public String name() {
        return this.name;
    }

    @Override
    public Date occurredOn() {
        return this.occurredOn;
    }

    public TaskId taskId() {
        return this.taskId;
    }

    public TenantId tenantId() {
        return this.tenantId;
    }

    public String volunteerMemberId() {
        return this.volunteerMemberId;
    }
}
