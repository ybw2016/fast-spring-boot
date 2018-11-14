package kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.UUID;

/**
 * 参考链接：https://www.cnblogs.com/JreeyQi/p/9414726.html?utm_source=debugrun&utm_medium=referral
 *
 * @author bowen.yan
 * @date 2018-11-12
 */
@Configuration
@EnableScheduling
public class KafkaService {
    @Autowired
    private KafkaSender<User> kafkaSender;

    @Scheduled(cron = "00/1 * * * * ?")
    public void kafkaSend() throws InterruptedException {
        //模拟发消息
        for (int i = 0; i < 5; i++) {

            User user = new User();
            user.setId(System.currentTimeMillis());
            user.setMsg(UUID.randomUUID().toString());
            user.setSendTime(new Date());

            kafkaSender.send(user);
            Thread.sleep(3000);
        }
    }
}
