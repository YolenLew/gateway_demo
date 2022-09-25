/*
 * Copyright (c) 2022. This is my custom copyright information.$today.year-2022. All rights reserved.
 */

package com.lew.common.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Yolen
 * @date 2022/9/25
 */
public class CommonUtils {
    public static final int DEFAULT_MIN_STR_LENGTH_INCLUSIVE = 5;
    public static final int DEFAULT_MAX_STR_LENGTH_EXCLUSIVE = 30;
    public static final int DEFAULT_START_SIZE_INCLUSIVE = 100;
    public static final int DEFAULT_END_SIZE_EXCLUSIVE = 150;

    private CommonUtils() {}

    public static List<String> generateStrList() {
        return generateStrList(DEFAULT_MIN_STR_LENGTH_INCLUSIVE, DEFAULT_MAX_STR_LENGTH_EXCLUSIVE,
            DEFAULT_START_SIZE_INCLUSIVE, DEFAULT_END_SIZE_EXCLUSIVE);
    }

    public static List<String> generateStrList(int size) {
        return generateStrList(DEFAULT_MIN_STR_LENGTH_INCLUSIVE, DEFAULT_MAX_STR_LENGTH_EXCLUSIVE, size, size + 1);
    }

    public static List<String> generateStrList(int minStrLengthInclusive, int maxStrLengthExclusive,
        int startSizeInclusive, int endSizeExclusive) {
        return Stream.generate(() -> RandomStringUtils.randomAlphanumeric(minStrLengthInclusive, maxStrLengthExclusive))
            .limit(RandomUtils.nextInt(startSizeInclusive, endSizeExclusive)).collect(Collectors.toList());
    }
}
