package com.zixue.atcrowdfunding.service;

import java.util.List;
import java.util.Map;

import com.zixue.atcrowdfunding.bean.Permission;
import com.zixue.atcrowdfunding.bean.User;

public interface PermissionService {

	Permission queryRootpermission();

	List<Permission> queryChildPermissions(Integer id);

	List<Permission> queryAll();
	
	Map<Integer, Permission> queryAllMap();

	void insertPermission(Permission permission);

	Permission queryRootpermissionById(Integer id);

	void updatePermission(Permission permission);

	void deletePermissionById(Integer id);

	List<Integer> queryPermissionByRoleid(Integer roleid);

	Map<Integer, Permission> queryPermissionMapByUser(User dbuser);


}
