package com.jianzhi.service;

import java.util.List;

import com.jianzhi.model.Pxbm;
import com.jianzhi.model.SpotcheckMember;

public interface PxbmService {

	/**
	 * 查询总人员
	 * @param
	 * @return
	 */
	List<Pxbm> getPxbmList(Pxbm pxbm);
	
	/**
	 * 修改承诺书社保状态备注 如临时表中存在由mysql触发器触发更新临时表
	 * @param cns 0未设 1有 2无
	 * @param sb  0未设 1有 2无
	 * @param bz2   备注
	 * @param id  主键
	 * @return 1返回成功
	 */
	int modifyCNSandSB(Integer cns,Integer sb,String bz2,String id);
	
	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	Pxbm getPxbmById(String id);
	
	/**
	 * 更新是否已被分配
	 * @param idList 主键集合
	 * @param spid 被分配的记录id
	 * @return
	 */
	int updateDistribution(List<String> idList,Integer spid);

	/**
	 * 清空pxbm表的已分配状态spid
	 * @param
	 * @return
	 */
	int clearSpid(Integer spid);
	
	/**
	 * 设置申请修改状态
	 * @param isApply
	 * @param id
	 * @return
	 */
	int setIsApply(Integer isApply,String apply,String reply, String id);

	/**
	 * 更新pxbm表中的实操结果
	 * @param idlist
	 * @param jg
	 * @return
	 */
	int  updateJgStatus(List<Integer> idlist, String jg);



	/**
	 * 查询Excel中导入人员行的身份证/培训科目/姓名是否对应
	 * @param pxbm
	 * @param pxbm
	 * @return List
	 */
	Pxbm getPxbmExcelList(Pxbm pxbm);

	/**
	 * 更新人员单位名称
	 * @param pxbm
	 * @param pxbm
	 * @return int
	 */
    int updatePxbmExcelList(Pxbm pxbm);

	/**
	 * 更新人员单位名称
	 * @return int
	 */
    int updatePxbmInfo(Pxbm pxbm);


	/**
	 * 查询添加人员是否在抽查计划
	 */
	int selecctPxbmId(Pxbm pxbm);

	/**
	 * 导入实操结果，更新实操结果
	 */
	int updateOpZt(Pxbm pxbm);

	/**
	 * 查询人员承诺书
	 */
	Pxbm getSelectisCNS(Pxbm pxbm);

	/**
	 * 导入人员承诺书
	 */
	int updateIsCNS(Pxbm pxbm);

	/**
	 * 抽查人员导入
	 */
	int updateSpokeMembers(Pxbm pxbm);

	/**
	 * 查询抽查人员
	 *
	 */
	Pxbm getSelectSpokeMembers(Pxbm pxbm);

	/**
	 * 省协会登录时查询Excel表里的全部抽查人员
	 */
//	Pxbm getSelectSXHSpokeMembers(Pxbm pxbm);

	/**
	 * 省协会登录Excel导入抽查人员
	 *
	 */
	int updateSXHSpokeMembers(Pxbm pxbm);

	int updateIsFp(Integer spid);

	/**
	 * 查询实操结果
	 */

	Pxbm selectPxbmOpzt(Pxbm pxbm);

	/**
	 * 同步本次计划执行完的所有人员信息
	 */
	List<Pxbm> selectPxbmInformations(Integer spid);

	/**
	 * 更新p_bj公示字段
	 */
	int updateP_bj(List<Integer> idlist, Integer p_bj);


	/**
	 * 监督计划分配
	 */
	int update_jdfpspid(Pxbm pxbm);
}
