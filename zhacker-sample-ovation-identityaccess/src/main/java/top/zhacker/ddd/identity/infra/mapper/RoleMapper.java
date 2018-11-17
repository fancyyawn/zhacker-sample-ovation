package top.zhacker.ddd.identity.infra.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

import top.zhacker.ddd.identity.domain.role.Role;


/**
 * Created by zhacker.
 * Time 2018/7/29 上午10:14
 */
@Mapper
public interface RoleMapper {
  
  @Results({
      @Result(column = "tenant_id_id", property = "tenantId.id")
  })
  @Select("select * from tbl_role")
  List<Role> findAll();
}
