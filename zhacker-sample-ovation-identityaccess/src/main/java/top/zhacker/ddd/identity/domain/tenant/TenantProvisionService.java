package top.zhacker.ddd.identity.domain.tenant;

import org.springframework.stereotype.Service;

import lombok.val;
import top.zhacker.boot.event.publish.DomainEventPublisher;
//import top.zhacker.boot.registry.DomainRegistry;
import top.zhacker.boot.registry.DomainRegistry;
import top.zhacker.ddd.identity.domain.role.Role;
import top.zhacker.ddd.identity.domain.tenant.event.TenantAdministratorRegistered;
import top.zhacker.ddd.identity.domain.tenant.event.TenantProvisioned;
import top.zhacker.ddd.identity.domain.user.Enablement;
import top.zhacker.ddd.identity.domain.user.PasswordService;
import top.zhacker.ddd.identity.domain.user.User;
import top.zhacker.ddd.identity.domain.user.person.ContactInformation;
import top.zhacker.ddd.identity.domain.user.person.EmailAddress;
import top.zhacker.ddd.identity.domain.user.person.FullName;
import top.zhacker.ddd.identity.domain.user.person.Person;
import top.zhacker.ddd.identity.domain.user.person.PostalAddress;
import top.zhacker.ddd.identity.domain.user.person.Telephone;
import top.zhacker.ddd.identity.domain.role.RoleRepo;
import top.zhacker.ddd.identity.domain.user.UserRepo;


/**
 * Created by zhacker.
 * Time 2018/6/13 上午11:28
 */
@Service
public class TenantProvisionService {
  
  private final TenantRepo tenantRepo;
  private final UserRepo userRepo;
  private final RoleRepo roleRepo;
  
  public TenantProvisionService(TenantRepo tenantRepo, UserRepo userRepo, RoleRepo roleRepo) {
    this.tenantRepo = tenantRepo;
    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
  }
  
  public Tenant provisionTenant(
      String aTenantName,
      String aTenantDescription,
      FullName anAdministorName,
      EmailAddress anEmailAddress,
      PostalAddress aPostalAddress,
      Telephone aPrimaryTelephone,
      Telephone aSecondaryTelephone) {
    try{
      Tenant tenant = new Tenant(
          this.tenantRepo.nextIdentity(),
          aTenantName,
          aTenantDescription,
          true
      );
      this.tenantRepo.add(tenant);
      
      this.registerAdministratorFor(
          tenant,
          anAdministorName,
          anEmailAddress,
          aPostalAddress,
          aPrimaryTelephone,
          aSecondaryTelephone
      );
  
      DomainEventPublisher.publish(new TenantProvisioned(tenant.getTenantId()));

      return tenant;
      
    }catch (Throwable t){
      throw new IllegalStateException(
          "Cannot provision tenant because: " + t.getMessage());
    }
  }
  
  private void registerAdministratorFor(
      Tenant aTenant,
      FullName anAdministorName,
      EmailAddress anEmailAddress,
      PostalAddress aPostalAddress,
      Telephone aPrimaryTelephone,
      Telephone aSecondaryTelephone) {
  
    val invitation = aTenant.offerRegistrationInvitation("init").openEnded();
    
    String strongPassword = DomainRegistry.service(PasswordService.class).generateStrongPassword();
    
    User admin = aTenant.registerUser(
        invitation.getInvitationId(),
        "admin",
        strongPassword,
        Enablement.indefiniteEnablement(),
        new Person(
            aTenant.getTenantId(),
            anAdministorName,
            new ContactInformation(
                anEmailAddress,
                aPostalAddress,
                aPrimaryTelephone,
                aSecondaryTelephone
            )
        )
    );
    
    
    aTenant.withdrawInvitation(invitation.getInvitationId());
  
    userRepo.save(admin);
    
  
    Role adminRole = aTenant.provisionRole(
        "Adminitrator",
        "Default "+ aTenant.getName() + " administrator"
    );
    
    //assign user
    adminRole.assignUser(admin);
    
    this.roleRepo.add(adminRole);
    
    DomainEventPublisher.publish(
        new TenantAdministratorRegistered(
            aTenant.getTenantId(),
            aTenant.getName(),
            anAdministorName,
            anEmailAddress,
            admin.getUsername(),
            strongPassword
        )
    );
    
  }
  
}
