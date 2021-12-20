package com.jianzhi.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jianzhi.mapper.SysLogMapper;
import com.jianzhi.model.SysLog;

@Service
public class SysLogService {

	@Resource
	SysLogMapper sysLogMapper;
	
	public void save(SysLog sysLog) {
		sysLogMapper.insertSelective(sysLog);
	}
	
	

}
