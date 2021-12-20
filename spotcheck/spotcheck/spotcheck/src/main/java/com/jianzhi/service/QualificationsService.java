package com.jianzhi.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jianzhi.model.Qualifications;
import com.jianzhi.model.QualificationsDetail;
import com.jianzhi.model.QualificationsEx;

public interface QualificationsService {
	
	/**
	 * 删除id
	 * @param id
	 * @return
	 */
	int removeQualifications(String id);
	
	/**
	 * 添加资质信息
	 * @param qualifications
	 * @return
	 */
	int addQualifications(Qualifications qualifications,List<QualificationsDetail> detailList);
	
	/**
	 * 更新资质信息
	 * @param qualifications
	 * @return
	 */
	int modifyQualifications(Qualifications qualifications,List<QualificationsDetail> detailList);
	
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
    List<QualificationsEx> selectQualificationsByMh(Qualifications qualifications);
	

}
