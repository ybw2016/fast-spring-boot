package com.fast.springboot.basic.serializer;

import com.fast.springboot.basic.annotation.EncryptResponseField;
import com.fast.springboot.basic.util.Base64Util;
import com.fast.springboot.basic.util.ReflectionUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.lang.reflect.Field;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncryptOutputSerializer extends JsonSerializer<Object> {
    public EncryptOutputSerializer() {
    }

    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        Object obj = jsonGenerator.getCurrentValue();
        String fieldName = jsonGenerator.getOutputContext().getCurrentName();

        Field field = ReflectionUtil.getDeclaredField(obj, fieldName);
        if (field == null) {
            return;
        }

        if (field.getAnnotation(EncryptResponseField.class) == null) {
            jsonGenerator.writeObject(value);
        } else {
            String newValue = Base64Util.encrypt(value.toString());
            log.info("EncryptOutputSerializer -> key:{}, rawValue:{}, newValue:{}", fieldName, value, newValue);
            jsonGenerator.writeString(newValue);
        }
    }
}