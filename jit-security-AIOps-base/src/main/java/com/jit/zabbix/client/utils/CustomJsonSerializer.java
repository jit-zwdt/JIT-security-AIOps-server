package com.jit.zabbix.client.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Mamadou Lamine NIANG
 **/
public final class CustomJsonSerializer {

    private static final String TIME_OFFSET = "+8";

    private CustomJsonSerializer() {
    }

    public static class BooleanNumericSerializer extends JsonSerializer<Boolean> {

        @Override
        public void serialize(Boolean aBoolean, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(Boolean.TRUE.equals(aBoolean) ? 1 : 0);
        }
    }

    public static class BooleanNumericDeserializer extends JsonDeserializer<Boolean> {

        @Override
        public Boolean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return !"0".equals(jsonParser.getText());
        }
    }

    public static class DateStrDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            String date = jsonParser.getText();
            if (!"0".equals(date)) {
                LocalDateTime localDateTimeBack = LocalDateTime.ofEpochSecond(Long.valueOf(date), 0, ZoneOffset.of(TIME_OFFSET));
                return localDateTimeBack;
            } else {
                return null;
            }
        }
    }

    public static class TimestampStrDeserializer extends JsonDeserializer<Timestamp> {

        @Override
        public Timestamp deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            String date = jsonParser.getText();
            if (date != null && !"".equals(date) && !"0".equals(date)) {
                Timestamp ts = null;
                try {
                    ts = Timestamp.valueOf(date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ts;
            } else {
                return null;
            }
        }
    }

    public static class LocalDateTimeStrDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            String date = jsonParser.getText();
            if (date != null && !"".equals(date) && !"0".equals(date)) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDateTimeBack = LocalDateTime.parse(date, df);
                return localDateTimeBack;
            } else {
                return null;
            }
        }
    }
}
