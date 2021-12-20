package com.jianzhi.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jianzhi.model.Enterprise;

public interface EnterpriseService {
	/**
	 * 登录
	 * @param zh
	 * @param mm
	 * @return
	 */
	Map<String, Object> login(String zh,String mm);
	/**
	 * 注销
	 * @param request
	 * @return
	 */
	String logout(HttpServletRequest request);
	
	/**
	 * 根据id查询单位
	 * @param id
	 * @return
	 */
	Enterprise getEnterprise(String id);
	
	/**
	 * 
	 * @param request 获取request中的token
	 * @param oldpassword 获取旧密码
	 * @param newpassword 获取新密码
	 * @return 1修改成功;-1修改失败;-2旧密码输入错误;-3单位账号不存在;-4token不存在未登录
	 */
	int modifyPassword(HttpServletRequest request,String oldpassword,String newpassword);

}
