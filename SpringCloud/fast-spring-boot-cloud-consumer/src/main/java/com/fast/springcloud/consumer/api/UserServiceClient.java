package com.fast.springcloud.consumer.api;

import com.fast.springcloud.consumer.constant.UserSysErrorConstants;
import com.fast.springcloud.consumer.dto.UserSysReq;
import com.fast.springcloud.consumer.dto.UserSysRsp;
import com.fast.springcloud.consumer.dto.UserSysRspBizBase;
import com.fast.springcloud.consumer.dto.request.UserInfoReq;
import com.fast.springcloud.consumer.dto.response.UserInfoListRsp;
import com.fast.springcloud.consumer.dto.response.UserInfoRsp;
import com.fast.springcloud.consumer.exception.ApiBizException;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.Retryer;
import feign.codec.EncodeException;
import lombok.extern.slf4j.Slf4j;

//@Lazy
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

    // 此接口返回对象为：List<UserInfoRsp>，没有实现UserSysRspBase基类，因此无法自动翻译错误码
    @Deprecated
    @RequestMapping(
            value = "/user-info-list", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    List<UserInfoRsp> queryUserListInfo2(@RequestBody UserInfoReq userInfoReq);

    /**
     * client自定义配置.
     */
    @Slf4j
    class ClientConfiguration {
        @Value("${userService.feign.request.connect.timeout.millis:10000}")
        private int connectTimeoutMillis;// 链接超时时间
        @Value("${userService.feign.request.read.timeout.millis:5000}")
        private int readTimeoutMillis;//请求处理时间
        @Value("${userService.feign.request.retry.period:5000}")
        private int retryPeriod;//重试间隔
        @Value("${userService.feign.request.retry.max.period:10000}")
        private int retryMaxPeriod;//最大重试间隔
        @Value("${userService.feign.request.retry.max.attempts:1}")
        private int retryMaxAttempts;//最大重试次数

        @Bean
        public Request.Options feignOptions() {
            // timeout设置
            return new Request.Options(connectTimeoutMillis, readTimeoutMillis);
        }

        @Bean
        public Retryer feignRetryer() {
            // 重试次数配置
            return new Retryer.Default(retryPeriod, retryMaxPeriod, retryMaxAttempts);
        }

        /**
         * 日志级别
         * 1. NONE : 不记录任何日志(默认)
         * 2. BASIC : 仅记录请求方法,url,响应状态码和执行时间
         * 3. HEADERS : 在basic的基础上,记录请求和响应的header
         * 4. FULL : 记录请求响应的header,body和元数据
         */
        @Bean
        feign.Logger.Level feignLoggerLevel() {
            return feign.Logger.Level.FULL;
        }

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
                UserSysReq userSysReq = (UserSysReq) requestBody;
                userSysReq.setFromSystem("spring-boot-cloud-consumer");
                userSysReq.setSerialId(UUID.randomUUID().toString());
                userSysReq.setTimestamp(Instant.now().toEpochMilli());
                super.encode(requestBody, bodyType, request);
            }
        }

        @Bean
        public feign.codec.Decoder userServiceDecoder() {
            return new ResponseEntityDecoder(new UserServiceDecoder(this.messageConverters));
        }

        private static class UserServiceDecoder extends CommonDecoder {
            private static final String USER_SERVICE_SUCCESS_CODE = "0";

            @Override
            protected String getDataKey() {
                return "list";
            }

            public UserServiceDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
                super(messageConverters);
            }

            @Override
            public Object decode(Response response, Type returnType) throws IOException, FeignException {
                // 基础对象接收json值
                UserSysRsp userSysRsp = (UserSysRsp) super.decode(response, UserSysRsp.class);
                printLog(response, userSysRsp);

                if (userSysRsp == null || userSysRsp.getCode() == null) {
                    throw new ApiBizException("接口访问错误");
                }

                UserSysRspBizBase userSysRspBizBase;
                if (returnType instanceof ParameterizedType) {
                    // 将data里面的数据转换："data":{ "list":[ beanObj1, beanObj2] } -> List<bean>
                    LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) userSysRsp.getData();
                    userSysRspBizBase = (UserSysRspBizBase) convertGenericList((ParameterizedType) returnType, dataMap);
                } else {
                    // 将data里面的数据转换成bean
                    userSysRspBizBase = mapper.convertValue(userSysRsp.getData(), mapper.constructType(returnType));
                }

                userSysRspBizBase = ensureInstance(userSysRspBizBase, returnType);
                // 绑定原始返回错误码
                userSysRspBizBase.setRawErrorCode(userSysRsp.getCode());
                userSysRspBizBase.setRawErrorMsg(userSysRsp.getMsg());

                // 绑定自定义错误码
                if (USER_SERVICE_SUCCESS_CODE.equals(userSysRsp.getCode())) {
                    userSysRspBizBase.setBusinessError(UserSysErrorConstants.BIZ_SUCCESS);
                } else {
                    userSysRspBizBase.setBusinessError(userSysRspBizBase.getErrorMap().get(userSysRsp.getCode()));
                }
                return userSysRspBizBase;
            }

            private UserSysRspBizBase ensureInstance(UserSysRspBizBase response, Type returnType) {
                if (response == null) {
                    try {
                        response = (UserSysRspBizBase) ReflectUtils.newInstance(Class.forName(returnType.getTypeName()));
                    } catch (Exception ex) {
                        throw new ApiBizException("创建对象实例错误", ex);
                    }
                }
                return response;
            }

            private void printLog(Response response, UserSysRsp userSysRsp) {
                StringBuilder logBuilder = new StringBuilder();
                logBuilder.append("\n======================请求响应参数======================");
                logBuilder.append("\nrequestUrl: ").append(response.request().url());
                logBuilder.append("\nrequestParam: ").append(getRequestParam(response));

                try {
                    logBuilder.append("\nresponseData : ").append(mapper.writeValueAsString(userSysRsp));
                    log.info("UserServiceClient -> {}", logBuilder.toString());
                } catch (JsonProcessingException e) {
                    log.error("UserServiceClient调用http接口异常！", e);
                }
            }

            private String getRequestParam(Response response) {
                if (HttpMethod.GET.name().equals(response.request().method())) {
                    return StringUtils.EMPTY;
                }
                return new String(response.request().body());
            }
        }
    }
}