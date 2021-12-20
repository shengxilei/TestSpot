package com.jianzhi.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jianzhi.mapper.QualificationsMapper;
import com.jianzhi.model.Qualifications;
import com.jianzhi.model.QualificationsDetail;
import com.jianzhi.model.QualificationsEx;
import com.jianzhi.service.QualificationsDetailService;
import com.jianzhi.service.QualificationsService;
import com.jianzhi.service.SubjectService;

@Service
public class QualificationsServiceImpl implements QualificationsService {
	
	@Resource
	QualificationsMapper qualificationsMapper;
	@Resource
	QualificationsDetailService qualificationsDetailServiceImpl;
	@Resource
	SubjectService subjectServiceImpl;
	

	@Override
	@Transactional
	public int removeQualifications(String id) {
		// TODO Auto-generated method stub
		qualificationsDetailServiceImpl.removeQualificationsDetailByQuaid(id);
		return qualificationsMapper.deleteByPrimaryKey(id);
	}

	@Override
	@Transactional
	public int addQualifications(Qualifications qualifications,List<QualificationsDetail> detailList) {
		// TODO Auto-generated method stub
		int a=qualificationsMapper.insert(qualifications);
		for (QualificationsDetail detail : detailList) {
			detail.setQualid(qualifications.getId());
			String code=subjectServiceImpl.getSubjectByName(detail.getZzKmm());
			detail.setZzCode(code);
			detail.setQualid(qualifications.getId());
			detail.setZsBh(qualifications.getZsBh());
			detail.setDwm(qualifications.getDwm());
			a=+qualificationsDetailServiceImpl.addQualificationsDetail(detail);
		}
		return a;
	}

	@Override
	@Transactional
	public int modifyQualifications(Qualifications qualifications,List<QualificationsDetail> detailList) {
		// TODO Auto-generated method stub
		int a=qualificationsMapper.updateByPrimaryKeySelective(qualifications);//更新主表
		a=+qualificationsDetailServiceImpl.removeQualificationsDetailByQuaid(qualifications.getId());//删除原表项目列表
		for (QualificationsDetail detail : detailList) {
			detail.setQualid(qualifications.getId());
			String code=subjectServiceImpl.getSubjectByName(detail.getZzKmm());
			detail.setZzCode(code);
			detail.setQualid(qualifications.getId());
			detail.setZsBh(qualifications.getZsBh());
			detail.setDwm(qualifications.getDwm());
			a=+qualificationsDetailServiceImpl.addQualificationsDetail(detail);//添加新项目列表
		}
		
		return a;
	}

	@Override
	public QualificationsEx selectQualificationsBydwm(String dwm) {
		// TODO Auto-generated method stub
		return qualificationsMapper.selectQualificationsBydwm(dwm);
	}

	@Override
	public QualificationsEx selectQualificationsBydwid(String dwid) {
		// TODO Auto-generated method stub
		return qualificationsMapper.selectQualificationsBydwid(dwid);
	}

	@Override
	public List<QualificationsEx> selectQualificationsByMh(Qualifications qualifications) {
		// TODO Auto-generated method stub
		return qualificationsMapper.selectQualificationsByMh(qualifications);
	}

}
