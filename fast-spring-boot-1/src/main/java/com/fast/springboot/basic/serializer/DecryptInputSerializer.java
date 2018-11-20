package com.fast.springboot.basic.serializer;

import com.fast.springboot.basic.util.LogExtUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP POST JSON解密器
 *
 * @author bowen.yan
 * @date 2018-11-16
 */
@Slf4j
public class DecryptInputSerializer extends JsonDeserializer<Object> {
    public DecryptInputSerializer() {
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return LogExtUtil.decryptAndLog("DecryptInputSerializer", jsonParser.getCurrentName(), jsonParser.getValueAsString());
    }
}
