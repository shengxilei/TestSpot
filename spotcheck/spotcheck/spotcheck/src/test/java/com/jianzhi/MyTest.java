package com.jianzhi;

import com.jianzhi.mapper.EnterpriseMapper;
import com.jianzhi.model.Enterprise;
import com.jianzhi.service.SpotcheckService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

public class MyTest {
    @Resource
    EnterpriseMapper enterpriseMapper;
    @Test
    public void stat(){

        System.out.println("开始");

        Enterprise ent= enterpriseMapper.selectByZhAndMM("sxh","123456");

        System.out.println(ent);

    }
}
