package com.jianzhi.mapper;

import java.util.List;

import com.jianzhi.model.QualificationsDetail;

public interface QualificationsDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QualificationsDetail record);

    int insertSelective(QualificationsDetail record);

    QualificationsDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QualificationsDetail record);

    int updateByPrimaryKey(QualificationsDetail record);
    
    /**
     * 根据dwid查询单位id
     * @param dwid
     * @return
     */
    List<QualificationsDetail> selectDetailBydwid(String dwid);

    /**
     * 删除该资质单位的所有资质项
     * @param quaid
     * @return
     */
	int deleteByQuaid(String quaid);
    
    
}