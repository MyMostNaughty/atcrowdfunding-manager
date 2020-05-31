package com.zixue.atcrowdfunding.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

import com.zixue.atcrowdfunding.bean.Permission;
import com.zixue.atcrowdfunding.bean.User;

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

	void insertPermission(Permission permission);

	@Select("select * from t_permission where id = #{id}")
	Permission queryRootpermissionById(Integer id);

	void updatePermission(Permission permission);

	@Delete("delete from t_permission where id = #{id}")
	void deleteRootpermissionById(Integer id);

	@Select("select permissionid from t_role_permission where roleid = #{roleid}")
	List<Integer> queryPermissionByRoleid(Integer roleid);

	@MapKey("id")
//	@Select("select * "+
//		   "from t_permission "+
//		"where id in ( "+
//		    "select permissionid "+ 
//		       "from t_role_permission "+ 
//		    "where roleid in ( "+
//		    	"select roleid "+
//		    	   "from t_user_role "+
//		    	"where userid = #{id} "+
//		    ") "+
//		")")
	Map<Integer, Permission> queryPermissionMapByUser(User dbuser);

}
