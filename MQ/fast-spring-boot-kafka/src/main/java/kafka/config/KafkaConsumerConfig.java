package kafka.config;

/**
 * @author bowen.yan
 * @date 2018-11-12
 */
//@Configuration
//@EnableKafka
public class KafkaConsumerConfig {
//    @Value("${kafka.consumer.bootstrap-servers}")
//    private String servers;
//    @Value("${kafka.consumer.enable.auto.commit}")
//    private boolean enableAutoCommit;
//    @Value("${kafka.consumer.session.timeout}")
//    private String sessionTimeout;
//    @Value("${kafka.consumer.auto.commit.interval}")
//    private String autoCommitInterval;
//    @Value("${kafka.consumer.group.id}")
//    private String groupId;
//    @Value("${kafka.consumer.auto.offset.reset}")
//    private String autoOffsetReset;
//    @Value("${kafka.consumer.concurrency}")
//    private int concurrency;
//
//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        factory.setConcurrency(concurrency);
//        factory.getContainerProperties().setPollTimeout(1500);
//        return factory;
//    }
//
//    public ConsumerFactory<String, String> consumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
//    }
//
//    public Map<String, Object> consumerConfigs() {
//        Map<String, Object> propsMap = new HashMap<>();
//        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
//        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
//        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
//        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
//        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
//        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
//        return propsMap;
//    }
//
//    @Bean
//    public Listener listener() {
//        return new Listener();
//    }
}