package com.fast.springboot.basic.annotation;

import com.fast.springboot.basic.serializer.DecryptInputSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bowen.yan
 * @date 2018-11-16
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonDeserialize(using = DecryptInputSerializer.class)
public @interface DecryptPostJson {
    String value() default "";

    boolean required() default true;
}