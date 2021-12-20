package com.jianzhi.service;

import com.jianzhi.model.QualificationsDetail;

public interface QualificationsDetailService {
	/**
	 * 根据quaid删除全部资质
	 * @param id
	 * @return
	 */
	 int removeQualificationsDetailByQuaid(String quaid);
	 
	 /**
	  * 添加新资质项
	  * @param qualificationsDetail
	  * @return
	  */
	 int addQualificationsDetail(QualificationsDetail qualificationsDetail);
	 
	 /**
	  * 根据id删除资质项
	  * @param id
	  * @return
	  */
	 int removeQualificationsDetailById(Integer id);

}
