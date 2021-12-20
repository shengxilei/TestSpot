package com.jianzhi.mapper;

import com.jianzhi.model.PxbmApply;

public interface PxbmApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PxbmApply record);

    int insertSelective(PxbmApply record);

    PxbmApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PxbmApply record);

    int updateByPrimaryKey(PxbmApply record);


    
    /**
     * 根据pxid查询申请
     * @param pxid
     * @return
     */
    PxbmApply selectByPxid(String pxid);
}