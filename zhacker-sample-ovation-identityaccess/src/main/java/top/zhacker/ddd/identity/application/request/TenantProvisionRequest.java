package top.zhacker.ddd.identity.application.request;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * Created by zhacker.
 * Time 2018/6/14 下午6:57
 */
@Data
@Accessors(chain = true)
public class TenantProvisionRequest {
  
  private String tenantName;
  private String tenantDescription;
  private String administorFirstName;
  private String administorLastName;
  private String emailAddress;
  private String primaryTelephone;
  private String secondaryTelephone;
  private String addressStreetAddress;
  private String addressCity;
  private String addressStateProvince;
  private String addressPostalCode;
  private String addressCountryCode;
}
