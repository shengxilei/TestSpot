package com.jianzhi.service;

import java.util.List;


import com.jianzhi.model.MemberTemp;

public interface MemberTempService {
	
	/**
	 * 批量插入数据
	 * @param idList 设置pxbm表已分配
	 * @param memberTempList 插入表
	 * @return
	 */
	int addMemberTempList(List<String> idList,List<MemberTemp> memberTempList,Integer spid);
	
	/**
	 * 批量导入数据
	 * @param memberTempList
	 * @return
	 */
	int inputMemberTempList(List<MemberTemp> memberTempList);

	/**
	 * 获取当前记录的临时表数据
	 * @param memberTemp
	 * @return
	 */
	List<MemberTemp> getTempList(MemberTemp memberTemp);
	
	/**
	 * 根据idList表批量删除 pxbm表中spid由mysql触发器赋值null
	 * @param idList
	 * @return
	 */
	int deleteTempByIdList(List<Integer> idList);

	/**
	 * 将Excel表格里面的抽查人员插入到正式表中
	 */
	public  int insertSpokenMembers(MemberTemp memberTemp);
}
