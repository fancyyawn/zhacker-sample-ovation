package top.zhacker.ddd.identity.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import top.zhacker.core.model.AssertionConcern;
import top.zhacker.ddd.identity.domain.tenant.Tenant;
import top.zhacker.ddd.identity.domain.tenant.TenantId;
import top.zhacker.ddd.identity.domain.tenant.TenantRepo;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午10:32
 */
@Component
public class AuthenticationService extends AssertionConcern {
  
  private EncryptionService encryptionService;
  private TenantRepo tenantRepo;
  private UserRepo userRepo;
  
  @Autowired
  public AuthenticationService(EncryptionService encryptionService, TenantRepo tenantRepo, UserRepo userRepo) {
    this.encryptionService = encryptionService;
    this.tenantRepo = tenantRepo;
    this.userRepo = userRepo;
  }
  
  public UserDescriptor authenticate(
      TenantId aTenantId,
      String aUsername,
      String aPassword) {
    
    this.assertArgumentNotNull(aTenantId, "TenantId must not be null.");
    this.assertArgumentNotEmpty(aUsername, "Username must be provided.");
    this.assertArgumentNotEmpty(aPassword, "Password must be provided.");
    
    UserDescriptor userDescriptor = UserDescriptor.nullDescriptorInstance();
    
    Tenant tenant = this.tenantRepo.findByTenantId(aTenantId);
    
    if (tenant != null && tenant.isActive()) {
      String encryptedPassword = this.encryptionService.encryptedValue(aPassword);
      
      User user =
          this.userRepo
              .userFromAuthenticCredentials(
                  aTenantId,
                  aUsername,
                  encryptedPassword);
      
      if (user != null && user.isEnabled()) {
        userDescriptor = user.userDescriptor();
      }
    }
    
    return userDescriptor;
  }
  
}
