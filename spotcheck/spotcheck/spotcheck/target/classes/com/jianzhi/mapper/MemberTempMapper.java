package com.jianzhi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jianzhi.model.MemberTemp;
import com.jianzhi.model.Pxbm;

public interface MemberTempMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberTemp record);

    int insertSelective(MemberTemp record);

    MemberTemp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberTemp record);

    int updateByPrimaryKey(MemberTemp record);
    
    /**
     * 批量新增
     * @param memberTempList
     * @return
     */
    int insertMemberTempList(@Param("memberTempList")List<MemberTemp> memberTempList);
    
    /**
     * 导入
     * @param memberTempList
     * @return
     */
    int inputMemberTempList(@Param("memberTempList")List<MemberTemp> memberTempList);
    
    /**
     * 根据条件查询
     * @param record
     * @return
     */
    List<MemberTemp> selectMsemberTempByDetail(MemberTemp memberTemp);

    /**
     * 根据idList批量删除
     * @param idList
     * @return
     */
	int deleteTempByIdList(@Param("idList")List<Integer> idList);
    

	/**
     * 将Excel表里的抽查人员导入到正式表汇总，插入进去
	 */
	int insertSpokenMembers(MemberTemp memberTemp);
}