package com.miaoshaproject.controller;

import com.miaoshaoproject.service.Model.OrderModel;
import com.miaoshaoproject.service.OrderService;
import com.miaoshaproject.config.RedisUtil;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@Configuration
public class Listener {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OrderService orderService;
    @KafkaListener(id = "t1", topics = "t1")
    public void listenT1(ConsumerRecord<?, ?> cr) throws Exception {
        log.info("接收消息t1：{} - {} : {}", cr.topic(), cr.key(), cr.value());
        String key = (String) cr.key();
        Integer num = (Integer) redisUtil.get(key);
        if(num==0){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"秒杀失败");
        }else {
            String number = (String) cr.value();
            //异步处理方法
            OrderModel orderModel = orderService.createOrder(1,1,1,1);
            num = num-1;
            redisUtil.set(key,num);
        }
    }

    @KafkaListener(id = "t2", topics = "t2")
    public void listenT2(ConsumerRecord<?, ?> cr) throws Exception {
        log.info("接收消息t2：{} - {} : {}", cr.topic(), cr.key(), cr.value());
    }

}