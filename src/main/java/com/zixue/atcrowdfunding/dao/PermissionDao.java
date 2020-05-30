package com.zixue.atcrowdfunding.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

import com.zixue.atcrowdfunding.bean.Permission;

public interface PermissionDao {

	@Select("select * from t_permission where pid is null")
	Permission queryRootpermission();

	@Select("select * from t_permission where pid = #{pid}")
	List<Permission> queryChildPermissions(Integer pid);

	@Select("select * from t_permission ")
	List<Permission> queryAll();

	@MapKey("id")
	@Select("select * from t_permission ")
	Map<Integer, Permission> queryAllMap();

}
