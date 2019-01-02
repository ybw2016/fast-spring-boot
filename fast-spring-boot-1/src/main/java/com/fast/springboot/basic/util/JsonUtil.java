package com.fast.springboot.basic.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                //.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL, "META");
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    /**
     * java对象转换为json字符串.
     *
     * @param object target object
     * @return json format String
     */
    public static String toJsonString(Object object) {
        if (object instanceof String) {
            return object.toString();
        }
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转换为默认{@link Map}类型.
     * <p>
     * json字符串转换为默认<code>Map&lt;String, Object&gt;</code>类型.
     * </p>
     *
     * @param json String of json
     * @return {@link Map}
     */
    public static Map fromJson(String json) {
        return fromJson(json, Map.class);
    }

    /**
     * json字符串转换为指定类型java对象.
     *
     * @param json 需要转换的json字符串
     * @param type 转换后的java类型
     * @return target class of type
     */
    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转换为指定类型java对象.
     *
     * @param src  输入流
     * @param type 转换后的java类型
     * @return target class of type
     */
    protected static <T> T fromJson(InputStream src, Class<T> type) {
        try {
            return mapper.readValue(src, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转换为指定类型的java对象(用于复杂类型的转换).
     *
     * @param json 需要转换的json字符串
     * @param type 转换后的java类型
     * @param <T>  {@link TypeReference}
     * @return class of the type
     */
    public static <T> T fromJson(String json, TypeReference type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json字符串转换为指定类型的java对象(用于复杂类型的转换).
     *
     * @param src  需要转换的输入流
     * @param type 转换后的java类型
     * @param <T>  {@link TypeReference}
     * @return class of the type
     */
    protected static <T> T fromJson(InputStream src, TypeReference type) {
        try {
            return mapper.readValue(src, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 数组类型json字符串转换为java对象{@link List}.
     *
     * @param json  json string
     * @param clazz java type
     * @return List&lt;T&gt;
     */
    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 数组类型json字符串转换为java对象{@link List}.
     * <p>
     * 数组类型json字符串转换为java对象List&lt;Map&lt;String,Object&gt;&gt;
     * </p>
     *
     * @param json json String
     * @return List&lt;Map&lt;String, Object&gt;&gt;
     */
    public static List<Map<String, Object>> fromJsonArray(String json) {
        TypeReference<List<Map<String, Object>>> type = new TypeReference<List<Map<String, Object>>>() {
        };
        //type.getType().getTypeName();
        return fromJson(json, type);
    }

}
