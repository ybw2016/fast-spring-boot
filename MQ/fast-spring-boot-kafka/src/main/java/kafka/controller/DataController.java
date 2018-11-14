package kafka.controller;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bowen.yan
 * @date 2018-11-12
 */
//@RestController
//@RequestMapping("/kafka")
@Slf4j
public class DataController {
//    @Autowired
//    private KafkaTemplate kafkaTemplate;
//
//    @RequestMapping(value = "/send", method = RequestMethod.GET)
//    public Response sendKafka(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            String message = request.getParameter("message");
//            log.info("kafka的消息={}", message);
//            kafkaTemplate.send("test", "key", message);
//            log.info("发送kafka成功.");
//            return new Response(ResultCode.SUCCESS, "发送kafka成功", null);
//        } catch (Exception e) {
//            log.error("发送kafka失败", e);
//            return new Response(ResultCode.EXCEPTION, "发送kafka失败", null);
//        }
//    }
}
