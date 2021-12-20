package com.jianzhi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jianzhi.mapper.SubjectMapper;
import com.jianzhi.service.SubjectService;

@Service
public class SubjectServiceImpl implements SubjectService {
	
	@Autowired
	SubjectMapper subjectMapper;

	@Override
	public String getSubjectByName(String name) {
		return subjectMapper.getSubjectByname(name);
	}

}
