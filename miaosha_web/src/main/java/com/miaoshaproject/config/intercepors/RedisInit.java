package com.miaoshaproject.config.intercepors;

import com.miaoshaproject.config.RedisBloomFilter;
import com.miaoshaproject.dao.ItemDaoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

public class RedisInit {
    @Autowired
    ItemDaoMapper itemDaoMapper;
    @Autowired
    RedisBloomFilter redisBloomFilter;

    @PostConstruct
    public void init(){
        List<Integer> list = itemDaoMapper.selectAllId();
        for(Integer list1:list){
            redisBloomFilter.put(String.valueOf(list1));
        }
    }
}
