package com.memo.user.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.memo.user.model.User;

@Repository
public interface UserDAO {
	
	public boolean existLoginId(String loginId);
	
	public void insertUser(
			@Param("loginId") String loginId,
			@Param("password") String password, 
			@Param("name") String name,
			@Param("email") String email);
	
	public User selectUSerByLoginIdPassword(
			@Param("loginId") String loginId,
			@Param("password") String password);

}
