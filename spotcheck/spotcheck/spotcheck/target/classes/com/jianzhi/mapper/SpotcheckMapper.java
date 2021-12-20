package com.jianzhi.mapper;

import java.util.Date;
import java.util.List;

import com.jianzhi.model.Pxbm;
import com.jianzhi.model.SpotcheckMember;
import org.apache.ibatis.annotations.Param;

import com.jianzhi.model.Spotcheck;

public interface SpotcheckMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Spotcheck record);

    int insertSelective(Spotcheck record);

    Spotcheck selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Spotcheck record);

    int updateByPrimaryKey(Spotcheck record);
    
    /**
     * 删除该条记录 把isdelete设置1为删除
     * @param id
     * @return
     */
    int deleteZeroById(Integer id);
    
    /**
     * 根据条件查询
     * @param record
     * @return
     */
    List<Spotcheck> selectSpotcheckByDetail(Spotcheck spotcheck);
    
    /**
     * 查询包含该单位所有抽查记录
     * @param entid
     * @param spotcheck
     * @return
     */
    List<Spotcheck> selectSpotcheckListByEntid(@Param("entid")String entid,@Param("yyyy")String yyyy,@Param("num")String num,@Param("kmm")String kmm,@Param("status")String status,@Param("now")String now,@Param("name")String name,@Param("areaName")String areaName);
   // List<Spotcheck> selectSpotcheckListByEntid(@Param("entid")String entid, @Param("yyyy")String yyyy, @Param("num")String num, @Param("kmm")String kmm, @Param("status")String status, @Param("now")String now, @Param("start")Date start,@Param("end")Date end, @Param("name")String name, @Param("areaName")String areaName);

    /**
     * 批量修改地区确认状态
     * @param idlist
     * @return
     */
	int modifyDqenter(@Param("idlist")List<Integer> idlist);
/**
 * 批量修改协会检阅状态
 * @param idlist
 * @return
 */
	int modifyXhEnter(@Param("idlist")List<Integer> idlist);
	
	/**
	 * 设置该记录为已读状态
	 * @param id
	 * @return
	 */
	int setIsRead(Integer id);

    /**
     *查询这条记录的基本信息
     * @param id
     * @return
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
     * @name updateSpotcheckXhjd
     */
    int updateSpotcheckXhjd(SpotcheckMember spotcheckMember);

    /**
     * 协会创建监督计划删除
     */
   int deleteXhspotecheck(Integer id);

    /**
     * 将协会监督ID插入spid表中
     */
    int insertSpid(Integer spid,Integer jdspid);

    /**
     * 查询xhjdSpid
     */
    List<Integer>getselectSpid(Integer jdspid);
}