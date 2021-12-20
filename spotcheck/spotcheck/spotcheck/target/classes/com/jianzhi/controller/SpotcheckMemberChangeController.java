package com.jianzhi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jianzhi.annotation.MyLog;
import com.jianzhi.model.SpotcheckMemberChange;
import com.jianzhi.service.SpotcheckMemberChangeService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/spotcheckMemberChange")
public class SpotcheckMemberChangeController {
	
	@Resource
	SpotcheckMemberChangeService spotcheckMemberChangeServiceImpl;
	
	@MyLog(value = "查询修改记录")  //这里添加了AOP的自定义注解
	@PostMapping("/list")
	@ApiOperation(value = "根据条件查询修改记录", notes = "")
    public  Object getSpotcheckMemberChangeList(SpotcheckMemberChange spotcheckMemberChange,
    		@RequestParam(defaultValue = "1",value = "pageNumber") Integer pageNum,
    		@RequestParam(defaultValue = "10",value = "pageSize") Integer pageSize){ 
        PageHelper.startPage(pageNum,pageSize);
        List<SpotcheckMemberChange> list = spotcheckMemberChangeServiceImpl.getSpotcheckMemberChangeList(spotcheckMemberChange);
        PageInfo<SpotcheckMemberChange> pageInfo = new PageInfo<SpotcheckMemberChange>(list); 
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("list", pageInfo.getList());
        data.put("total", pageInfo.getTotal());
        map.put("data", data);
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("success", true);
        return map;
    } 
}
