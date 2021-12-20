package com.jianzhi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jianzhi.annotation.MyLog;
import com.jianzhi.model.Enterprise;
import com.jianzhi.model.Qualifications;
import com.jianzhi.model.QualificationsDetail;
import com.jianzhi.model.QualificationsEx;
import com.jianzhi.service.EnterpriseService;
import com.jianzhi.service.QualificationsService;
import com.jianzhi.service.RedisService;
import com.jianzhi.util.R;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/qualification")
public class QualificationsController {
	
	@Resource
	private QualificationsService qualificationsServiceImpl;
	@Resource
	EnterpriseService enterpriseServiceImpl;
	
	@Autowired
    private RedisService redisService;
	
	@MyLog(value = "查询单位资质")  //这里添加了AOP的自定义注解
	@PostMapping("/list")
	@ApiOperation(value = "查询单位资质", notes = "")
    public  Object getPxbmList(HttpServletRequest request,Qualifications qualifications,
    		@RequestParam(defaultValue = "1",value = "pageNumber") Integer pageNum,
    		@RequestParam(defaultValue = "10",value = "pageSize") Integer pageSize){ 
		String token=request.getHeader("token");
		String entid= (String) redisService.get(token);
		Enterprise ent=enterpriseServiceImpl.getEnterprise(entid);
		if("dqeditor".equals(ent.getRole())) {
			qualifications.setAreaName(ent.getAreaName());
		}
        PageHelper.startPage(pageNum,pageSize);
        List<QualificationsEx> list = qualificationsServiceImpl.selectQualificationsByMh(qualifications);
        PageInfo<QualificationsEx> pageInfo = new PageInfo<QualificationsEx>(list); 
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("tempList", pageInfo.getList());
        data.put("total", pageInfo.getTotal());
        map.put("data", data);
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("success", true);
        return map;
    }
	
	@MyLog(value = "添加新单位资质")  //这里添加了AOP的自定义注解
	@PostMapping("/add")
	@ApiOperation(value = "添加新单位资质", notes = "")
	public R addNewQualifications(Qualifications qualifications,@RequestParam("detailList")List<String> detailList) {
		List<QualificationsDetail> list=new ArrayList<>();
		QualificationsDetail de=null;
		for (String zzkmm : detailList) {
			de=new QualificationsDetail();
			de.setZzKmm(zzkmm);
			list.add(de);
		}
		if(qualificationsServiceImpl.addQualifications(qualifications,list)>0) {
			return R.ok("新增成功");
		}else {
			return R.error("新增失败，请联系管理员");
		}
	}
	
	@MyLog(value = "删除单位资质")  //这里添加了AOP的自定义注解
	@PostMapping("/delete")
	@ApiOperation(value = "删除单位资质", notes = "")
	public R deleteNewQualifications(String id) {
		if(qualificationsServiceImpl.removeQualifications(id)>0) {
			return R.ok("删除成功");
		}else {
			return R.error("删除失败，请联系管理员");
		}
	}
	
	@MyLog(value = "修改单位资质")  //这里添加了AOP的自定义注解
	@PostMapping("/modify")
	@ApiOperation(value = "修改单位资质", notes = "")
	public R modifyQualifications(Qualifications qualifications,@RequestParam("detailList")List<String> detailList) {
		List<QualificationsDetail> list=new ArrayList<>();
		QualificationsDetail de=null;
		for (String zzkmm : detailList) {
			de=new QualificationsDetail();
			de.setZzKmm(zzkmm);
			list.add(de);
		}
		if(qualificationsServiceImpl.modifyQualifications(qualifications, list)>0) {
			return R.ok("修改成功");
		}else {
			return R.error("修改失败，请联系管理员");
		}
	}
	
	@MyLog(value = "查询登录单位资质")  //这里添加了AOP的自定义注解
	@PostMapping("/qual")
	@ApiOperation(value = "查询登录单位资质", notes = "")
	public R getQualification(HttpServletRequest request) {
		String token=request.getHeader("token");
		String entid= (String) redisService.get(token);
		Enterprise en=enterpriseServiceImpl.getEnterprise(entid);
		String dwm=null;
		if(en!=null) {
			dwm=en.getName();
		}
		QualificationsEx qua=qualificationsServiceImpl.selectQualificationsBydwm(dwm);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("qua", qua);
		return R.ok(map);	
	}
	
}
