package com.jianzhi.interceptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jianzhi.service.RedisService;

public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	//System.out.println("======过滤=======");
    	if("OPTIONS".equals(request.getMethod().toUpperCase())) {
            return true;
        }
    	String[] allowDomain = {"http://211.155.231.168:81", "http://192.168.2.4:9527","http://202.75.231.91:81"};
    	Set<String> allowedOrigins = new HashSet<String>(Arrays.asList(allowDomain));
    	String originHeader = request.getHeader("Origin");
        response.setCharacterEncoding("UTF-8");
        if (allowedOrigins.contains(originHeader)) {
        response.setHeader("Access-Control-Allow-Origin",originHeader);
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "3600");
        }
        
        String token = request.getHeader("token");
        if (token==null||token=="") {
        	request.setAttribute("msg", "用户未登录");
        	request.setAttribute("code", 101);
            return false;
        }
        Object loginStatus = redisService.get(token);
        if( Objects.isNull(loginStatus)){
        	request.setAttribute("msg", "token错误");
        	request.setAttribute("code", 101);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}