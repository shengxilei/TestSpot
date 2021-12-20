package com.jianzhi.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jianzhi.annotation.MyLog;
import com.jianzhi.service.EnterpriseService;
import com.jianzhi.util.R;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.client.RestTemplate;

@Api(value = "登录登出")
@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/login")
public class LoginController {


	@Autowired
    private EnterpriseService enterpriseServiceImpl;

	@MyLog(value = "用户登录登录")  //这里添加了AOP的自定义注解
    @PostMapping({"/", ""})
	@ApiOperation(value = "登录", notes = "")
    public Map<String,Object> login(@ApiParam(value = "用户名" , required=true )String username,@ApiParam(value = "密码" , required=true ) String password) {
        return enterpriseServiceImpl.login(username, password);
    }

	@MyLog(value = "用户注销")  //这里添加了AOP的自定义注解
    @PostMapping("/logout")
	@ApiOperation(value = "注销", notes = "")
    public String logout(HttpServletRequest request) {
        return enterpriseServiceImpl.logout(request);
    }
	
	@MyLog(value = "修改密码")  //这里添加了AOP的自定义注解
    @PostMapping("/modifypassword")
	@ApiOperation(value = "修改密码", notes = "")
    public R modifyPasswrod(HttpServletRequest request,String oldpassword,String newpassword) {
		if(oldpassword!=null&&newpassword!=null) {
			if(newpassword.length()<6) {
				return R.error("密码需大于等于6位");
			}
			switch (enterpriseServiceImpl.modifyPassword(request, oldpassword, newpassword)) {
			case 1:
				return R.ok("密码修改成功");
			case -1:
				return R.error("密码修改失败");
			case -2:
				return R.error("旧密码输入错误");
			case -3:
				return R.error("未查询到该账号");
			case -4:
				return R.error("未登录，请登录");
			default:
				return R.error("未知原因,请联系管理员");
			}
		}
		
        return R.error("请正确输入");
    }

}
