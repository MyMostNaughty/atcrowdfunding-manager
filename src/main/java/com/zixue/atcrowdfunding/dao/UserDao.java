package com.zixue.atcrowdfunding.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zixue.atcrowdfunding.bean.User;

public interface UserDao {

	@Select("select * from t_user")
	List<User> queryAll();

	@Select("select * from t_user where loginacct = #{loginacct} and userpswd = #{userpswd}")
	User queryLogin(User user);

	List<User> pageQueryData(Map<String, Object> map);

	int pageQueryCount(Map<String, Object> map);

	void insertUser(User user);

	@Select("select * from t_user where id = #{id}")
	User queryById(Integer id);

	void updateUser(User user);

	void deleteUserById(Integer id);

	void deleteUsers(Map<String, Object> map);

	void insertUserRoles(Map<String, Object> map);

	void deleteUserRoles(Map<String, Object> map);

	@Select("select roleid from t_user_role where userid = #{id}")
	List<Integer> queryRoleidsByUserid(Integer id);

}
