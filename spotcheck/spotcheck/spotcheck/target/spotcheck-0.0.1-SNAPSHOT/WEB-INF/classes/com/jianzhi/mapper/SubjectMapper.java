package com.jianzhi.mapper;

import com.jianzhi.model.Subject;

public interface SubjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Subject record);

    int insertSelective(Subject record);

    Subject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Subject record);

    int updateByPrimaryKey(Subject record);

    /**
     * 根据科目名搜索科目代码
     * @param name
     * @return
     */
	String getSubjectByname(String name);
}