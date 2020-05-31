package com.zixue.atcrowdfunding.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zixue.atcrowdfunding.bean.AjaxResult;
import com.zixue.atcrowdfunding.bean.Permission;
import com.zixue.atcrowdfunding.service.PermissionService;

@Controller
@RequestMapping("/permission")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;
	
	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(Integer id){
		AjaxResult result = new AjaxResult();
		
		try {
			permissionService.deletePermissionById(id);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	
	@RequestMapping("/edit")
	public String edit(Integer id, Model model){
		Permission permission = permissionService.queryRootpermissionById(id);
		model.addAttribute("permission",permission);
		return "permission/edit";
	}
	
	@RequestMapping("/add")
	public String add(){
		return "permission/add";
	}
	
	@ResponseBody
	@RequestMapping("/update")
	public Object update(Permission permission){
		AjaxResult result = new AjaxResult();
		
		try {
			permissionService.updatePermission(permission);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/insert")
	public Object insert(Permission permission){
		AjaxResult result = new AjaxResult();
		
		try {
			permissionService.insertPermission(permission);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	
	@RequestMapping("/index")
	public String index(){
		return "permission/index";
	}
	
	@ResponseBody
	@RequestMapping("/loadAssignData")
	public Object loadAssignData(Integer roleid){
		List<Permission> permissions = new ArrayList<Permission>();
		// 获取当前角色已经分配的许可信息
		List<Integer> permissionids = permissionService.queryPermissionByRoleid(roleid);
		Map<Integer, Permission> permissionMap = permissionService.queryAllMap();
		for (Permission p : permissionMap.values()) {
			if(permissionids.contains(p.getId())){
				p.setChecked(true);
			}
			Permission child = p;
			if(child.getPid() == 0){
				permissions.add(p);
			}else{
				Permission parent = permissionMap.get(child.getPid());
				parent.getChildren().add(child);
			}
		}
		return permissions;
	}
	@ResponseBody
	@RequestMapping("/loadData")
	public Object loadData(){
		List<Permission> permissions = new ArrayList<Permission>();
		
		//1. 测试--模拟数据
//		Permission root = new Permission();
//		root.setName("顶级节点");
//		Permission child = new Permission();
//		child.setName("子节点");
//		root.getChildren().add(child);
//		permissions.add(root);
		
		//2. 读取数据库数据
		/*
		Permission root = permissionService.queryRootpermission();
		List<Permission> childPermissions = permissionService.queryChildPermissions(root.getId());
		for (Permission childPermission : childPermissions) {
			List<Permission> childChildPermissions = permissionService.queryChildPermissions(childPermission.getId());
			childPermission.setChildren(childChildPermissions);
		}
		root.setChildren(childPermissions);
		permissions.add(root);
		*/
		
		//3. 递归查询(多次查询数据库，效率低下)
		/*
		Permission parent = new Permission();
		parent.setId(0);
		queryChildPermission(parent);
		return parent.getChildren();
		*/
		
		// 4.查询所有的许可数据（效率比递归高）
//		List<Permission> ps  = permissionService.queryAll();
		/*
		for (Permission p : ps) {
			// 子节点
			Permission child = p;
			if(p.getPid() == 0){
				permissions.add(p);
			}else{
				for (Permission innerPermission : ps) {
					if(child.getPid().equals(innerPermission.getId())){
						// 父节点
						Permission parent = innerPermission;
						// 组合父子节点关系
						parent.getChildren().add(child);
						break;
					}
				}
			}
		}
		*/
		
		// 5.进一步提升效率
		Map<Integer, Permission> permissionMap = permissionService.queryAllMap();
		for (Permission p : permissionMap.values()) {
			Permission child = p;
			if(child.getPid() == 0){
				permissions.add(p);
			}else{
				Permission parent = permissionMap.get(child.getPid());
				parent.getChildren().add(child);
			}
		}
		return permissions;
	}
	
	/**
	 * 递归查询许可信息
	 * 1）方法可以自己调用自己
	 * 2）方法一定要存在跳出逻辑
	 * 3）方法调用时，参数之间应该有规律
	 * 4）递归算法，效率比较低
	 * @Description 
	 * @author hp
	 * @date 2020年5月31日  上午3:30:43
	 * @param parent
	 */
	private void queryChildPermission(Permission parent){
		List<Permission> childPermissions = permissionService.queryChildPermissions(parent.getId());
		if(childPermissions.size()>0){
			for (Permission permission : childPermissions) {
				queryChildPermission(permission);
			}
			parent.setChildren(childPermissions);
		}
	}
}
