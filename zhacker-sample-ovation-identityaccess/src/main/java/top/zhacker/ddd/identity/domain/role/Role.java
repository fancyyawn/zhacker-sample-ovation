package top.zhacker.ddd.identity.domain.role;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import top.zhacker.boot.event.publish.DomainEventPublisher;
import top.zhacker.core.model.IdentifiedEntity;
import top.zhacker.ddd.identity.domain.group.Group;
import top.zhacker.ddd.identity.domain.role.event.GroupAssignedToRole;
import top.zhacker.ddd.identity.domain.role.event.GroupUnassignedFromRole;
import top.zhacker.ddd.identity.domain.role.event.RoleProvisioned;
import top.zhacker.ddd.identity.domain.role.event.UserAssignedToRole;
import top.zhacker.ddd.identity.domain.role.event.UserUnassignedFromRole;
import top.zhacker.ddd.identity.domain.tenant.TenantId;
import top.zhacker.ddd.identity.domain.user.User;


/**
 * Created by zhacker.
 * Time 2018/6/13 上午11:04
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends IdentifiedEntity {
  
  @NotNull(message = "The tenantId is required.")
  private TenantId tenantId;
  
  @NotEmpty(message = "Role name must be provided.")
  @Size(min = 1, max = 250, message = "Role name must be 100 characters or less.")
  private String name;
  
  @NotEmpty(message = "Role description is required.")
  @Size(min = 1, max = 250, message = "Role description must be 250 characters or less.")
  private String description;
  
  private boolean supportsNesting = true;
  
  private Group group;
  
  public RoleSimple toSimple(){
    return new RoleSimple()
        .setTenantId(tenantId)
        .setName(name)
        .setDescription(description)
        .setSupportsNesting(supportsNesting);
  }
  
  public Role(TenantId tenantId, String name, String description) {
    this(tenantId, name, description, false);
  }
  
  
  public Role(
      TenantId aTenantId,
      String aName,
      String aDescription,
      boolean aSupportsNesting) {
    
    this();
    
    this.setDescription(aDescription);
    this.setName(aName);
    this.setSupportsNesting(aSupportsNesting);
    this.setTenantId(aTenantId);
    
    this.createInternalGroup();
  
    DomainEventPublisher.publish(
        new RoleProvisioned(this.tenantId, this.name)
    );
  }
  
  
  protected void createInternalGroup() {
    String groupName =
        Group.ROLE_GROUP_PREFIX
            + UUID.randomUUID().toString().toUpperCase();
    
    this.setGroup(new Group(
        this.tenantId,
        groupName,
        "Role backing group for: " + this.name));
  }
  
  
  public void assignGroup(Group group){
    this.assertStateTrue(this.supportsNesting, "This role does not support group nesting.");
    this.assertArgumentNotNull(group, "Group must not be null.");
    this.assertArgumentEquals(this.tenantId, group.getTenantId(), "Wrong tenant for this group.");
  
    this.group.addGroup(group);
  
    DomainEventPublisher
        .publish(new GroupAssignedToRole(
            this.tenantId,
            this.name,
            group.getName()));
  }
  
  public void assignUser(User aUser){
    this.assertArgumentNotNull(aUser, "User must not be null.");
    this.assertArgumentEquals(this.getTenantId(), aUser.getTenantId(), "Wrong tenant for this user.");
  
    this.group.addUser(aUser);
  
    // NOTE: Consider what a consuming Bounded Context would
    // need to do if this event was not enriched with the
    // last three user person properties. (Hint: A lot.)
    DomainEventPublisher
        .publish(new UserAssignedToRole(
            this.getTenantId(),
            this.getName(),
            aUser.getUsername(),
            aUser.getPerson().name().firstName(),
            aUser.getPerson().name().lastName(),
            aUser.getPerson().emailAddress().getAddress()));
  }
  
  public void unassignGroup(Group aGroup){
    this.assertStateTrue(this.supportsNesting, "This role does not support group nesting.");
    this.assertArgumentNotNull(aGroup, "Group must not be null.");
    this.assertArgumentEquals(this.getTenantId(), aGroup.getTenantId(), "Wrong tenant for this group.");
  
    this.group.removeGroup(aGroup);
  
    DomainEventPublisher
        .publish(new GroupUnassignedFromRole(
            this.getTenantId(),
            this.getName(),
            aGroup.getName()));
  }
  
  public void unassignUser(User aUser){
    this.assertArgumentNotNull(aUser, "User must not be null.");
    this.assertArgumentEquals(this.getTenantId(), aUser.getTenantId(), "Wrong tenant for this user.");
  
    this.group.removeUser(aUser);
  
    DomainEventPublisher
        .publish(new UserUnassignedFromRole(
            this.getTenantId(),
            this.getName(),
            aUser.getUsername()));
  }
  
  
  public void setTenantId(TenantId tenantId) {
    this.tenantId = tenantId;
  }
  
  
  public void setName(String name) {
    this.name = name;
  }
  
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  
  public void setSupportsNesting(boolean supportsNesting) {
    this.supportsNesting = supportsNesting;
  }
  
  
  public void setGroup(Group group) {
    this.group = group;
  }
  
  
  public boolean isInRole(User aUser) {
    return this.group.isMember(aUser);
  }
  
}
