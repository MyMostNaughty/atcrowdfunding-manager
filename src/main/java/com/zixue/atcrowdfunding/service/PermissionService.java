package com.zixue.atcrowdfunding.service;

import java.util.List;
import java.util.Map;

import com.zixue.atcrowdfunding.bean.Permission;

public interface PermissionService {

	Permission queryRootpermission();

	List<Permission> queryChildPermissions(Integer id);

	List<Permission> queryAll();
	
	Map<Integer, Permission> queryAllMap();

}
