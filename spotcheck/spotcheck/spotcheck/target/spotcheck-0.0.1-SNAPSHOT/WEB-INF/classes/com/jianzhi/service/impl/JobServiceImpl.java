package com.jianzhi.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jianzhi.mapper.JobMapper;
import com.jianzhi.model.Job;
import com.jianzhi.service.JobService;

@Service
public class JobServiceImpl implements JobService {
	
	@Resource
	JobMapper jobMapper;

	@Override
	public List<Job> getJobList() {
		return jobMapper.getJobList();
	}

	@Override
	public int updateJobUsed(Integer spid) {
		return jobMapper.updateJobUsed(spid);
	}

	@Override
	public int updateStratDate(Job record) {
		return jobMapper.updateStratDate(record);
	}

	@Override
	public int deleteJobBySpid(Integer spid) {
		return jobMapper.deleteJobBySpid(spid);
	}
	
	@Override
	public int addNewJob(Job job) {
		return jobMapper.insertSelective(job);
	}

}
