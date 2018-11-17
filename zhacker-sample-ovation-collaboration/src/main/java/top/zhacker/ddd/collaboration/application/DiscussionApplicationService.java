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

import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhacker.ddd.collaboration.application.command.PostCreateCommand;
import top.zhacker.ddd.collaboration.application.vo.DiscussionCommandResult;
import top.zhacker.ddd.collaboration.domain.collaborator.Author;
import top.zhacker.ddd.collaboration.domain.collaborator.CollaboratorService;
import top.zhacker.ddd.collaboration.domain.forum.discussion.Discussion;
import top.zhacker.ddd.collaboration.domain.forum.discussion.DiscussionId;
import top.zhacker.ddd.collaboration.domain.forum.discussion.DiscussionRepository;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.Post;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.PostId;
import top.zhacker.ddd.collaboration.domain.forum.discussion.post.PostRepository;
import top.zhacker.ddd.collaboration.domain.tenant.Tenant;


@Service
public class DiscussionApplicationService {

    private CollaboratorService collaboratorService;
    private DiscussionRepository discussionRepository;
    private PostRepository postRepository;

    @Autowired
    public DiscussionApplicationService(
            DiscussionRepository aDiscussionRepository,
            PostRepository aPostRepository,
            CollaboratorService aCollaboratorService) {

        super();

        this.collaboratorService = aCollaboratorService;
        this.discussionRepository = aDiscussionRepository;
        this.postRepository = aPostRepository;
    }

    public void closeDiscussion(String aTenantId, String aDiscussionId) {
        Discussion discussion =
                this.discussionRepository()
                    .discussionOfId(new Tenant(aTenantId),
                            new DiscussionId(aDiscussionId));

        discussion.close();

        this.discussionRepository().save(discussion);
    }
    
    public void postToDiscussion(PostCreateCommand command, DiscussionCommandResult commandResult){
        if(Strings.isNullOrEmpty(command.getReplyToPostId())){
            postToDiscussion(
                command.getTenantId(),
                command.getDiscussionId(),
                command.getAuthorId(),
                command.getSubject(),
                command.getBodyText(),
                commandResult
            );
        }else{
            postToDiscussionInReplyTo(
                command.getTenantId(),
                command.getDiscussionId(),
                command.getReplyToPostId(),
                command.getAuthorId(),
                command.getSubject(),
                command.getBodyText(),
                commandResult
            );
        }
    }

    public void postToDiscussion(
            String aTenantId,
            String aDiscussionId,
            String anAuthorId,
            String aSubject,
            String aBodyText,
            DiscussionCommandResult aDiscussionCommandResult) {

        Discussion discussion =
                this.discussionRepository()
                    .discussionOfId(new Tenant(aTenantId),
                            new DiscussionId(aDiscussionId));

        Author author =
                this.collaboratorService().authorFrom(new Tenant(aTenantId), anAuthorId);

        Post post = discussion.post( author, aSubject, aBodyText);

        this.postRepository().save(post);

        aDiscussionCommandResult.resultingDiscussionId(aDiscussionId);
        aDiscussionCommandResult.resultingPostId(post.getPostId().getId());
    }

    public void postToDiscussionInReplyTo(
            String aTenantId,
            String aDiscussionId,
            String aReplyToPostId,
            String anAuthorId,
            String aSubject,
            String aBodyText,
            DiscussionCommandResult aDiscussionCommandResult) {

        Discussion discussion =
                this.discussionRepository()
                    .discussionOfId(new Tenant(aTenantId),
                            new DiscussionId(aDiscussionId));

        Author author =
                this.collaboratorService().authorFrom(new Tenant(aTenantId), anAuthorId);

        Post post =
                discussion.post(
                        new PostId(aReplyToPostId),
                        author,
                        aSubject,
                        aBodyText);

        this.postRepository().save(post);

        aDiscussionCommandResult.resultingDiscussionId(aDiscussionId);
        aDiscussionCommandResult.resultingPostId(post.getPostId().getId());
        aDiscussionCommandResult.resultingInReplyToPostId(aReplyToPostId);
    }

    public void reopenDiscussion(String aTenantId, String aDiscussionId) {
        Discussion discussion =
                this.discussionRepository()
                    .discussionOfId(new Tenant(aTenantId),
                            new DiscussionId(aDiscussionId));

        discussion.reopen();

        this.discussionRepository().save(discussion);
    }

    private CollaboratorService collaboratorService() {
        return this.collaboratorService;
    }

    private DiscussionRepository discussionRepository() {
        return this.discussionRepository;
    }
    
    private PostRepository postRepository() {
        return this.postRepository;
    }
    
}
