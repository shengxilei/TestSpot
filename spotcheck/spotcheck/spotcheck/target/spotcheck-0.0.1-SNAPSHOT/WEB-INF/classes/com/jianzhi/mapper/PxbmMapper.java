package com.jianzhi.mapper;

import java.util.List;

import com.jianzhi.model.SpotcheckMember;
import org.apache.ibatis.annotations.Param;

import com.jianzhi.model.Pxbm;

public interface PxbmMapper {
    int deleteByPrimaryKey(String id);

    int insert(Pxbm record);

    int insertSelective(Pxbm record);

    Pxbm selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Pxbm record);

    int updateByPrimaryKey(Pxbm record);
    
    /**
     * 根据条件查询
     * @param record
     * @return
     */
    List<Pxbm> selectPxbmByDetail(Pxbm pxbm);
    
    /**
     * 修改状态
     * @param cns 承诺书
     * @param sb  社保
     * @param bz2 备注
     * @param id  主键
     * @return 1成功
     */
    int updateCNSorSBorBZ2(@Param("cns")Integer cns,@Param("sb")Integer sb,
    		@Param("bz2")String bz2,@Param("id")String id);
    
    /**
     * 
     * @param idList
     * @param spid
     * @return
     */
    int updateDistribution(@Param("idList") List<String> idList,Integer spid);

    /**
     * 清空spid中的已分配状态
     * @param id
     * @return
     */
	int clearSpid(Integer spid);
	
	/**
	 * 设置申请修改状态
	 * @param isApply
	 * @param id
	 * @return
	 */
	int setIsApply(@Param("isApply")Integer isApply,
			@Param("apply")String apply,@Param("reply")String reply,@Param("id")String id);

	/**
	 * 更新pxbm表中的实操结果
	 * @param idlist
	 * @param jg
	 * @return
	 */
	int updateJgStatus(List<Integer> idlist, String jg);

	/**
	 * 查询Excel导入人员信息
	 */
	Pxbm getPxblExcelList(Pxbm pxbm);

	/**
	 * 更新人员单位名称
	 */
	int updatePxbmExcelList(Pxbm pxbm);

	/**
	 * 更新人员单位名称
	 */
	int updatePxbmInfo(Pxbm pxbm);

	/**
	 * 查询人员是否在抽查计划中
	 */
	//SpotcheckMapper selecctPxbmId(Pxbm pxbm);

	/**
	 * 导入实操结果
	 */
	int updateOpZt(Pxbm pxbm);

	/**
	 * 查询人员承诺书
	 */
	Pxbm getSelectisCNS(Pxbm pxbm);

	/**
	 * 导入承诺书
	 */
	int updateIsCNS(Pxbm pxbm);

	/**
	 * 抽查人员导入
	 */
     int updateSpokeMembers(Pxbm pxbm);

     /**
	  * 查询导入的抽查人员信息
	  * */
     Pxbm getSelectSpokeMembers(Pxbm pxbm);

	/**
	 * 查询是是否是省协会登录，查询Excel所有抽查人员
	 */
	Pxbm getSelectSXHSpokeMembers(Pxbm pxbm);


	int updateSXHSpokeMembers(Pxbm pxbm);

	int updateIsFp(Integer spid);

	Pxbm selectPxbmOpzt(Pxbm pxbm);

	/**
	 * 根据人员ID更新图片地址
	 * @param pxbm
	 * @return int
	 * Pxbm
	 */
	int uploadImgPxbm(Pxbm pxbm);

	/**
	 *
	 */
	List<Pxbm> selectPxbmInformations(Integer spid);

	/**
	 *根据人员pxbmId和Spid来删除图片地址
	 */
	int deleteImgurl(Pxbm pxbm);

	/**
	 * 更新p_bj公示字段
	 */
	int updateP_bj(List<Integer> idlist, Integer p_bj);

	/**
	 * 监督计划分配人员
	 */
	int update_jdfpspid(Pxbm pxbm);
}