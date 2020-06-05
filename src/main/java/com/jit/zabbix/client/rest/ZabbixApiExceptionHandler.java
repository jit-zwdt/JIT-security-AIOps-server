package com.jit.zabbix.client.rest;

import com.jit.zabbix.client.exception.ZabbixApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Mamadou Lamine NIANG
 **/
@RestControllerAdvice
public class ZabbixApiExceptionHandler {
    Logger log = LoggerFactory.getLogger(ZabbixApiExceptionHandler.class);

    @ExceptionHandler(ZabbixApiException.class)
    public final ResponseEntity<String> handleApiException(ZabbixApiException e) {
        log.error("Exception using Zabbix Api", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
