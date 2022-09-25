/*
 * Copyright (c) 2022. This is my custom copyright information.$today.year-2022. All rights reserved.
 */

package com.lew.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.util.List;

import static com.lew.common.util.CommonUtils.DEFAULT_END_SIZE_EXCLUSIVE;
import static com.lew.common.util.CommonUtils.DEFAULT_MAX_STR_LENGTH_EXCLUSIVE;
import static com.lew.common.util.CommonUtils.DEFAULT_MIN_STR_LENGTH_INCLUSIVE;
import static com.lew.common.util.CommonUtils.DEFAULT_START_SIZE_INCLUSIVE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Yolen
 * @date 2022/9/25
 */
@Slf4j
public class CommonUtilsTest {
    @Test
    public void testGenerateStrList() {
        for (int i = 0; i < 100; i++) {
            List<String> strList = CommonUtils.generateStrList();
            int size = strList.size();
            log.info("strList: {}", strList);
            log.info("strList size: {}", size);
            assertTrue(size >= DEFAULT_START_SIZE_INCLUSIVE && size < DEFAULT_END_SIZE_EXCLUSIVE);
            assertTrue(strList.stream().allMatch(str -> str.length() >= DEFAULT_MIN_STR_LENGTH_INCLUSIVE
                && str.length() < DEFAULT_MAX_STR_LENGTH_EXCLUSIVE));
        }
    }

    @Test
    public void generateFixSizeList() {
        for (int i = 0; i < 100; i++) {
            int size = RandomUtils.nextInt(10, 50);
            List<String> strList = CommonUtils.generateStrList(size);
            int actualSize = strList.size();
            log.info("strList: {}", strList);
            log.info("strList actualSize: {}", actualSize);
            assertEquals(size, actualSize);
            assertTrue(strList.stream().allMatch(str -> str.length() >= DEFAULT_MIN_STR_LENGTH_INCLUSIVE
                    && str.length() < DEFAULT_MAX_STR_LENGTH_EXCLUSIVE));
        }
    }

    @Test
    public void testRandomUtils() {
        for (int i = 0; i < 100; i++) {
            int startInclusive = 1;
            int endExclusive = 2;
            int number = RandomUtils.nextInt(startInclusive, endExclusive);
            assertEquals(startInclusive, number);
        }
    }
}