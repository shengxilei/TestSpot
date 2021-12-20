package com.jianzhi.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

@Service
public class RedisService {
	@Resource
    private RedisTemplate<String,Object> redisTemplate;

    public void set(String key, Object value) {
        //更改在redis里面查看key编码问题
        RedisSerializer redisSerializer =new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        ValueOperations<String,Object> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }

    public Object get(String key) {
        ValueOperations<String,Object> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
    
    public int produce(String key, Integer id,Integer spid) {
        Map<String, Integer> map = Maps.newHashMap();
        map.put("id", id);
        map.put("spid", spid);
        redisTemplate.opsForList().leftPush(key, map);
        return 1;
    }

}
