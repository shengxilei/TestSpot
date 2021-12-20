package com.jianzhi.model;

import java.util.List;

public class QualificationsEx extends Qualifications{

	
	private static final long serialVersionUID = 1L;
	
	 private List<QualificationsDetail> detailList;

	public List<QualificationsDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<QualificationsDetail> detailList) {
		this.detailList = detailList;
	}
	 

}
