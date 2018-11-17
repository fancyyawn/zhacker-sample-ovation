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

package top.zhacker.ddd.collaboration.port.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zhacker.ddd.collaboration.domain.collaborator.*;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;


@Component
public class TranslatingCollaboratorService implements CollaboratorService {

    @Autowired
    private UserInRoleClient userInRoleClient;

    @Override
    public Author authorFrom(Tenant aTenant, String anIdentity) {

        return userInRoleClient.userInRole(aTenant.getId(), anIdentity, "Author").to(Author.class);
    }

    @Override
    public Creator creatorFrom(Tenant aTenant, String anIdentity) {
        return userInRoleClient.userInRole(aTenant.getId(), anIdentity, "Creator").to(Creator.class);
    }

    @Override
    public Moderator moderatorFrom(Tenant aTenant, String anIdentity) {
        return userInRoleClient.userInRole(aTenant.getId(), anIdentity, "Moderator").to(Moderator.class);
    }

    @Override
    public Owner ownerFrom(Tenant aTenant, String anIdentity) {
        return userInRoleClient.userInRole(aTenant.getId(), anIdentity, "Owner").to(Owner.class);
    }

    @Override
    public Participant participantFrom(Tenant aTenant, String anIdentity) {
        return userInRoleClient.userInRole(aTenant.getId(), anIdentity, "Participant").to(Participant.class);
    }
}
