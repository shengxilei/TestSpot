package com.jianzhi.service;

import java.util.List;

import com.jianzhi.model.SpotPrize;

public interface SpotPrizeService {
	
	public int addSpotPrizeList(List<SpotPrize> spotPrizeList);
	
	public List<SpotPrize> selectSpotPrizeByDetail(SpotPrize spotPrize);
	
	public int updateSpotPrize(SpotPrize spotPrize);

}
