package com.zixue.atcrowdfunding.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zixue.atcrowdfunding.bean.Permission;
import com.zixue.atcrowdfunding.bean.User;
import com.zixue.atcrowdfunding.dao.PermissionDao;
import com.zixue.atcrowdfunding.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionDao permissionDao;

	@Override
	public Permission queryRootpermission() {
		return permissionDao.queryRootpermission();
	}

	@Override
	public List<Permission> queryChildPermissions(Integer pid) {
		return permissionDao.queryChildPermissions(pid);
	}

	@Override
	public List<Permission> queryAll() {
		return permissionDao.queryAll();
	}

	@Override
	public Map<Integer, Permission> queryAllMap() {
		return permissionDao.queryAllMap();
	}

	@Override
	public void insertPermission(Permission permission) {
		permissionDao.insertPermission(permission);
	}

	@Override
	public Permission queryRootpermissionById(Integer id) {
		return permissionDao.queryRootpermissionById(id);
	}

	@Override
	public void updatePermission(Permission permission) {
		permissionDao.updatePermission(permission);
	}

	@Override
	public void deletePermissionById(Integer id) {
		permissionDao.deleteRootpermissionById(id);
		
	}

	@Override
	public List<Integer>  queryPermissionByRoleid(Integer roleid) {
		return permissionDao.queryPermissionByRoleid(roleid);
	}

	@Override
	public Map<Integer, Permission> queryPermissionMapByUser(User dbuser) {
		return permissionDao.queryPermissionMapByUser(dbuser);
	}

}
