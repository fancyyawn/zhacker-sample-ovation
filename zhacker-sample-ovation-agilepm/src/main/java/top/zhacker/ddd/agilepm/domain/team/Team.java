package top.zhacker.ddd.agilepm.domain.team;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import top.zhacker.core.model.IdentifiedEntity;
import top.zhacker.ddd.agilepm.domain.team.member.ProductOwner;
import top.zhacker.ddd.agilepm.domain.team.member.TeamMember;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * Created by zhacker.
 * Time 2018/6/18 下午10:13
 */
@ToString
@EqualsAndHashCode(of = {"tenantId", "name"}, callSuper = false)
@Getter
public class Team extends IdentifiedEntity {
  
  private TenantId tenantId;
  private String name;
  private ProductOwner productOwner;
  private Set<TeamMember> teamMembers;
  
  private Team() {
    super();
    this.setTeamMembers(new HashSet<TeamMember>(0));
  }
  
  
  public Team(TenantId tenantId, String name) {
    this();
    this.setTenantId(tenantId);
    this.setName(name);
  }
  
  
  public Team(TenantId tenantId, String name, ProductOwner productOwner) {
    this();
    this.setTenantId(tenantId);
    this.setName(name);
    this.setProductOwner(productOwner);
  }
  
  
  public void setTenantId(TenantId tenantId) {
    this.assertArgumentNotNull(tenantId, "The tenantId must be provided.");
    
    this.tenantId = tenantId;
  }
  
  
  public void setName(String name) {
    this.assertArgumentNotEmpty(name, "The name must be provided.");
    this.assertArgumentLength(name, 100, "The name must be 100 characters or less.");
  
    this.name = name;
  }
  
  
  public void setProductOwner(ProductOwner productOwner) {
    this.assertArgumentNotNull(productOwner, "The productOwner must be provided.");
    this.assertArgumentEquals(this.getTenantId(), productOwner.getTenantId(), "The productOwner must be of the same tenant.");
  
    this.productOwner = productOwner;
  }
  
  
  public void setTeamMembers(Set<TeamMember> teamMembers) {
    this.teamMembers = teamMembers;
  }
  
  public Set<TeamMember> allTeamMembers() {
    return Collections.unmodifiableSet(this.getTeamMembers());
  }
  
  public void assignProductOwner(ProductOwner aProductOwner) {
    this.assertArgumentEquals(this.getTenantId(), aProductOwner.getTenantId(), "Product owner must be of the same tenant.");
    
    this.setProductOwner(aProductOwner);
  }
  
  public void assignTeamMember(TeamMember aTeamMember) {
    this.assertArgumentEquals(this.getTenantId(), aTeamMember.getTenantId(), "Team member must be of the same tenant.");
    
    this.getTeamMembers().add(aTeamMember);
  }
  
  public boolean isTeamMember(TeamMember aTeamMember) {
    this.assertArgumentEquals(this.getTenantId(), aTeamMember.getTenantId(), "Team member must be of the same tenant.");
    
    return this.getTeamMembers().stream().anyMatch(member-> Objects.equals(aTeamMember.getUsername(), member.getUsername()));
  }
  
  public void removeTeamMember(TeamMember aTeamMember) {
    this.assertArgumentEquals(this.getTenantId(), aTeamMember.getTenantId(), "Team member must be of the same tenant.");
    
    TeamMember memberToRemove = null;
    String usernameToMatch = aTeamMember.getUsername();
    
    for (TeamMember member : this.getTeamMembers()) {
      if (member.getUsername().equals(usernameToMatch)) {
        memberToRemove = member;
        break;
      }
    }
    
    this.getTeamMembers().remove(memberToRemove);
  }
}
