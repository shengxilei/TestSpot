package com.jianzhi.service;

import java.util.List;

import com.jianzhi.model.SpotcheckMemberChange;

public interface SpotcheckMemberChangeService {
	
	public int save(SpotcheckMemberChange spotcheckMemberChange);
	
	/**
	 * 查询修改记录
	 * @param spotcheckMember
	 * @return
	 */
	List<SpotcheckMemberChange> getSpotcheckMemberChangeList(SpotcheckMemberChange spotcheckMemberChange);

}
