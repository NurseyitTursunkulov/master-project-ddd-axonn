package com.masterproject.ddd.common.exceptions;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ServerExceptionHandler {

    @Value
    public static class ServerError {
        private final String message;
        private final String cause;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ServerError handleError(Exception ex) {
        log.error("[Server Error] Error: " + ex.getCause());
        return new ServerError(ex.getMessage(), ExceptionUtils.getRootCauseMessage(ex));
    }

}
