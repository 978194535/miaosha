package com.miaoshaproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.hash;

@ConfigurationProperties("bloom.filter")
@Component
public class RedisBloomFilter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    //位数组的长度
    private long numbits;
    //hash次数
    private int numHashFunctions;
    //数据量
    private long expectedInst;
    //容错率
    private double fpp;

    @PostConstruct
    public void init(){
        this.numbits=optNumOfBit(expectedInst,fpp);
        this.numHashFunctions=optNumOfHash(expectedInst,numbits);
    }
//计算Hash函数的长度
    public int optNumOfHash(long n,long m){
        return Math.max(1,(int)Math.round((double)m/n*Math.log(2)));
    }
    //计算bit数组的长度
    public long optNumOfBit(long n,double p){
        if(p==0){
            p=Double.MIN_VALUE;
        }
        return (long)(-n*Math.log(p)/(Math.log(2)*Math.log(2)));
    }
    private long[] getIndex(String key){
        long hash1 = hash(key);
        long hash2 = hash1 >>> 16;
        long[] result = new long[numHashFunctions];
        for(int i = 0; i<numHashFunctions;i++){
            long comHash = hash1+i*hash2;
            if(comHash<0){
                comHash=~comHash;
            }
            result[i]=comHash%numbits;
        }
        return result;
    }

    public void put(String key){
        long[] indexs = getIndex(key);
        ValueOperations<String,Object> ops = redisTemplate.opsForValue();
        for(long index:indexs) {
            ops.setBit("wt", index, true);
        }
    }
    public Boolean isExist(String key){
        List list = new ArrayList();

        long[] indexs = getIndex(key);
        ValueOperations<String,Object> ops = redisTemplate.opsForValue();
        for(int i = 0; i<numHashFunctions;i++) {
            Boolean a = ops.getBit("wt", indexs[i]);
            list.set(i, a);
        }
        return !list.contains(false);
    }
}
