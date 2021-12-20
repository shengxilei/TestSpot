package com.jianzhi.mapper;

import java.util.List;

import com.jianzhi.model.Job;

public interface JobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Job record);

    int insertSelective(Job record);

    Job selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Job record);

    int updateByPrimaryKey(Job record);
    
    /**
     * 查询未被使用的定时器开始时间
     * @return
     */
    List<Job> getJobList();
    /**
     * 根据spid设置为已使用状态1
     * @param spid
     * @return
     */
    int updateJobUsed(Integer spid);
    
    /**
     * 根据spid更新时间
     * @param record
     * @return
     */
    int updateStratDate(Job record);
    /**
     * 根据spid删除
     * @param spid
     * @return
     */
    int deleteJobBySpid(Integer spid);
}