package com.jit.zabbix.client.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.jit.zabbix.client.exception.ZabbixApiException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mamadou Lamine NIANG
 **/
@Component
public class JsonMapper {

    private final ObjectMapper objectMapper;

    public JsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T getObject(JsonNode node, Class<T> tClass) throws ZabbixApiException {
        try {
            return objectMapper.treeToValue(node, tClass);
        } catch (JsonProcessingException e) {
            throw new ZabbixApiException("Error converting value to Object", e);
        }
    }

    public <T> List<T> getList(JsonNode node, Class<T> tClass) throws ZabbixApiException {
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, tClass);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        JsonParser jsonParser = node.traverse();
        try {
            MappingIterator<List<T>> mappingIterator = objectMapper.readValues(jsonParser, type);
            if(mappingIterator.hasNext()) {
                return mappingIterator.next();
            } else {
                return new ArrayList<>();
            }

        } catch (IOException e) {
            throw new ZabbixApiException("Error converting value to List", e);
        }
    }

    public <T> List<T> getList(JsonNode node, String fieldName, Class<T> tClass) throws ZabbixApiException {
        JsonNode innerNode = node.findValue(fieldName);
        if(innerNode == null) {
            throw new ZabbixApiException(String.format("Node '%s' not found", fieldName));
        }
        return getList(innerNode, tClass);
    }
}
