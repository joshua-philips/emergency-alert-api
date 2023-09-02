package com.jphilips.springemergencyapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jphilips.springemergencyapi.dto.DefaultResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public DefaultResponse globalEceptions(Exception exception) {
        return new DefaultResponse(exception.getLocalizedMessage(), null);
    }
}
