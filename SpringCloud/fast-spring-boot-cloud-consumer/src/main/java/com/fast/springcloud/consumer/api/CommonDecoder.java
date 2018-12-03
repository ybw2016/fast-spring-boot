package com.fast.springcloud.consumer.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class CommonDecoder extends SpringDecoder {
    protected static ObjectMapper mapper = new ObjectMapper();

    /**
     * 数据样例一：
     * {
     * "code": 0,
     * "msg": "",
     * "data": {
     * "list": [  ->  DataKey为list
     * {
     * "name": "zhangsan",
     * "age": 20
     * },
     * {
     * "name": "lisi",
     * "age": 25
     * }
     * ]
     * }
     * }
     * 数据样例二：
     * {
     * "code": 0,
     * "msg": "",
     * "data": {
     * "items": [  ->  DataKey为items
     * {
     * "name": "zhangsan",
     * "age": 20
     * },
     * {
     * "name": "lisi",
     * "age": 25
     * }
     * ]
     * }
     * }
     */
    protected abstract String getDataKey();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public CommonDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }


    /**
     * 转换泛型列表.
     *
     * @param type    类型
     * @param dataMap 数据
     * @return Object
     */
    public Object convertGenericList(ParameterizedType type, LinkedHashMap<String, Object> dataMap) {
        return convertGenericList(type, dataMap, getDataKey());
    }

    /**
     * 转换泛型列表.
     *
     * @param type    类型
     * @param dataMap 数据
     * @return Object
     */
    public Object convertGenericList(ParameterizedType type, LinkedHashMap<String, Object> dataMap, String dataKey) {
        JavaType beanType = getJavaType(type);
        List<LinkedHashMap> linkedHashMapList = (List<LinkedHashMap>) dataMap.get(dataKey);
        return MapUtils.isEmpty(dataMap) ? null : mapper.convertValue(linkedHashMapList, beanType);
    }

    /**
     * 转换泛型列表.
     *
     * @param type              类型
     * @param linkedHashMapList 数据
     * @return Object
     */
    public Object convertGenericList(ParameterizedType type, List<LinkedHashMap> linkedHashMapList) {
        JavaType javaType = getJavaType(type);
        return mapper.convertValue(linkedHashMapList, javaType);
    }

    private JavaType getJavaType(ParameterizedType type) {
        Type actualType = type.getActualTypeArguments()[0];
        return mapper.getTypeFactory().constructParametricType((Class) type.getRawType(), (Class) actualType);
    }
}
