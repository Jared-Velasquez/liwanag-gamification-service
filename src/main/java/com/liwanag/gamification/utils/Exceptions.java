package com.liwanag.gamification.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class Exceptions {
    public static ResponseEntity<Map<String, String>> convertErrorToResponse(Exception e, HttpStatusCode code) {
        HashMap<String, String> map = new HashMap<>();
        map.put("error", e.getMessage());
        return new ResponseEntity<>(
                map,
                code
        );
    }
}
