/*
 * Copyright (c) 2024. This is my custom copyright information.2024-2024. All rights reserved.
 */

package com.lew.common.util;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.lew.common.entity.DemoData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Yolen
 * @date 2024/6/15
 */
@Slf4j
public class EasyExcelUtilTest {
    @Test
    public void testSimpleWrite() {
        String fileName = "SimpleWrite-" + Instant.now().getEpochSecond();
        try {
            File tempFile = Files.createTempFile(fileName, ExcelTypeEnum.XLSX.getValue()).toFile();
            log.info("tempFile {}", tempFile.getCanonicalPath());
            EasyExcelUtil.simpleWrite(tempFile, DemoData.class, data());
        } catch (IOException e) {
            fail();
            throw new RuntimeException(e);
        }
    }

    private List<DemoData> data() {
        return IntStream.rangeClosed(1, 10).mapToObj(i -> {
            DemoData data = new DemoData();
            data.setString(RandomUtil.randomString(10));
            data.setDate(new Date());
            data.setDoubleData(ThreadLocalRandom.current().nextDouble());
            return data;
        }).collect(Collectors.toList());
    }
}