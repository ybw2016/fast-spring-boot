package com.fast.springboot.basic.serializer;

import com.fast.springboot.basic.util.Base64Util;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncryptOutputSerializer extends JsonSerializer<Object> {
    public EncryptOutputSerializer() {
    }

    @Override
    public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        String fieldName = jsonGenerator.getOutputContext().getCurrentName();
        String newValue = Base64Util.encrypt(value.toString());
        log.info("EncryptOutputSerializer -> fieldName:{}, rawValue:{}, newValue:{}", fieldName, value, newValue);
        jsonGenerator.writeString(newValue);
    }
}