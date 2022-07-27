package com.lew.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 正则表达式测试类
 *
 * @author Yolen
 * @date 2022/7/27
 */
@Slf4j
public class PatternTest {
    @Test
    public void patternGroup() {
        String str = "yam.scut_sports_soccer_volleyball_basketball";
        int dotIndex = str.indexOf(".");
        int firstUnderlineIndex = str.indexOf("_");
        int secondUnderlineIndex = str.indexOf("_", firstUnderlineIndex + 1);
        String str1 = str.substring(0, dotIndex);
        String str2 = str.substring(dotIndex + 1, firstUnderlineIndex);
        String str3 = str.substring(firstUnderlineIndex + 1, secondUnderlineIndex);
        String str4 = str.substring(secondUnderlineIndex + 1);
        Pattern pattern = Pattern.compile("^([A-Za-z]+)\\.([A-Za-z]+)_([A-Za-z]+)_([A-Za-z_]+)$");
        Matcher matcher = pattern.matcher(str);
        assertTrue(matcher.find());
        assertEquals(4, matcher.groupCount());
        assertEquals(str, matcher.group(0));
        assertEquals(str1, matcher.group(1));
        assertEquals(str2, matcher.group(2));
        assertEquals(str3, matcher.group(3));
        assertEquals(str4, matcher.group(4));
        log.info("str: {}", str);
        log.info("str1: {}", str1);
        log.info("str2: {}", str2);
        log.info("str3: {}", str3);
        log.info("str4: {}", str4);
    }

    @Test
    public void commaStrPattern() {
        Pattern commaAlphanumericPattern = Pattern.compile("^[A-Za-z0-9.]{1,32}(,[A-Za-z0-9.]{1,32})*$");
        String str1 = "com.lew.common";
        String str2 = "com.lew.common,org.springframework.boot";
        String str3 = "com.lew.common,233";
        String str4 = "com.lew.common,233,yaml";

        String str5 = ",com.lew.common";
        String str6 = "com.lew.common,";
        String str7 = "com.lew.common,org.springframework.boot,";

        assertTrue(commaAlphanumericPattern.matcher(str1).find());
        assertTrue(commaAlphanumericPattern.matcher(str2).find());
        assertTrue(commaAlphanumericPattern.matcher(str3).find());
        assertTrue(commaAlphanumericPattern.matcher(str4).find());

        assertFalse(commaAlphanumericPattern.matcher(str5).find());
        assertFalse(commaAlphanumericPattern.matcher(str6).find());
        assertFalse(commaAlphanumericPattern.matcher(str7).find());
    }
}
