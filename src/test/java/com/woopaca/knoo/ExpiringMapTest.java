package com.woopaca.knoo;

import net.jodah.expiringmap.ExpiringMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ExpiringMapTest {

    @Test
    @DisplayName("ExpiringMap")
    void expiringMapTest() throws Exception {
        Map<String, String> map = ExpiringMap.builder()
                .maxSize(50)
                .expiration(3, TimeUnit.SECONDS)
                .build();
        map.put("test", "test");

        String getBeforeExpired = map.get("test");
        System.out.println("getBeforeExpired = " + getBeforeExpired);

        Thread.sleep(4000);

        String test = map.get("test");
        System.out.println("test = " + test);
    }
}
