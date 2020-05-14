package com.miaoshaproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Array;
import java.util.Arrays;

public class RedisLock {
    @Autowired
    RedisTemplate redisTemplate;
    private ThreadLocal<String> threadLocal = new ThreadLocal<>();


    public void lock(String key){
       boolean a =  trylock(key);
       if(a){
           return;
       }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock(key);
    }



    public Boolean trylock(String key){
        JedisPool jedisPool = new JedisPool();
        Jedis resource = jedisPool.getResource();
//        Long lock1 = resource.setnx("lock", key);
//        if(lock1==1L){
//            resource.expire("lock",10);
//            return true;
//        }
//        return false;
        String lock = resource.set("lock",key,"NX","PX",5000);
        threadLocal.set(key);
        resource.close();
        if("OK".equals(lock)){
            return true;
        }
        return false;
    }

    public void unlock(String key) throws Exception {
//        JedisPool jedisPool = new JedisPool();
//        Jedis resource = jedisPool.getResource();
//        resource.del(key);
        String script="if redis.call(\"get\",KEYS[1]==ARGV[1]) then\n" +
                "            return redis.call(\"del\",KEYS[1])\n" +
                "        else\n" +
                "            return 0\n" +
                "        end";
        JedisPool jedisPool = new JedisPool();
        Jedis resource = jedisPool.getResource();
        Object eval1 = resource.eval(script,Arrays.asList(key),Arrays.asList(threadLocal.get()));
        if(Integer.valueOf(eval1.toString())==0){
            resource.close();
            throw new Exception("解锁失败");
        }else {
            resource.close();
        }

    }


}
