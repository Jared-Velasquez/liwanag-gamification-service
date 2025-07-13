package com.liwanag.gamification.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> illegalArgument(IllegalArgumentException iae) {
        HashMap<String, String> map = new HashMap<>();
        map.put("error", iae.getMessage());
        return new ResponseEntity<>(
                map,
                HttpStatus.BAD_REQUEST
        );
    }
}
