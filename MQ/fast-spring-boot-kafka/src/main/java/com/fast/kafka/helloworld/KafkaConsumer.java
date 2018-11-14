package com.fast.kafka.helloworld;

import org.springframework.stereotype.Component;

/**
 * 消费者
 * 监听kafka服务器上的消息
 */
@Component
public class KafkaConsumer {
//    @KafkaListener(topics = {"test"})
//    public void receive(String content) {
//        System.err.println("Consumer -> 消息接收成功：" + content);
//    }
}
