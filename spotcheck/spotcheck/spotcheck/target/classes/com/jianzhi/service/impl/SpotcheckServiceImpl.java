package com.jianzhi.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.jianzhi.model.SpotcheckMember;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.jianzhi.mapper.SpotcheckMapper;
import com.jianzhi.model.Job;
import com.jianzhi.model.Spotcheck;
import com.jianzhi.service.JobService;
import com.jianzhi.service.PxbmService;
import com.jianzhi.service.SpotcheckService;
import com.jianzhi.service.SpotcheckmemberService;
import com.jianzhi.util.DateCoreUtil;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.pattern.CronPattern;

@Service
public class SpotcheckServiceImpl implements SpotcheckService {
	
	@Resource
	private SpotcheckMapper spotcheckMapper;
	@Resource
	private PxbmService pxbmServiceImpl;
	@Resource
	SpotcheckmemberService spotcheckmemberServiceImpl;
	@Resource
	private JobService jobServiceImpl;
	

	@Override
	public int addSpotcheck(Spotcheck spotcheck) {
		// TODO Auto-generated method stub
		return spotcheckMapper.insertSelective(spotcheck);
	}

	@Override
	@Transactional
	public int deleteSpotcheck(Integer id) {
		// TODO Auto-generated method stub
		try {
			spotcheckMapper.deleteZeroById(id);
			pxbmServiceImpl.clearSpid(id);
			CronUtil.remove(id.toString());
			jobServiceImpl.deleteJobBySpid(id);//删除job
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
		
		
	}

	@Override
	@Transactional
	public int deleteXhSpotcheck(Integer id){
		try {
			spotcheckMapper.deleteZeroById(id);
			CronUtil.remove(id.toString());
			jobServiceImpl.deleteJobBySpid(id);//删除job
			//spotcheckmemberServiceImpl.deleteXhSpotmember(id);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}

	}

	@Override
	public int deleteXhspotecheck(Integer id){
		return spotcheckMapper.deleteXhspotecheck(id);
	}

	@Override
	@Transactional
	public int modifySpotcheck(Spotcheck spotcheck) {
		Integer id=spotcheck.getId();
		Spotcheck old =spotcheckMapper.selectByPrimaryKey(id);
		if(old.getStart().compareTo(spotcheck.getStart())!=0) {
			Job job=new Job();
			job.setStartdate(spotcheck.getStart());
			job.setSpid(id);
			jobServiceImpl.updateStratDate(job);;
			//修改已存在的定时器
			CronUtil.updatePattern(id.toString(),new CronPattern(DateCoreUtil.getCron(spotcheck.getStart())));
			jobServiceImpl.updateStratDate(job);
		}
		return spotcheckMapper.updateByPrimaryKeySelective(spotcheck);
	}

	@Override
	public List<Spotcheck> getSpotcheckList(Spotcheck spotcheck) {
		// TODO Auto-generated method stub
		
		return spotcheckMapper.selectSpotcheckByDetail(spotcheck);
	}

	@Override
	public List<Spotcheck> getSpotcheckListByEntid(String entid, Spotcheck spotcheck) {
		String status=null;
		if(spotcheck==null) {
			return spotcheckMapper.selectSpotcheckListByEntid(entid,null,null,null,null,null,null,null);
		}
		if(spotcheck.getStatus()!=null) {
		status=spotcheck.getStatus().toString();
		}
		return spotcheckMapper.selectSpotcheckListByEntid(entid, spotcheck.getYyyy(), spotcheck.getNum(), spotcheck.getKmm(),status,spotcheck.getNow(),spotcheck.getName(),spotcheck.getAreaName());
	}

	@Override
	public Spotcheck getSpotcheckById(Integer id) {
		return spotcheckMapper.selectByPrimaryKey(id);
	}

	@Override
	public int modifyDqenter(List<Integer> idlist) {
		return spotcheckMapper.modifyDqenter(idlist);
	}

	@Override
	public int modifyXhEnter(List<Integer> idlist) {
		return spotcheckMapper.modifyXhEnter(idlist);
	}
	
	@Override
	public int setIsRead(Integer id) {
		return spotcheckMapper.setIsRead(id);
	}

	@Override
	public Spotcheck selectSpotcheckInfos(Integer id){
		return  spotcheckMapper.selectSpotcheckInfos(id);
	}

	@Override
	public List<Spotcheck> selectXhJdSpotcheckByDetail(Spotcheck spotcheck){
		return spotcheckMapper.selectXhJdSpotcheckByDetail(spotcheck);
	}

	@Override
	public int updateSpotcheckXhjd(SpotcheckMember spotcheckMember){
		return spotcheckMapper.updateSpotcheckXhjd(spotcheckMember);
	}

	@Override
	public int insertSpid(Integer spid,Integer jdspid){
		return spotcheckMapper.insertSpid(spid,jdspid);
	}

	@Override
	public List<Integer>getselectSpid(Integer jdspid){
		return spotcheckMapper.getselectSpid(jdspid);
	}

}
