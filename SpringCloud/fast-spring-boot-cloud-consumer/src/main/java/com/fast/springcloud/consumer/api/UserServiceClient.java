package com.fast.springcloud.consumer.api;

import com.fast.springcloud.consumer.dto.ReqBase;
import com.fast.springcloud.consumer.dto.RspBase;
import com.fast.springcloud.consumer.dto.request.UserInfoReq;
import com.fast.springcloud.consumer.dto.response.UserInfoListRsp;
import com.fast.springcloud.consumer.dto.response.UserInfoRsp;
import com.fast.springcloud.consumer.exception.ApiBizException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import feign.FeignException;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.EncodeException;
import lombok.extern.slf4j.Slf4j;

//@Lazy
@Lazy
@FeignClient(name = "${userService.url}/user", configuration = UserServiceClient.ClientConfiguration.class)
public interface UserServiceClient {
    @RequestMapping(
            value = "/user-info", method = RequestMethod.GET
            //consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    UserInfoRsp queryUserInfo(@RequestParam("username") String username, @RequestParam("mobile") String mobile);

    @RequestMapping(
            value = "/user-info-list", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    UserInfoListRsp queryUserListInfo(@RequestBody UserInfoReq userInfoReq);

    /**
     * client自定义配置.
     */
    @Slf4j
    class ClientConfiguration {
        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        public UserServiceEncoder userServiceEncoder() {
            return new UserServiceEncoder(this.messageConverters);
        }

        private static class UserServiceEncoder extends SpringEncoder implements feign.codec.Encoder {
            private UserServiceEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
                super(messageConverters);
            }

            @Override
            public void encode(Object requestBody, Type bodyType, RequestTemplate request) throws EncodeException {
                // 设置公共请求报文头
                ReqBase reqBase = (ReqBase) requestBody;
                reqBase.setFromSystem("spring-boot-cloud-consumer");
                reqBase.setSerialId(UUID.randomUUID().toString());
                reqBase.setTimestamp(Instant.now().toEpochMilli());

                try {
                    log.info("userService request -> requestBody:{}", new ObjectMapper().writeValueAsString(requestBody));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                super.encode(requestBody, bodyType, request);
            }
        }

        @Bean
        public feign.codec.Decoder userServiceDecoder() {
            return new ResponseEntityDecoder(new UserServiceDecoder(this.messageConverters));
        }

        private static class UserServiceDecoder extends CommonDecoder {
            @Override
            protected String getDataKey() {
                return "list";
            }

            public UserServiceDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
                super(messageConverters);
            }

            @Override
            public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
                RspBase baseResp = (RspBase) super.decode(response, RspBase.class);
                log.info("userService response -> baseResp:{}", mapper.writeValueAsString(baseResp));

                if (baseResp == null || baseResp.getCode() == null || baseResp.getCode() != 0) {
                    throw new ApiBizException("接口访问错误");
                }

                if (type instanceof ParameterizedType) {
                    // 将data里面的数据转换："data":{ "list":[ beanObj1, beanObj2] } -> List<bean>
                    LinkedHashMap<String, Object> items = (LinkedHashMap<String, Object>) baseResp.getData();
                    return convertGenericList((ParameterizedType) type, items);
                } else {
                    // 将data里面的数据转换成bean
                    return mapper.convertValue(baseResp.getData(), mapper.constructType(type));
                }
            }
        }

        /**
         * 构造header.
         *
         * @param headerMap map
         * @return Map
         */
        private static Map<String, Collection<String>> buildHeader(Map<String, String> headerMap) {
            Map<String, Collection<String>> map = new HashMap<>();
            if (MapUtils.isEmpty(headerMap)) {
                return map;
            }

            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                List<String> list = new ArrayList<>(1);
                list.add(entry.getValue());
                map.put(entry.getKey(), list);
            }
            return map;
        }
    }
}
