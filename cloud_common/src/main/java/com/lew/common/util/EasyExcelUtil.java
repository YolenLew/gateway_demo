/*
 * Copyright (c) 2024. This is my custom copyright information.2024-2024. All rights reserved.
 */

package com.lew.common.util;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Yolen
 * @date 2024/6/13
 */
@Slf4j
public class EasyExcelUtil {
    private EasyExcelUtil() {
    }

    /**
     * 在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入
     *
     * @param file  file
     * @param clazz clazz
     * @param data  data
     * @param <T>   T
     */
    public static <T> void simpleWrite(File file, Class<T> clazz, List<T> data) {
        EasyExcelFactory.write(file, clazz).excelType(ExcelTypeEnum.XLSX).sheet().doWrite(data);
    }

    public <T> void repeatedWrite(File file, Class<T> clazz, Supplier<List<T>> supplier) {
        try (ExcelWriter excelWriter = EasyExcelFactory.write(file, clazz).build()) {
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcelFactory.writerSheet().build();
            // 去调用写入,这里示例调用五次，实际使用时根据数据库分页的总的页数来
            for (int i = 0; i < 5; i++) {
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<T> data = supplier.get();
                excelWriter.write(data, writeSheet);
            }
        }
    }
}