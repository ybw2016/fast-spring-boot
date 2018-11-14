package com.fast.kafka.helloworld;

/**
 * 生产者
 * 使用@EnableScheduling注解开启定时任务
 */
//@Component
//@EnableScheduling
public class KafkaProducer {
//    @Autowired
//    private KafkaTemplate kafkaTemplate;
//
//    /**
//     * 定时任务
//     */
//    @Scheduled(cron = "00/1 * * * * ?")
//    public void send() {
//        String message = UUID.randomUUID().toString();
//        ListenableFuture future = kafkaTemplate.send("test", message);
//        future.addCallback(o -> System.out.println("Producer -> 消息发送成功：" + message),
//                throwable -> System.out.println("Producer -> 消息发送失败：" + message));
//    }
}
