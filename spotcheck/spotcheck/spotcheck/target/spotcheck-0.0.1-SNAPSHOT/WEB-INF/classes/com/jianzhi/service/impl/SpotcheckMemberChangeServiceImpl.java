package com.jianzhi.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jianzhi.mapper.SpotcheckMemberChangeMapper;
import com.jianzhi.model.SpotcheckMemberChange;
import com.jianzhi.service.SpotcheckMemberChangeService;

@Service
public class SpotcheckMemberChangeServiceImpl implements SpotcheckMemberChangeService{
	@Resource
	SpotcheckMemberChangeMapper spotcheckMemberChangeMapper;

	@Override
	public int save(SpotcheckMemberChange spotcheckMemberChange) {
		// TODO Auto-generated method stub
		return spotcheckMemberChangeMapper.insert(spotcheckMemberChange);
	}

	@Override
	public List<SpotcheckMemberChange> getSpotcheckMemberChangeList(SpotcheckMemberChange spotcheckMemberChange) {
		return spotcheckMemberChangeMapper.selectSpotcheckMemberChangeList(spotcheckMemberChange);
	}
	
	
}
