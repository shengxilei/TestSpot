package com.jianzhi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jianzhi.model.Qualifications;
import com.jianzhi.model.QualificationsEx;

public interface QualificationsMapper {
    int deleteByPrimaryKey(String id);

    int insert(Qualifications record);

    int insertSelective(Qualifications record);

    Qualifications selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Qualifications record);

    int updateByPrimaryKey(Qualifications record);
    
    /**
     * 根据单位名查询
     * @param dwm
     * @return
     */
    QualificationsEx selectQualificationsBydwm(@Param("dwm")String dwm);
    
    /**
     * 根据单位id查询
     * @param dwm
     * @return
     */
    QualificationsEx selectQualificationsBydwid(@Param("dwid")String dwid);
    
    
    
    /**
     * 根据条件查询单位资质表
     * @param record
     * @return
     */
    List<QualificationsEx> selectQualificationsByMh(Qualifications record);
}