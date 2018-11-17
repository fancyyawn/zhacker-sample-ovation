package top.zhacker.ddd.agilepm.port.message.listener;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;


/**
 * Created by zhacker.
 * Time 2018/7/15 下午2:40
 */
public interface ListeningChannels {
  
  String BacklogItemCommitted = "top.zhacker.ddd.agilepm.domain.product.blacklogitem.event.BacklogItemCommitted";
  
  @Input(BacklogItemCommitted)
  SubscribableChannel backlogItemCommitted();
  
  String BacklogItemScheduled = "top.zhacker.ddd.agilepm.domain.product.blacklogitem.event.BacklogItemScheduled";
  
  @Input(BacklogItemScheduled)
  SubscribableChannel backlogItemScheduled();
  
  
  String ProductBacklogItemPlanned = "top.zhacker.ddd.agilepm.domain.product.event.ProductBacklogItemPlanned";
  
  @Input(ProductBacklogItemPlanned)
  SubscribableChannel productBacklogItemPlanned();
  
  
  String UserAssignedToRole = "top.zhacker.ddd.identity.domain.role.event.UserAssignedToRole";
  
  @Input(UserAssignedToRole)
  SubscribableChannel userAssignedToRole();
  
  String PersonNameChanged = "top.zhacker.ddd.identity.domain.user.person.event.PersonNameChanged";
  
  @Input(PersonNameChanged)
  SubscribableChannel personNameChanged();
  
  
  String PersonContactInformationChanged = "top.zhacker.ddd.identity.domain.user.person.event.PersonContactInformationChanged";
  
  @Input(PersonContactInformationChanged)
  SubscribableChannel personContactInformationChanged();
  
  String UserUnassignedFromRole = "top.zhacker.ddd.identity.domain.role.event.UserUnassignedFromRole";
  
  @Input(UserUnassignedFromRole)
  SubscribableChannel userUnassignedFromRole();
}
