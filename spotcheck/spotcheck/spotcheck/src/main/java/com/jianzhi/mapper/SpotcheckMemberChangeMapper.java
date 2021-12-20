package com.jianzhi.mapper;

import java.util.List;

import com.jianzhi.model.SpotcheckMember;
import com.jianzhi.model.SpotcheckMemberChange;

public interface SpotcheckMemberChangeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SpotcheckMemberChange record);

    int insertSelective(SpotcheckMemberChange record);

    SpotcheckMemberChange selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SpotcheckMemberChange record);

    int updateByPrimaryKey(SpotcheckMemberChange record);
    
    /**
     * 查询该次抽查的人员数据
     * @param spotcheckMember
     * @return
     */
    List<SpotcheckMemberChange> selectSpotcheckMemberChangeList(SpotcheckMemberChange spotcheckMemberChange);
}