package com.lew.common.util;

import cn.hutool.core.lang.Assert;

import java.time.*;

/**
 * @author Yolen
 * @date 2022/7/3
 */
public class MyDateUtils {
    private MyDateUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static LocalDateTime timestamToDatetime(long timestam) {
        Instant instant = Instant.ofEpochMilli(timestam);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static long datatimeToTimestamp(LocalDateTime localDateTime) {
        Assert.notNull(localDateTime, "localDateTime must not be null");
        return localDateTime.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
    }
}
