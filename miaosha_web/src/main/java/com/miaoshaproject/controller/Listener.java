package com.miaoshaproject.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@Configuration
public class Listener {

    @KafkaListener(id = "t1", topics = "t1")
    public void listenT1(ConsumerRecord<?, ?> cr) throws Exception {
        log.info("接收消息t1：{} - {} : {}", cr.topic(), cr.key(), cr.value());
    }

    @KafkaListener(id = "t2", topics = "t2")
    public void listenT2(ConsumerRecord<?, ?> cr) throws Exception {
        log.info("接收消息t2：{} - {} : {}", cr.topic(), cr.key(), cr.value());
    }

}