package kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2018-11-12
 */
@Slf4j
public class Listener {
//    @KafkaListener(topics = {"test"})
//    public void listen(ConsumerRecord<?, ?> record) {
//        log.info("kafka的key: " + record.key());
//        log.info("kafka的value: " + record.value().toString());
//    }
}
