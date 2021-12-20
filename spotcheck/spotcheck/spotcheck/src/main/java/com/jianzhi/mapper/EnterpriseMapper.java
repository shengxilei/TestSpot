package com.jianzhi.mapper;

import org.apache.ibatis.annotations.Param;

import com.jianzhi.model.Enterprise;

public interface EnterpriseMapper {
    int deleteByPrimaryKey(String id);

    int insert(Enterprise record);

    int insertSelective(Enterprise record);

    Enterprise selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Enterprise record);

    int updateByPrimaryKey(Enterprise record);
    
    /**
     * 根据账号和密码查询登录
     * @param zh
     * @param mm
     * @return
     */
    Enterprise selectByZhAndMM(@Param("zh")String zh,@Param("mm")String mm);
    
    /**
     * 修改密码
     * @param id 主键
     * @param newpassword 新密码
     * @return
     */
    int updatePassword(@Param("id")String id,@Param("newpassword")String newpassword);
}