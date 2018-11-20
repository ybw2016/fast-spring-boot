package com.fast.springboot.basic.annotation;

import com.fast.springboot.basic.serializer.EncryptOutputSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bowen.yan
 * @date 2018-11-16
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = EncryptOutputSerializer.class)
public @interface EncryptResponseField {
    String value() default "";
}