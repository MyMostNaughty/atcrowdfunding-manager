package com.zixue.atcrowdfunding.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zixue.atcrowdfunding.bean.AjaxResult;
import com.zixue.atcrowdfunding.bean.Page;
import com.zixue.atcrowdfunding.bean.Role;
import com.zixue.atcrowdfunding.bean.User;
import com.zixue.atcrowdfunding.service.RoleService;
import com.zixue.atcrowdfunding.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService poleService;
	
	@ResponseBody
	@RequestMapping("deleteUsers")
	public Object deleteUsesr(Integer[] userid){
		AjaxResult result = new AjaxResult();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("userids", userid);
			userService.deleteUsers(map);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("deleteUser")
	public Object deleteUser(Integer id){
		AjaxResult result = new AjaxResult();
		try {
			userService.deleteUserById(id);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			e.printStackTrace();
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("updateUser")
	public Object updateUser(User user){
		AjaxResult result = new AjaxResult();
		try {
			userService.updateUser(user);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	
	@RequestMapping("edit")
	public String edit(Integer id, Model model){
		User user = userService.queryById(id);
		model.addAttribute("user", user);
		return "user/edit";
	}
	
	@RequestMapping("assign")
	public String assign(Integer id, Model model){
		User user = userService.queryById(id);
		model.addAttribute("user", user);
		
		List<Role> roles = poleService.queryAll();
		model.addAttribute("roles", roles);
		
		List<Role> assingedRoles = new ArrayList<>();
		List<Role> unassingedRoles = new ArrayList<>();
		
		// 获取关系表的数据
		List<Integer> roleids = userService.queryRoleidsByUserid(id);
		
		for (Role role : roles) {
			if(roleids.contains(role.getId())){
				assingedRoles.add(role);
			}else{
				unassingedRoles.add(role);
			}
		}
		
		model.addAttribute("assingedRoles", assingedRoles);    // 已分配
		model.addAttribute("unassingedRoles", unassingedRoles);// 未分配
		return "user/assign";
	}
	
	@ResponseBody
	@RequestMapping("/doAssign")
	public Object doAssign(Integer userid, Integer[] unassignroleids){
		AjaxResult result = new AjaxResult();
		try{
			// 增加关系表数据
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("userid", userid);
			map.put("roleids", unassignroleids);
			userService.insertUserRoles(map);
			result.setSuccess(true);
		}catch (Exception e) {
			result.setSuccess(false);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/dounAssign")
	public Object dounAssign(Integer userid, Integer[] assignroleids){
		AjaxResult result = new AjaxResult();
		try{
			// 删除关系表数据
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("userid", userid);
			map.put("roleids", assignroleids);
			userService.deleteUserRoles(map);
			result.setSuccess(true);
		}catch (Exception e) {
			result.setSuccess(false);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping("insert")
	public Object insert(User user){
		AjaxResult result = new AjaxResult();
		try {
			user.setUserpswd("123456");
			user.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			userService.insertUser(user);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		return result;
	}
	
	@RequestMapping("/add")
	public String add(){
		return "user/add";
	}
	
	
	@RequestMapping("/index")
	public String index(){
		return "user/index";
	}
	
	@ResponseBody
	@RequestMapping("/pageQuery")
	public Object pageQuery(Integer pageno, Integer pagesize, String queryText){
		AjaxResult result = new AjaxResult();
		try {
			// 1.1查询sql所需条件  ,封装map中
			Map<String , Object> map = new HashMap<String , Object>();
			map.put("start", (pageno-1)*pagesize);
			map.put("size", pagesize);
			map.put("queryText", queryText);
			// 1.2当前分页数据
			List<User> users = userService.pageQueryData(map);
			
			// 2.1最大页码判断逻辑
			int totalsize = userService.pageQueryCount(map);  // 总条数
			int totalno = 0;    // 最大页码(总页码)
			if(totalsize % pagesize == 0){
				totalno = totalsize / pagesize;
			}else{
				totalno = totalsize / pagesize + 1;
			}
			
			// 3.分页对象
			Page<User> userPage = new Page<>();
			userPage.setDatas(users);
			userPage.setPageno(pageno);
			userPage.setTotalno(totalno);
			userPage.setTotalsize(totalsize);
			
			result.setSuccess(true);
			result.setData(userPage);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		
		return result;
	}
	
	/*
	 * 用户首页
	 */
	@RequestMapping("/index1")
	public String index1(@RequestParam(required=false,defaultValue="1")Integer pageno,@RequestParam(required=false,defaultValue="2")Integer pagesize, Model model) {
		// 分页查询
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("start", (pageno-1)*pagesize);
		map.put("size", pagesize);
		List<User> users = userService.pageQueryData(map);
		model.addAttribute("users", users);
		// 当前页码
		model.addAttribute("pageno",pageno);
		// 最大页码判断逻辑
		int totalsize = userService.pageQueryCount(map);  // 总条数
		int totalno = 0;    // 最大页码(总页码)
		if(totalsize % pagesize == 0){
			totalno = totalsize / pagesize;
		}else{
			totalno = totalsize / pagesize + 1;
		}
		model.addAttribute("totalno",totalno);// 最大页码
		return "user/index2";
	}
}
