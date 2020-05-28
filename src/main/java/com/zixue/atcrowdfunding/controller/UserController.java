package com.zixue.atcrowdfunding.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.cmd.AddCommentCmd;
import org.apache.poi.ss.formula.functions.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zixue.atcrowdfunding.bean.AjaxResult;
import com.zixue.atcrowdfunding.bean.Page;
import com.zixue.atcrowdfunding.bean.User;
import com.zixue.atcrowdfunding.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
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
