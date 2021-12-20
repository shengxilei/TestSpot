package com.jianzhi.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jianzhi.mapper.PxbmApplyMapper;
import com.jianzhi.model.PxbmApply;
import com.jianzhi.service.PxbmApplyService;
import com.jianzhi.service.PxbmService;

@Service
public class PxbmApplyServiceImpl implements PxbmApplyService{

	@Resource
	private PxbmApplyMapper pxbmApplyMapper;
	@Resource
	private PxbmService pxbmServiceImpl;
	
	@Override
	public PxbmApply getByPxid(String pxid) {
		// TODO Auto-generated method stub
		return pxbmApplyMapper.selectByPxid(pxid);
	}

	@Override
	@Transactional
	public int checkstatus(PxbmApply pxbmApply) {
		if(pxbmApply.getStatus()==2) {//如同意则修改pxbm表中的cns sb
			pxbmServiceImpl.modifyCNSandSB(pxbmApply.getIsCns(),
					pxbmApply.getIsSb(), null, pxbmApply.getPxid());
		}
		// TODO Auto-generated method stub
		pxbmServiceImpl.setIsApply(pxbmApply.getStatus(),null,pxbmApply.getReply(), pxbmApply.getPxid());
		pxbmApplyMapper.updateByPrimaryKeySelective(pxbmApply);
		
		return 1;
	}

	@Override
	@Transactional
	public int addPxbmApply(PxbmApply pxbmApply) {
		// TODO Auto-generated method stub
		pxbmApply.setStatus(1);
		int a=pxbmServiceImpl.setIsApply(1,pxbmApply.getApply(),null, pxbmApply.getPxid());
		int b=pxbmApplyMapper.insert(pxbmApply);
		if(a>0&&b>0) {
			return 1;
		}
		return 0;
	}

}
