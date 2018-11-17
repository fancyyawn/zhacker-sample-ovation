package top.zhacker.ddd.identity.domain.group;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import top.zhacker.boot.event.publish.DomainEventPublisher;
import top.zhacker.core.model.IdentifiedEntity;
import top.zhacker.boot.registry.DomainRegistry;
import top.zhacker.ddd.identity.domain.group.event.GroupGroupAdded;
import top.zhacker.ddd.identity.domain.group.event.GroupGroupRemoved;
import top.zhacker.ddd.identity.domain.group.event.GroupProvisioned;
import top.zhacker.ddd.identity.domain.group.event.GroupUserAdded;
import top.zhacker.ddd.identity.domain.group.event.GroupUserRemoved;
import top.zhacker.ddd.identity.domain.tenant.TenantId;
import top.zhacker.ddd.identity.domain.user.User;


/**
 * Created by zhacker.
 * Time 2018/6/13 上午11:03
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends IdentifiedEntity {
  
  public static final String ROLE_GROUP_PREFIX = "ROLE-INTERNAL-GROUP: ";
  
  @NotNull(message = "The tenant is required.")
  private TenantId tenantId;
  
  @NotEmpty(message = "Group name is required.")
  @Size(min = 1, max = 100, message = "Group name must be 100 chars or less.")
  private String name;
  
  @NotEmpty(message = "Group desc is required.")
  @Size(min = 1, max = 250, message = "Group desc must be 250 chars or less.")
  private String description;
  
  private Set<GroupMember> groupMembers = new HashSet<>(0);
  
  public Group(TenantId tenantId, String name, String description) {
    this.tenantId = tenantId;
    this.name = name;
    this.description = description;
    validate();
  
    DomainEventPublisher.publish(
        new GroupProvisioned(this.tenantId, this.name)
    );
  }
  
  public void setTenantId(TenantId tenantId) {
    validate("tenantId");
    this.tenantId = tenantId;
  }
  
  
  public void setName(String name) {
    validate("name");
    this.name = name;
  }
  
  public void setDescription(String description) {
    validate("description");
    this.description = description;
  }
  
  protected void setGroupMembers(Set<GroupMember> groupMembers) {
    this.groupMembers = groupMembers;
  }
  
  public boolean isMember(User aUser) {
    this.assertArgumentNotNull(aUser, "User must not be null.");
    this.assertArgumentEquals(this.tenantId, aUser.getTenantId(), "Wrong tenant for this group.");
    this.assertArgumentTrue(aUser.isEnabled(), "User is not enabled.");
    
    boolean isMember =
        this.groupMembers.contains(aUser.toGroupMember());
    
    GroupMemberService groupMemberService = DomainRegistry.service(GroupMemberService.class);
    
    if (isMember) {
      isMember = groupMemberService.confirmUser(this, aUser);
    } else {
      isMember = groupMemberService.isUserInNestedGroup(this, aUser);
    }
    
    return isMember;
  }
  
  public void addGroup(Group aGroup) {
    
    GroupMemberService groupMemberService = DomainRegistry.service(GroupMemberService.class);
  
    this.assertArgumentNotNull(aGroup, "Group must not be null.");
    this.assertArgumentEquals(this.getTenantId(), aGroup.getTenantId(), "Wrong tenant for this group.");
    this.assertArgumentFalse(groupMemberService.isMemberGroup(aGroup, this.toGroupMember()), "Group recurrsion.");
    
    if (this.getGroupMembers().add(aGroup.toGroupMember()) && !this.isInternalGroup()) {
      DomainEventPublisher
          .publish(new GroupGroupAdded(
              this.getTenantId(),
              this.getName(),
              aGroup.getName()));
    }
  }
  
  protected boolean isInternalGroup() {
    return this.isInternalGroup(this.getName());
  }
  
  protected boolean isInternalGroup(String aName) {
    return aName.startsWith(ROLE_GROUP_PREFIX);
  }
  
  
  protected GroupMember toGroupMember() {
    GroupMember groupMember =
        new GroupMember(
            this.getTenantId(),
            this.getName(),
            GroupMemberType.Group);
    
    return groupMember;
  }
  
  public void addUser(User aUser) {
    this.assertArgumentNotNull(aUser, "User must not be null.");
    this.assertArgumentEquals(this.getTenantId(), aUser.getTenantId(), "Wrong tenant for this group.");
    this.assertArgumentTrue(aUser.isEnabled(), "User is not enabled.");
    
    if (this.getGroupMembers().add(aUser.toGroupMember()) && !this.isInternalGroup()) {
      DomainEventPublisher
          .publish(new GroupUserAdded(
              this.getTenantId(),
              this.getName(),
              aUser.getUsername()));
    }
  }
  
  public void removeGroup(Group aGroup) {
    this.assertArgumentNotNull(aGroup, "Group must not be null.");
    this.assertArgumentEquals(this.getTenantId(), aGroup.getTenantId(), "Wrong tenant for this group.");
    
    // not a nested remove, only direct member
    if (this.getGroupMembers().remove(aGroup.toGroupMember()) && !this.isInternalGroup()) {
      DomainEventPublisher
          .publish(new GroupGroupRemoved(
              this.getTenantId(),
              this.getName(),
              aGroup.getName()));
    }
  }
  
  public void removeUser(User aUser) {
    this.assertArgumentNotNull(aUser, "User must not be null.");
    this.assertArgumentEquals(this.getTenantId(), aUser.getTenantId(), "Wrong tenant for this group.");
    
    // not a nested remove, only direct member
    if (this.getGroupMembers().remove(aUser.toGroupMember()) && !this.isInternalGroup()) {
      DomainEventPublisher
          .publish(new GroupUserRemoved(
              this.getTenantId(),
              this.getName(),
              aUser.getUsername()));
    }
  }
  
  
}
