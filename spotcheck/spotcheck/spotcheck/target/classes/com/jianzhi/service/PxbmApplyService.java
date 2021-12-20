package com.jianzhi.service;

import com.jianzhi.model.PxbmApply;

public interface PxbmApplyService {
	
	/**
     * 根据pxid查询申请
     * @param pxid
     * @return
     */
    PxbmApply getByPxid(String pxid);
    
    /**
     * 更新是否同意及备注
     * @param pxbmApply
     * @return
     */
    int checkstatus(PxbmApply pxbmApply);
    
    /**
     * 提出申请
     * @param pxbmApply
     * @return
     */
    int addPxbmApply(PxbmApply pxbmApply);

}
