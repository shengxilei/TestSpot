package com.jianzhi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jianzhi.mapper.SpotPrizeMapper;
import com.jianzhi.model.SpotPrize;
import com.jianzhi.service.SpotPrizeService;

@Service
public class SpotPrizeServiceImpl implements SpotPrizeService{
	
	@Autowired
	SpotPrizeMapper spotPrizeMapper;

	@Override
	public int addSpotPrizeList(List<SpotPrize> spotPrizeList) {
		return spotPrizeMapper.insertSpotPrizeList(spotPrizeList);
	}

	@Override
	public List<SpotPrize> selectSpotPrizeByDetail(SpotPrize spotPrize) {
		return spotPrizeMapper.selectSpotPrizeByDetail(spotPrize);
	}

	@Override
	public int updateSpotPrize(SpotPrize spotPrize) {
		// TODO Auto-generated method stub
		return spotPrizeMapper.updateByPrimaryKey(spotPrize);
	}

}
