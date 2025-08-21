//package com.liwanag.gamification.utils;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.auditing.DateTimeProvider;
//
//import java.time.OffsetDateTime;
//
//public class Auditors {
//    @Bean
//    public DateTimeProvider auditingDateTimeProvider() {
//        return () -> java.util.Optional.of(OffsetDateTime.now(java.time.ZoneOffset.UTC));
//    }
//}
