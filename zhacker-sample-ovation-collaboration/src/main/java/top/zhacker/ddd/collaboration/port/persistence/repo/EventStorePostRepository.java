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
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.Post;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.PostId;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.PostRepository;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;

import java.util.UUID;

@Repository
public class EventStorePostRepository
        implements PostRepository {
    
    @Autowired
    private EventStreamStore eventStreamStore;

    public EventStorePostRepository() {
        super();
    }

    @Override
    public Post postOfId(Tenant aTenantId, PostId aPostId) {
        // snapshots not currently supported; always use version 1

        EventStreamId eventId = new EventStreamId(aTenantId.getId(), aPostId.getId());

        EventStream eventStream = this.eventStreamStore.eventStreamSince(eventId);

        Post Post = new Post(eventStream.events(), eventStream.version());

        return Post;
    }

    @Override
    public PostId nextIdentity() {
        return new PostId(UUID.randomUUID().toString().toUpperCase());
    }

    @Override
    public void save(Post aPost) {
        EventStreamId eventId =
                new EventStreamId(
                        aPost.getTenant().getId(),
                        aPost.getPostId().getId(),
                        aPost.mutatedVersion());

        this.eventStreamStore.appendWith(eventId, aPost.mutatingEvents());
    }
}
