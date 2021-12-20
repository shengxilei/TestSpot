package com.jianzhi.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.jianzhi.model.Job;
import com.jianzhi.util.DateCoreUtil;

import cn.hutool.cron.CronUtil;

@Component
@EnableScheduling
public class ScheduleService {
	
	@Autowired
	private SpotcheckmemberService spotcheckmemberServiceImpl;
	@Autowired
	private JobService jobServiceImpl;
	
	
	
	@PostConstruct
	public void init() {
		//初始化计时器
		System.out.println("初始化计时器");
		List<Job> joblist=jobServiceImpl.getJobList();
		for (Job job : joblist) {
			System.out.println("添加计时器spid："+job.getSpid()+DateCoreUtil.getCron(job.getStartdate()));
			CronUtil.schedule(job.getSpid().toString(), DateCoreUtil.getCron(job.getStartdate()),
					() -> {
						if(spotcheckmemberServiceImpl.countspot(job.getSpid())>0) {
							jobServiceImpl.updateJobUsed(job.getSpid());
						}
						CronUtil.remove(job.getSpid().toString());
					});
			
			System.out.println("结束"+job.getSpid());
		}
		
	}

}
