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
import top.zhacker.ddd.collaboration.domain.forum.discussion.Discussion;
import top.zhacker.ddd.collaboration.domain.forum.discussion.DiscussionId;
import top.zhacker.ddd.collaboration.domain.forum.discussion.DiscussionRepository;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;

import java.util.UUID;

@Repository
public class EventStoreDiscussionRepository implements DiscussionRepository {
    
    @Autowired
    private EventStreamStore eventStreamStore;

    public EventStoreDiscussionRepository() {
        super();
    }

    @Override
    public Discussion discussionOfId(Tenant aTenant, DiscussionId aDiscussionId) {
        // snapshots not currently supported; always use version 1

        EventStreamId eventId = new EventStreamId(aTenant.getId(), aDiscussionId.getId());

        EventStream eventStream = eventStreamStore.eventStreamSince(eventId);

        Discussion Discussion = new Discussion(eventStream.events(), eventStream.version());

        return Discussion;
    }

    @Override
    public DiscussionId nextIdentity() {
        return new DiscussionId(UUID.randomUUID().toString().toUpperCase());
    }

    @Override
    public void save(Discussion aDiscussion) {
        EventStreamId eventId =
                new EventStreamId(
                        aDiscussion.getTenant().getId(),
                        aDiscussion.getDiscussionId().getId(),
                        aDiscussion.mutatedVersion());

        eventStreamStore.appendWith(eventId, aDiscussion.mutatingEvents());
    }
}
