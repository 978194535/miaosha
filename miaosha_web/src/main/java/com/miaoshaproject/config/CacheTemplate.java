package com.miaoshaproject.config;

import com.miaoshaproject.response.commonReturnType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

public class CacheTemplate {
    @Autowired
    RedisBloomFilter redisBloomFilter;
    @Autowired
    RedisLock redisLock;
    public commonReturnType redisaa(String key, long expire, TimeUnit timeUnit,CacheLoadAble cacheLoadAble){
        Object a = cacheLoadAble.load();
        return commonReturnType.create(null);
    }

}
