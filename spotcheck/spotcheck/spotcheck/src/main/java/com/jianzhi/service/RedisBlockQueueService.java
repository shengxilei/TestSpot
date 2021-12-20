package com.jianzhi.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.jianzhi.common.component.RedisBlockQueue;
import com.jianzhi.mapper.SpotcheckMemberMapper;
import com.jianzhi.model.SpotcheckMember;

//@Component
public class RedisBlockQueueService {

	/*@Resource
	SpotcheckMemberMapper spotcheckMemberMapper;

	@Resource
	SpotcheckService spotcheckServiceImpl;

	@Resource
	SpotcheckMemberChangeService spotcheckMemberChangeServiceImpl;
	
	@Resource
	private RedisBlockQueue redisBlockQueue;

	@PostConstruct
	public void init() {
		System.out.println("开始监测");
	   redisBlockQueue.bRPopLPush("spot", (value) -> {
		   System.out.println(value);
		   String[] list=value.toString().split(",");
		   Integer id=Integer.valueOf(list[0]);
		   Integer spid=Integer.valueOf(list[1]);
		   SpotcheckMember spotcheckMember=spotcheckMemberMapper.selectByPrimaryKey(id);
		   
		   spotcheckMemberMapper.getNonedwm(spid,
				   spotcheckMemberMapper.selectByPrimaryKey(spid).getKmm());//查询该记录所有没资质的单位
		   
		   if(spotcheckMember.getIsZz()!=1) {//该人没有资质
			   
		   }else {
			   
		   }
	   });
	}*/
}
