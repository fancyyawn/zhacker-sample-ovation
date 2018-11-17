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

package top.zhacker.ddd.agilepm.domain.discussion;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import top.zhacker.core.model.ValueObject;

@ToString
@EqualsAndHashCode(callSuper = false)
public class DiscussionDescriptor extends ValueObject {

    public static final String UNDEFINED_ID = "UNDEFINED";

    @Getter
    private String id;
    
    private DiscussionDescriptor() {
        super();
    }
    
    
    public DiscussionDescriptor(String anId) {
        this();

        this.setId(anId);
    }

    public DiscussionDescriptor(DiscussionDescriptor aDiscussionDescriptor) {
        this(aDiscussionDescriptor.getId());
    }
    

    public boolean isUndefined() {
        return this.getId().equals(UNDEFINED_ID);
    }
    
    
    private void setId(String anId) {
        this.assertArgumentNotEmpty(anId, "The discussion identity must be provided.");
        this.assertArgumentLength(anId, 36, "The discussion identity must be 36 characters or less.");

        this.id = anId;
    }
}
