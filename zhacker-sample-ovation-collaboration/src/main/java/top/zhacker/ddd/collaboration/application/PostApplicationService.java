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

package top.zhacker.ddd.collaboration.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhacker.ddd.collaboration.application.command.PostModerateCommand;
import top.zhacker.ddd.collaboration.domain.collaborator.CollaboratorService;
import top.zhacker.ddd.collaboration.domain.collaborator.Moderator;
import top.zhacker.ddd.collaboration.domain.forum.Forum;
import top.zhacker.ddd.collaboration.domain.forum.ForumId;
import top.zhacker.ddd.collaboration.domain.forum.ForumRepository;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.Post;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.PostId;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.PostRepository;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;


@Service
public class PostApplicationService {

    private CollaboratorService collaboratorService;
    private ForumRepository forumRepository;
    private PostRepository postRepository;

    @Autowired
    public PostApplicationService(
            PostRepository aPostRepository,
            ForumRepository aForumRepository,
            CollaboratorService aCollaboratorService) {

        super();

        this.collaboratorService = aCollaboratorService;
        this.forumRepository = aForumRepository;
        this.postRepository = aPostRepository;
    }

    public void moderatePost(PostModerateCommand command) {

        Tenant tenant = new Tenant(command.getTenantId());

        Forum forum =
                this.forumRepository()
                    .forumOfId(
                            tenant,
                            new ForumId(command.getForumId()));

        Moderator moderator =
                this.collaboratorService().moderatorFrom(tenant, command.getModeratorId());

        Post post = this.postRepository().postOfId(tenant, new PostId(command.getPostId()));

        forum.moderatePost(post, moderator, command.getSubject(), command.getBodyText());

        this.postRepository().save(post);
    }

    private CollaboratorService collaboratorService() {
        return this.collaboratorService;
    }

    private ForumRepository forumRepository() {
        return this.forumRepository;
    }

    private PostRepository postRepository() {
        return this.postRepository;
    }
}
