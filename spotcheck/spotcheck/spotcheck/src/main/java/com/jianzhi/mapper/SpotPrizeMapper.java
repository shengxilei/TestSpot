package com.jianzhi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jianzhi.model.SpotPrize;
import com.jianzhi.model.SpotcheckMember;

public interface SpotPrizeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SpotPrize record);

    int insertSelective(SpotPrize record);

    SpotPrize selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SpotPrize record);

    int updateByPrimaryKey(SpotPrize record);
    
    
    int insertSpotPrizeList(@Param("spotPrizeList")List<SpotPrize> spotPrizeList);
    
    /**
     * 查询该条抽查记录的抽奖状态
     * @param spotPrize
     * @return
     */
    List<SpotPrize> selectSpotPrizeByDetail(SpotPrize spotPrize);
    
}