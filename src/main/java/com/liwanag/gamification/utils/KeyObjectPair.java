package com.liwanag.gamification.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class KeyObjectPair<T> {
    private String key;
    private T obj;
}
