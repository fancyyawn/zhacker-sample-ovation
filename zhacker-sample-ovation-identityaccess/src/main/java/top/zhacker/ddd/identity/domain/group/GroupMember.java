package top.zhacker.ddd.identity.domain.group;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import top.zhacker.core.model.IdentifiedEntity;
import top.zhacker.core.model.IdentifiedValueObject;
import top.zhacker.ddd.identity.domain.tenant.TenantId;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午1:53
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMember extends IdentifiedValueObject {
  
  @NotNull(message = "The tenantId must be provided.")
  private TenantId tenantId;
  
  @NotEmpty(message = "Member name is required.")
  @Size(min = 1, max = 100, message = "Member name must be 100 characters or less.")
  private String name;
  
  @NotNull(message = "The type must be provided.")
  private GroupMemberType type;
  
  
  public GroupMember(TenantId tenantId, String name, GroupMemberType type) {
    this();
    this.tenantId = tenantId;
    this.name = name;
    this.type = type;
    validate();
  }
  
  protected void setTenantId(TenantId tenantId) {
    validate("tenantId");
    this.tenantId = tenantId;
  }
  
  protected void setName(String name) {
    validate("name");
    this.name = name;
  }
  
  protected void setType(GroupMemberType type) {
    validate("type");
    this.type = type;
  }
  
  public boolean isGroup(){
    return this.type.isGroup();
  }
  
  public boolean isUser(){
    return this.type.isUser();
  }
  
  
}
