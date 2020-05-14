package com.miaoshaproject.controller;

import com.miaoshaproject.config.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

@Slf4j
@RestController
public class KafkaController {
    @Autowired
    private KafkaTemplate<String, String> template;
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/send")
    String send(String topic,String key, String data) {
        log.info("发送消息：{} - {} : {}", topic, key, data);
        ListenableFuture<SendResult<String, String>> send = template.send(topic, key, data);
        return "success";
    }

}
