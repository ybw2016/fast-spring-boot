package kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2018-11-12
 */
@Component
@Slf4j
public class KafkaReceiver {
    /**
     * 监听kafka.tut 的 topic
     *
     * @param topic topic
     */
    @KafkaListener(id = "tut", topics = "kafka.tut")
    public void listen(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        //判断是否NULL
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            //获取消息
            Object message = kafkaMessage.get();

            log.info("Receive： +++++++++++++++ Topic:" + topic);
            log.info("Receive： +++++++++++++++ Record:" + record);
            log.info("Receive： +++++++++++++++ Message:" + message);
            System.out.println();
        }
    }
}