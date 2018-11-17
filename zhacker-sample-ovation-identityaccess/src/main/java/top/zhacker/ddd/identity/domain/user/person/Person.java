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

package top.zhacker.ddd.identity.domain.user.person;


import lombok.Getter;
import top.zhacker.boot.event.publish.DomainEventPublisher;
import top.zhacker.core.model.IdentifiedEntity;
import top.zhacker.ddd.identity.domain.tenant.TenantId;
import top.zhacker.ddd.identity.domain.user.User;
import top.zhacker.ddd.identity.domain.user.person.event.PersonContactInformationChanged;
import top.zhacker.ddd.identity.domain.user.person.event.PersonNameChanged;

public class Person extends IdentifiedEntity {

    private static final long serialVersionUID = 1L;
    
    @Getter
    private ContactInformation contactInformation;
    @Getter
    private FullName name;
    @Getter
    private TenantId tenantId;
    
    private transient User user;

    public Person(
            TenantId aTenantId,
            FullName aName,
            ContactInformation aContactInformation) {

        this();

        this.setContactInformation(aContactInformation);
        this.setName(aName);
        this.setTenantId(aTenantId);
    }

    public void changeContactInformation(ContactInformation aContactInformation) {
        this.setContactInformation(aContactInformation);

        DomainEventPublisher
            .publish(new PersonContactInformationChanged(
                    this.tenantId(),
                    this.user().getUsername(),
                    this.contactInformation()));
    }

    public void changeName(FullName aName) {
        this.setName(aName);

        DomainEventPublisher
            .publish(new PersonNameChanged(
                    this.tenantId(),
                    this.user().getUsername(),
                    this.name()));
    }

    public ContactInformation contactInformation() {
        return this.contactInformation;
    }

    public EmailAddress emailAddress() {
        return this.contactInformation().emailAddress();
    }

    public FullName name() {
        return this.name;
    }

    @Override
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            Person typedObject = (Person) anObject;
            equalObjects =
                    this.tenantId().equals(typedObject.tenantId()) &&
                    this.user().getUsername().equals(typedObject.user().getUsername());
        }

        return equalObjects;
    }

    @Override
    public int hashCode() {
        int hashCodeValue =
            + (90113 * 223)
            + this.tenantId().hashCode()
            + this.user().getUsername().hashCode();

        return hashCodeValue;
    }

    @Override
    public String toString() {
        return "Person [tenantId=" + tenantId + ", name=" + name + ", contactInformation=" + contactInformation + "]";
    }

    protected Person() {
        super();
    }

    protected void setContactInformation(ContactInformation aContactInformation) {
        this.assertArgumentNotNull(aContactInformation, "The person contact information is required.");

        this.contactInformation = aContactInformation;
    }

    protected void setName(FullName aName) {
        this.assertArgumentNotNull(aName, "The person name is required.");

        this.name = aName;
    }

    protected TenantId tenantId() {
        return this.tenantId;
    }

    public void setTenantId(TenantId aTenantId) {
        this.assertArgumentNotNull(aTenantId, "The tenantId is required.");

        this.tenantId = aTenantId;
    }

    protected User user() {
        return this.user;
    }

    public void internalOnlySetUser(User aUser) {
        this.user = aUser;
    }
}
