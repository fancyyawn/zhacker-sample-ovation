package top.zhacker.ddd.identity.domain.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Optional;

import top.zhacker.ddd.identity.domain.user.User;
import top.zhacker.ddd.identity.domain.user.UserRepo;


/**
 * Created by zhacker.
 * Time 2018/6/30 下午4:38
 */
@Component
public class GroupMemberService {
  
  private GroupRepo groupRepo;
  
  private UserRepo userRepo;
  
  @Autowired
  public GroupMemberService(GroupRepo groupRepo, UserRepo userRepo) {
    this.groupRepo = groupRepo;
    this.userRepo = userRepo;
  }
  
  public boolean confirmUser(Group group, User user){
    return Optional.ofNullable(this.userRepo.userWithUsername(group.getTenantId(), user.getUsername()))
        .filter(User::isEnabled)
        .isPresent();
  }
  
  public boolean isMemberGroup(Group group, GroupMember groupMember){
    boolean isMember = false;
  
    Iterator<GroupMember> iter =
        group.getGroupMembers().iterator();
  
    while (!isMember && iter.hasNext()) {
      GroupMember member = iter.next();
      if (member.isGroup()) {
        if (groupMember.equals(member)) {
          isMember = true;
        } else {
          Group groupGroup = this.groupRepo
                  .groupNamed(member.getTenantId(), member.getName());
          if (groupGroup != null) {
            isMember = this.isMemberGroup(groupGroup, groupMember);
          }
        }
      }
    }
  
    return isMember;
  }
  
  public boolean isUserInNestedGroup(Group aGroup, User user){
    boolean isInNestedGroup = false;
  
    Iterator<GroupMember> iter =
        aGroup.getGroupMembers().iterator();
  
    while (!isInNestedGroup && iter.hasNext()) {
      GroupMember member = iter.next();
      if (member.isGroup()) {
        Group group =
            this.groupRepo
                .groupNamed(member.getTenantId(), member.getName());
        if (group != null) {
          isInNestedGroup = group.isMember(user);
        }
      }
    }
  
    return isInNestedGroup;
  }
  
}
