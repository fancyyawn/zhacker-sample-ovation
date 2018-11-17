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

package top.zhacker.ddd.agilepm.domain.product.blacklogitem.task;


import lombok.Getter;
import top.zhacker.boot.event.publish.DomainEventPublisher;
import top.zhacker.core.model.IdentifiedEntity;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.BacklogItemId;
import top.zhacker.ddd.agilepm.domain.product.blacklogitem.task.event.*;
import top.zhacker.ddd.agilepm.domain.team.member.TeamMember;
import top.zhacker.ddd.agilepm.domain.team.member.TeamMemberId;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;

import java.util.*;

@Getter
public class Task extends IdentifiedEntity {

    private BacklogItemId backlogItemId;
    private String description;
    private List<EstimationLogEntry> estimationLog;
    private int hoursRemaining;
    private String name;
    private TaskStatus status;
    private TaskId taskId;
    private TenantId tenantId;
    private TeamMemberId volunteer;

    public List<EstimationLogEntry> allEstimationLogEntries() {
        return Collections.unmodifiableList(this.estimationLog());
    }

    public String description() {
        return this.description;
    }

    public String name() {
        return this.name;
    }

    public TaskStatus status() {
        return this.status;
    }

    public TeamMemberId volunteer() {
        return this.volunteer;
    }

    protected void setVolunteer(TeamMemberId aVolunteer) {
        this.assertArgumentNotNull(aVolunteer, "The volunteer id must be provided.");
        this.assertArgumentEquals(this.tenantId(), aVolunteer.getTenantId(), "The volunteer must be of the same tenant.");

        this.volunteer = aVolunteer;
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            Task typedObject = (Task) anObject;
            equalObjects =
                this.tenantId().equals(typedObject.tenantId()) &&
                this.backlogItemId().equals(typedObject.backlogItemId()) &&
                this.taskId().equals(typedObject.taskId());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        int hashCodeValue =
            + (73721 * 23)
            + this.tenantId().hashCode()
            + this.backlogItemId().hashCode()
            + this.taskId().hashCode();

        return hashCodeValue;
    }

    public Task(
            TenantId aTenantId,
            BacklogItemId aBacklogItemId,
            TaskId aTaskId,
            TeamMember aVolunteer,
            String aName,
            String aDescription,
            int aHoursRemaining,
            TaskStatus aStatus) {

        this();

        this.setBacklogItemId(aBacklogItemId);
        this.setDescription(aDescription);
        this.setHoursRemaining(aHoursRemaining);
        this.setName(aName);
        this.setStatus(aStatus);
        this.setTaskId(aTaskId);
        this.setTenantId(aTenantId);
        this.setVolunteer(aVolunteer.teamMemberId());
    }

    private Task() {
        super();

        this.setEstimationLog(new ArrayList<EstimationLogEntry>(0));
    }

    public void assignVolunteer(TeamMember aVolunteer) {
        this.setVolunteer(aVolunteer.teamMemberId());
  
      DomainEventPublisher
            .publish(new TaskVolunteerAssigned(
                    this.tenantId(),
                    this.backlogItemId(),
                    this.taskId(),
                    this.volunteer().getId()));
    }

    public void changeStatus(TaskStatus aStatus) {
        this.setStatus(aStatus);
  
      DomainEventPublisher
            .publish(new TaskStatusChanged(
                    this.tenantId(),
                    this.backlogItemId(),
                    this.taskId(),
                    this.status()));
    }

    public void describeAs(String aDescription) {
        this.setDescription(aDescription);
  
      DomainEventPublisher
            .publish(new TaskDescribed(
                    this.tenantId(),
                    this.backlogItemId(),
                    this.taskId(),
                    this.description()));
    }

    public void estimateHoursRemaining(int anHoursRemaining) {
        if (anHoursRemaining < 0) {
            throw new IllegalArgumentException(
                    "Hours reminaing is illegal: " + anHoursRemaining);
        }

        if (anHoursRemaining != this.hoursRemaining()) {
            this.setHoursRemaining(anHoursRemaining);
  
          DomainEventPublisher
                .publish(new TaskHoursRemainingEstimated(
                        this.tenantId(),
                        this.backlogItemId(),
                        this.taskId(),
                        this.hoursRemaining()));

            if (anHoursRemaining == 0 && !this.status().isDone()) {
                this.changeStatus(TaskStatus.DONE);
            } else if (anHoursRemaining > 0 && !this.status().isInProgress()) {
                this.changeStatus(TaskStatus.IN_PROGRESS);
            }

            this.logEstimation(anHoursRemaining);
        }
    }

    public void rename(String aName) {
        this.setName(aName);

        DomainEventPublisher
            .publish(new TaskRenamed(
                    this.tenantId(),
                    this.backlogItemId(),
                    this.taskId(),
                    this.name()));
    }

    protected BacklogItemId backlogItemId() {
        return this.backlogItemId;
    }

    protected void setBacklogItemId(BacklogItemId aBacklogItemId) {
        if (aBacklogItemId == null) {
            throw new IllegalArgumentException("The backlogItemId is required.");
        }

        this.backlogItemId = aBacklogItemId;
    }

    protected void setDescription(String aDescription) {
        if (aDescription == null || aDescription.length() == 0) {
            throw new IllegalArgumentException("Description is required.");
        }
        if (aDescription.length() > 65000) {
            throw new IllegalArgumentException("Description must be 65000 characters or less.");
        }

        this.description = aDescription;
    }

    protected List<EstimationLogEntry> estimationLog() {
        return this.estimationLog;
    }

    protected void setEstimationLog(List<EstimationLogEntry> anEstimationLog) {
        this.estimationLog = anEstimationLog;
    }

    public int hoursRemaining() {
        return this.hoursRemaining;
    }

    protected void setHoursRemaining(int aHoursRemaining) {
        this.hoursRemaining = aHoursRemaining;
    }

    protected void setName(String aName) {
        if (aName == null || aName.length() == 0) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (aName.length() > 100) {
            throw new IllegalArgumentException("Name must be 100 characters or less.");
        }

        this.name = aName;
    }

    protected void setStatus(TaskStatus aStatus) {
        if (aStatus == null) {
            throw new IllegalArgumentException("Status is required.");
        }

        this.status = aStatus;
    }

    public TaskId taskId() {
        return this.taskId;
    }

    protected void setTaskId(TaskId aTaskId) {
        if (aTaskId == null) {
            throw new IllegalArgumentException("The taskId is required.");
        }
        this.taskId = aTaskId;
    }

    protected TenantId tenantId() {
        return this.tenantId;
    }

    protected void setTenantId(TenantId aTenantId) {
        if (aTenantId == null) {
            throw new IllegalArgumentException("The tenantId is required.");
        }

        this.tenantId = aTenantId;
    }

    private void logEstimation(int anHoursRemaining) {
        Date today = EstimationLogEntry.currentLogDate();

        boolean updatedLogForToday = false;

        Iterator<EstimationLogEntry> iterator = this.estimationLog().iterator();

        while (!updatedLogForToday && iterator.hasNext()) {
            EstimationLogEntry entry = iterator.next();

            updatedLogForToday =
                    entry.updateHoursRemainingWhenDateMatches(
                            anHoursRemaining,
                            today);
        }

        if (!updatedLogForToday) {
            this.estimationLog().add(
                    new EstimationLogEntry(
                            this.tenantId(),
                            this.taskId(),
                            today,
                            anHoursRemaining));
        }
    }
}
