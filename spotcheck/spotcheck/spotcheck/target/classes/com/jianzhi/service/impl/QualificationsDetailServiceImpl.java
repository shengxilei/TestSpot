package com.jianzhi.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jianzhi.mapper.QualificationsDetailMapper;
import com.jianzhi.model.QualificationsDetail;
import com.jianzhi.service.QualificationsDetailService;

@Service
public class QualificationsDetailServiceImpl implements QualificationsDetailService{
	
	@Resource
	QualificationsDetailMapper qualificationsDetailMapper;

	@Override
	public int removeQualificationsDetailByQuaid(String quaid) {
		return qualificationsDetailMapper.deleteByQuaid(quaid);
	}

	@Override
	public int addQualificationsDetail(QualificationsDetail qualificationsDetail) {
		// TODO Auto-generated method stub
		return qualificationsDetailMapper.insertSelective(qualificationsDetail);
	}

	@Override
	public int removeQualificationsDetailById(Integer id) {
		// TODO Auto-generated method stub
		return qualificationsDetailMapper.deleteByPrimaryKey(id);
	}

}
