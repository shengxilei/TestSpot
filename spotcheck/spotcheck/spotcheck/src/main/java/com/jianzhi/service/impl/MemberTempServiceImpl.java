package com.jianzhi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.jianzhi.mapper.MemberTempMapper;
import com.jianzhi.model.MemberTemp;
import com.jianzhi.service.MemberTempService;
import com.jianzhi.service.PxbmService;

@Service
public class MemberTempServiceImpl implements MemberTempService{
	
	@Autowired
	MemberTempMapper memberTempMapper;
	
	@Autowired
	PxbmService pxbmServiceImpl;

	@Override
	@Transactional
	public int inputMemberTempList(List<MemberTemp> memberTempList) {
		// TODO Auto-generated method stub
		return memberTempMapper.inputMemberTempList(memberTempList);
	}

	@Override
	@Transactional
	public int addMemberTempList(List<String> idList,List<MemberTemp> memberTempList,Integer spid) {
		
			try {
				memberTempMapper.insertMemberTempList(memberTempList);
				pxbmServiceImpl.updateDistribution(idList, spid);
				return 1;
			} catch (Exception e) {
				e.printStackTrace();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			
			return 0;
	}

	@Override
	public List<MemberTemp> getTempList(MemberTemp memberTemp) {
		return memberTempMapper.selectMsemberTempByDetail(memberTemp);
	}

	@Override
	public int deleteTempByIdList(List<Integer> idList) {
		return memberTempMapper.deleteTempByIdList(idList);
	}

	@Override
	public  int insertSpokenMembers(MemberTemp memberTemp){
		return memberTempMapper.insertSpokenMembers(memberTemp);
	}
}
