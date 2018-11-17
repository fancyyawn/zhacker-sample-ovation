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

package top.zhacker.ddd.identity.domain.tenant.event;


import java.util.Date;

import lombok.Getter;
import top.zhacker.core.model.DomainEvent;
import top.zhacker.ddd.identity.domain.tenant.TenantId;
import top.zhacker.ddd.identity.domain.user.person.EmailAddress;
import top.zhacker.ddd.identity.domain.user.person.FullName;

@Getter
public class TenantAdministratorRegistered implements DomainEvent {

    private FullName administratorName;
    private EmailAddress emailAddress;
    private int eventVersion;
    private Date occurredOn;
    private String temporaryPassword;
    private TenantId tenantId;
    private String tenantName;
    private String username;

    public TenantAdministratorRegistered(
            TenantId aTenantId,
            String aTenantName,
            FullName anAdministratorName,
            EmailAddress anEmailAddress,
            String aUsername,
            String aTemporaryPassword) {

        super();

        this.administratorName = anAdministratorName;
        this.emailAddress = anEmailAddress;
        this.eventVersion = 1;
        this.occurredOn = new Date();
        this.temporaryPassword = aTemporaryPassword;
        this.tenantId = aTenantId;
        this.tenantName = aTenantName;
        this.username = aUsername;
    }
    
    @Override
    public int getEventVersion() {
        return this.eventVersion;
    }

    @Override
    public Date getOccurredOn() {
        return this.occurredOn;
    }
    
    public TenantId tenantId() {
        return this.tenantId;
    }

}
