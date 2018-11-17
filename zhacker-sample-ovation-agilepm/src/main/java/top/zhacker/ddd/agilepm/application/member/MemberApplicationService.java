package top.zhacker.ddd.agilepm.application.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhacker.ddd.agilepm.application.member.command.*;
import top.zhacker.ddd.agilepm.domain.team.TeamRepo;
import top.zhacker.ddd.agilepm.domain.team.member.ProductOwner;
import top.zhacker.ddd.agilepm.domain.team.member.ProductOwnerRepository;
import top.zhacker.ddd.agilepm.domain.team.member.TeamMember;
import top.zhacker.ddd.agilepm.domain.team.member.TeamMemberRepo;
import top.zhacker.ddd.agilepm.domain.tenant.TenantId;
import top.zhacker.boot.leveldb.LevelDbTransactional;

import java.util.Collection;


/**
 * Created by zhacker.
 * Time 2018/7/18 上午11:06
 */
@Service
public class MemberApplicationService {
  
  @Autowired
  private TeamRepo teamRepo;
  
  @Autowired
  private ProductOwnerRepository productOwnerRepo;
  
  @Autowired
  private TeamMemberRepo teamMemberRepo;
  
  
  public Collection<ProductOwner> allProductOwners(String tenantId){
    return productOwnerRepo.allProductOwnersOfTenant(new TenantId(tenantId));
  }
  
  public ProductOwner productOwnerOfIdentity(String tenantId, String username){
    return productOwnerRepo.productOwnerOfIdentity(new TenantId(tenantId), username);
  }
  
  
  public Collection<TeamMember> allTeamMembersOfTenant(String aTenantId){
    return teamMemberRepo.allTeamMembersOfTenant(new TenantId(aTenantId));
  }
  
  public TeamMember teamMemberOfIdentity(String tenantId, String aUsername){
    return teamMemberRepo.teamMemberOfIdentity(new TenantId(tenantId), aUsername);
  }
  
  @LevelDbTransactional
  public void enableProductOwner(EnableProductOwnerCommand command) {
    
    TenantId tenantId = new TenantId(command.getTenantId());
    
    ProductOwner owner = productOwnerRepo.productOwnerOfIdentity(tenantId, command.getUsername());
    if(owner != null){
      owner.enable(command.getOccurredOn());
    }else{
      owner = new ProductOwner(
          tenantId,
          command.getUsername(),
          command.getFirstName(),
          command.getLastName(),
          command.getEmailAddress(),
          command.getOccurredOn()
      );
    }
    productOwnerRepo.save(owner);
  }
  
  @LevelDbTransactional
  public void enableTeamMember(EnableTeamMemberCommand command) {
    
    TenantId tenantId = new TenantId(command.getTenantId());
    
    TeamMember member = teamMemberRepo.teamMemberOfIdentity(tenantId, command.getUsername());
    if(member != null){
      member.enable(command.getOccurredOn());
    }else{
      member = new TeamMember(
          tenantId,
          command.getUsername(),
          command.getFirstName(),
          command.getLastName(),
          command.getEmailAddress(),
          command.getOccurredOn()
      );
    }
    teamMemberRepo.save(member);
  }
  
  @LevelDbTransactional
  public void changeTeamMemberEmailAddress(ChangeTeamMemberEmailAddressCommand aCommand) {
    TenantId tenantId = new TenantId(aCommand.getTenantId());
    
    ProductOwner productOwner =
        this.productOwnerRepo.productOwnerOfIdentity(
            tenantId,
            aCommand.getUsername());
    
    if (productOwner != null) {
      productOwner
          .changeEmailAddress(
              aCommand.getEmailAddress(),
              aCommand.getOccurredOn());
      
      this.productOwnerRepo.save(productOwner);
    }
    
    TeamMember teamMember =
        this.teamMemberRepo.teamMemberOfIdentity(
            tenantId,
            aCommand.getUsername());
    
    if (teamMember != null) {
      teamMember
          .changeEmailAddress(
              aCommand.getEmailAddress(),
              aCommand.getOccurredOn());
      
      this.teamMemberRepo.save(teamMember);
    }
    
  }
  
  @LevelDbTransactional
  public void changeTeamMemberName(ChangeTeamMemberNameCommand aCommand) {
    TenantId tenantId = new TenantId(aCommand.getTenantId());
    
    ProductOwner productOwner =
        this.productOwnerRepo.productOwnerOfIdentity(
            tenantId,
            aCommand.getUsername());
    
    if (productOwner != null) {
      productOwner
          .changeName(
              aCommand.getFirstName(),
              aCommand.getLastName(),
              aCommand.getOccurredOn());
      
      this.productOwnerRepo.save(productOwner);
    }
    
    TeamMember teamMember =
        this.teamMemberRepo.teamMemberOfIdentity(
            tenantId,
            aCommand.getUsername());
    
    if (teamMember != null) {
      teamMember
          .changeName(
              aCommand.getFirstName(),
              aCommand.getLastName(),
              aCommand.getOccurredOn());
      
      this.teamMemberRepo.save(teamMember);
    }
  }
  
  @LevelDbTransactional
  public void disableProductOwner(DisableProductOwnerCommand command) {
    TenantId tenantId = new TenantId(command.getTenantId());
    
    ProductOwner productOwner =
        this.productOwnerRepo.productOwnerOfIdentity(
            tenantId,
            command.getUsername());
    
    if (productOwner != null) {
      productOwner.disable(command.getOccurredOn());
      
      this.productOwnerRepo.save(productOwner);
    }
  }
  
  @LevelDbTransactional
  public void disableTeamMember(DisableTeamMemberCommand command) {
    
    TenantId tenantId = new TenantId(command.getTenantId());
    
    TeamMember teamMember =
        this.teamMemberRepo.teamMemberOfIdentity(
            tenantId,
            command.getUsername());
    
    if (teamMember != null) {
      teamMember.disable(command.getOccurredOn());
      
      this.teamMemberRepo.save(teamMember);
    }
  }
}
