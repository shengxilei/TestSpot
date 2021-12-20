package com.jianzhi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jianzhi.annotation.MyLog;
import com.jianzhi.model.Enterprise;
import com.jianzhi.model.MemberTemp;
import com.jianzhi.model.Pxbm;
import com.jianzhi.model.Spotcheck;
import com.jianzhi.service.MemberTempService;
import com.jianzhi.service.PxbmService;
import com.jianzhi.service.SpotcheckService;
import com.jianzhi.service.SubjectService;
import com.jianzhi.util.R;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/memberTemp")
public class MemberTempController {
	
	@Autowired
	MemberTempService memberTempServiceImpl;
	@Autowired
	PxbmService pxbmServiceImpl;
	@Autowired
	SpotcheckService spotcheckServiceImpl;
	@Autowired
	SubjectService subjectServiceImpl;
	
	@PostMapping("/input")
	@ApiOperation(value = "导入到临时表Excel", notes = "")
	public R inputMemberTempList(MultipartHttpServletRequest multipartHttpServletRequest,Integer spid) {
		
		MultipartFile file = multipartHttpServletRequest.getFile("memberExcel");
		if(file!=null&&!file.isEmpty()) {
			ExcelReader excelReader;
			try {
				excelReader = ExcelUtil.getReader(file.getInputStream());
				List<MemberTemp> memberTempList=new ArrayList<MemberTemp>();
				MemberTemp  memberTemp;
				List<Map<String,Object>> readAll = excelReader.readAll();
				for (Map<String, Object> map : readAll) {
					memberTemp=new MemberTemp();
					memberTemp.setXm((String) map.get("姓名"));
					memberTemp.setXb((String) map.get("性别")=="男"?0:1);
					memberTemp.setSfzhm((String) map.get("身份证号码"));
					memberTemp.setSpid(spid);
					memberTemp.setDwm((String) map.get("单位名称"));
					memberTemp.setXh((Integer) map.get("序号"));
					memberTempList.add(memberTemp);
				}
				int num= memberTempServiceImpl.inputMemberTempList(memberTempList);
				if(num>0) {
					return R.ok().put("num", num).put("msg", "成功导入");
				}else {
					return R.error("导入格式错误或者没有数据");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		
		return R.error("解析excel失败");
	}

	@MyLog("添加抽查人员到临时表")
	@PostMapping("/addMemberTempList")
	@ApiOperation(value = "从pxbm表内选择人员导入到临时表", notes = "")
	public R addMemberTempList(@RequestParam("idlist") List<String> idlist,Integer spid) {
		Spotcheck spotcheck=spotcheckServiceImpl.getSpotcheckById(spid);
		/*if(spotcheck.getIsInput()>0) {
			return R.error("该记录已经正式导入，不能导入临时库");
		}*/
		if(idlist!=null) {
			Pxbm pxbm=null;
			MemberTemp memberTemp;
			List<MemberTemp> memberTempList=new ArrayList<>();
			for (String id : idlist) {
				memberTemp=new MemberTemp();
				pxbm=pxbmServiceImpl.getPxbmById(id);
				if(pxbm.getSpid()!=null) {
					return R.error("存在已分配人员，不能导入");
				}
				memberTemp.setAge(pxbm.getAge2());
				memberTemp.setDwm(pxbm.getDwm());
				memberTemp.setJobtitle(pxbm.getZc());
				memberTemp.setKmm(pxbm.getKmm());
				memberTemp.setSfzhm(pxbm.getSfzhm());
				memberTemp.setJg(Integer.valueOf(pxbm.getJg()));
				memberTemp.setSpid(spid);
				memberTemp.setXb(pxbm.getXb().equals("男")?0:1);
				memberTemp.setXm(pxbm.getXm());
				memberTemp.setIsCNS(pxbm.getIsCNS());
				memberTemp.setIsSB(pxbm.getIsSB());
				memberTemp.setPxbmid(id);
				memberTempList.add(memberTemp);
			}
			int num=memberTempServiceImpl.addMemberTempList(idlist,memberTempList,spid);
			if(num>0) {
				return R.ok("插入成功");
			}
		}
		return R.error("插入失败，请联系管理员");
	}
	
	@MyLog(value = "查询临时表")  //这里添加了AOP的自定义注解
	@PostMapping("/list")
	@ApiOperation(value = "根据条件查询临时表内容", notes = "")
    public  Object getPxbmList(MemberTemp memberTemp,
    		@RequestParam(defaultValue = "1",value = "pageNumber") Integer pageNum,
    		@RequestParam(defaultValue = "10",value = "pageSize") Integer pageSize){ 
        PageHelper.startPage(pageNum,pageSize);
        List<MemberTemp> list = memberTempServiceImpl.getTempList(memberTemp);
        PageInfo<MemberTemp> pageInfo = new PageInfo<MemberTemp>(list); 
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
	
	@MyLog("删除临时库中的人员")
	@PostMapping("/deleteMemberTemp")
	@ApiOperation(value = "删除临时库中的人员", notes = "")
	public R addMemberTempList(@RequestParam("idlist") List<Integer> idlist) {
		if(memberTempServiceImpl.deleteTempByIdList(idlist)>0) {
			return R.ok("删除成功");
		}
		return R.error("删除失败，请联系管理员");
	}
	
	
	
}
