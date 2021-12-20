package com.jianzhi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.jianzhi.mapper.EnterpriseMapper;
import com.jianzhi.model.Enterprise;
import com.jianzhi.model.Spotcheck;
import com.jianzhi.service.EnterpriseService;
import com.jianzhi.service.RedisService;
import com.jianzhi.service.SpotcheckService;

@Service
public class EnterpriseServiceImpl implements EnterpriseService{
	
	@Autowired
    private RedisService redisService;

	@Resource
	EnterpriseMapper enterpriseMapper;
	
	@Resource
	SpotcheckService spotcheckServiceImpl;
	
	
	@Override
	public Map<String, Object> login(String zh, String mm) {
		Map<String, Object> map=new HashMap<String, Object>();
		Map<String, Object> msg;
		if(zh==""||zh==null||mm==""||mm==null) {
			map.put("message", "请填写用户名或密码");
			return map;
		}
		Enterprise ent=enterpriseMapper.selectByZhAndMM(zh, mm);
		List<Map<String, Object>> stlist=new ArrayList<>();
		if(ent!=null) {
			Spotcheck sp=new Spotcheck();
			
			if("admin".equals(ent.getRole())) {
				sp.setIsDqenter(1);
				sp.setIsXhenter(0);
				sp.setIsRead(0);
				List<Spotcheck> lt=spotcheckServiceImpl.getSpotcheckList(sp);
				for (Spotcheck spotcheck : lt) {
					msg=new HashMap<String, Object>();
					msg.put("id", spotcheck.getId());
					msg.put("str", spotcheck.getAreaName()+"地区"+spotcheck.getName()+"已确认，请及时检阅!");
					stlist.add(msg);
				}
				map.put("msgstr", stlist);
			}
			String token = UUID.randomUUID().toString();
	        redisService.set(token, ent.getId());
	        map.put("token", token);
	        map.put("dwid", ent.getId());
	        map.put("name", ent.getName());
	        map.put("msg", "登录成功");
	        map.put("roles",ent.getRole());
	        map.put("code", 0);
	        return map;
		}
			map.clear();
			map.put("message", "用户名或密码错误，登录失败");
			map.put("code", 1);
	        return map;
		
	}
	
	public String logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        Boolean delete = redisService.delete(token);
        if (!delete) {
            return "注销失败，请检查是否登录！";
        }
        return "注销成功！";
    }

	@Override
	public Enterprise getEnterprise(String id) {
		return enterpriseMapper.selectByPrimaryKey(id);
	}

	@Override
	public int modifyPassword(HttpServletRequest request,String oldpassword,String newpassword) {
		String token = request.getHeader("token");
		String entid= (String) redisService.get(token);
		if(token!=null&&!token.isEmpty()) {
			Enterprise enterprise=enterpriseMapper.selectByPrimaryKey(entid);
			if(enterprise!=null) {
				if(enterprise.getPxmm().equals(oldpassword)) {
					if(enterpriseMapper.updatePassword(entid, newpassword)>0) {
						return 1;//修改成功返回1
					}else {
						return -1;//修改失败返回-1
					}
				}else {
					    return -2;//旧密码输入错误返回-2
				}
			}else {
				return -3;//未查询到该账号返回-3
			}
		}else {
			return -4;//token为空未登录
		}
	}

}
