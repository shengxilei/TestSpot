package com.jianzhi.service;

import java.util.List;

import com.jianzhi.model.Pxbm;
import com.jianzhi.model.Spotcheck;
import com.jianzhi.model.SpotcheckMember;

public interface SpotcheckService {
	/**
	 * 增
	 * @param spotcheck
	 * @return
	 */
	int addSpotcheck(Spotcheck spotcheck);
	/**
	 * 删
	 * @param spotcheck
	 * @return
	 */
	int deleteSpotcheck(Integer id);
	/**
	 * 改
	 * @param spotcheck
	 * @return
	 */
	int modifySpotcheck(Spotcheck spotcheck);
	/**
	 * 查
	 * @param spotcheck
	 * @return
	 */
	List<Spotcheck> getSpotcheckList(Spotcheck spotcheck);
	
	/**
	 * 根据单位id查询含该单位人员的抽查计划
	 * @param entid
	 * @return
	 */
	List<Spotcheck> getSpotcheckListByEntid(String entid,Spotcheck spotcheck);

	
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	Spotcheck getSpotcheckById(Integer id);
	/**
	 * 批量修改地区确认状态
	 * @param idlist
	 * @return
	 */
	int modifyDqenter(List<Integer> idlist);
	
	/**
	 * 批量修改协会确认状态
	 * @param idlist
	 * @return
	 */
	int modifyXhEnter(List<Integer> idlist);
	
	/**
	 * 设置该记录为已读
	 * @param id
	 * @return
	 */
	int setIsRead(Integer id);

	/**
	 * 查询这条记录的信息
	 */
	Spotcheck selectSpotcheckInfos(Integer id);

	/**
	 * sxh根据条件查询
	 * @param
	 * @return
	 */
	List<Spotcheck> selectXhJdSpotcheckByDetail(Spotcheck spotcheck);

	/**
	 * 更新计划的xhjd
	 * @name  int updateSpotcheckXhjd(Spotcheck spotcheck);
	 */
	int updateSpotcheckXhjd(SpotcheckMember spotcheckMember);

	//删除协会监督计划的总接口
	int deleteXhSpotcheck(Integer id);

	/**
	 * 删除协会计划的分支接口
	 */
	int deleteXhspotecheck(Integer id);

	/**
	 * 插入协会计划spid
	 */
	int insertSpid(Integer spid,Integer jdspid);

	/**
	 * 查询xhjdSpid
	 */
	List<Integer>getselectSpid(Integer jdspid);
}
