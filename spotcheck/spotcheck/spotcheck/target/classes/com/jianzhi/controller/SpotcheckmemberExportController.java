package com.jianzhi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jianzhi.annotation.MyLog;
import com.jianzhi.model.Spotcheck;
import com.jianzhi.model.SpotcheckMember;
import com.jianzhi.service.SpotcheckService;
import com.jianzhi.service.SpotcheckmemberService;
import com.jianzhi.util.MyExcel;

import cn.hutool.core.collection.CollUtil;


public class SpotcheckmemberExportController {
	/*@Resource
	SpotcheckService spotcheckServiceImpl;
	@Resource
	private SpotcheckmemberService spotcheckmemberServiceImpl;
	
	@MyLog(value = "导出该抽查记录的人员")  //这里添加了AOP的自定义注解
	@GetMapping("/export")
    public  void getSpotcheckMemberList(HttpServletResponse response,Integer spid,SpotcheckMember spotcheckMember){ 
		Spotcheck spotcheck=spotcheckServiceImpl.getSpotcheckById(spid);
        spotcheckMember.setSpid(spid);
        List<List<String>> rows=new ArrayList<>();
        
        rows.add(CollUtil.newArrayList("序号","姓名","身份证", "性别", "单位名称", "考试科目","抽查状态","考核结果"));
        List<String> row;
        List<SpotcheckMember> list = spotcheckmemberServiceImpl.getSpotcheckMemberList(spotcheckMember);
        for (SpotcheckMember sp : list) {
        	row = CollUtil.newArrayList(sp.getXh().toString(),sp.getXm(),sp.getSfzhm(), sp.getXb()==0?"男":"女", sp.getDwm(),
        			sp.getKmm(),sp.getCheckstatus()==0?"待抽查":sp.getCheckstatus()==1?"抽中":"未抽中"
        				,sp.getJg()==0?"未设":sp.getJg()==1?"通过":"未通过");
        	rows.add(row);
		}
        try {
            MyExcel.getExcel(response,rows.get(0).size(),rows,spotcheck.getName());
        }catch (Exception e){
            e.printStackTrace();
        }
    } */

}
