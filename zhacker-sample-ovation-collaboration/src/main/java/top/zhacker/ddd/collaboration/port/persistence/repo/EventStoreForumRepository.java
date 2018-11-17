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
import top.zhacker.ddd.collaboration.domain.forum.Forum;
import top.zhacker.ddd.collaboration.domain.forum.ForumId;
import top.zhacker.ddd.collaboration.domain.forum.ForumRepository;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;

import java.util.UUID;


@Repository
public class EventStoreForumRepository implements ForumRepository {
    
    @Autowired
    EventStreamStore eventStreamStore;

    public EventStoreForumRepository() {
        super();
    }

    @Override
    public Forum forumOfId(Tenant aTenant, ForumId aForumId) {
        // snapshots not currently supported; always use version 1

        EventStreamId eventId = new EventStreamId(aTenant.getId(), aForumId.getId());

        EventStream eventStream = eventStreamStore.eventStreamSince(eventId);

        Forum forum = new Forum(eventStream.events(), eventStream.version());

        return forum;
    }

    @Override
    public ForumId nextIdentity() {
        return new ForumId(UUID.randomUUID().toString().toUpperCase());
    }

    @Override
    public void save(Forum aForum) {
        EventStreamId eventId =
                new EventStreamId(
                        aForum.tenant().getId(),
                        aForum.forumId().getId(),
                        aForum.mutatedVersion());

        eventStreamStore.appendWith(eventId, aForum.mutatingEvents());
    }
}
