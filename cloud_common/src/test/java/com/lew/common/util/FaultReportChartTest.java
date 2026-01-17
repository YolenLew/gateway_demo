
/*
 * Copyright (c) 2026. This is my custom copyright information.2026-2026. All rights reserved.
 */

package com.lew.common.util;

import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertTrue;

/**
 * @author Yolen
 * @date 2026-01-17
 */
public class FaultReportChartTest {
    public static String imagePath = "E://data//jfree";

    @Test
    public void testPie() throws Exception {
        //图例名称列表
        List<String> legendNameList =
            new ArrayList<>(Arrays.asList("大数据", "原生云", "数据库", "运维池", "业务裸机", "金融云", "终端"));
        //数据列表
        List<Object> dataList = new ArrayList<>(Arrays.asList(15, 12, 6, 4, 4, 1, 1));
        JFreeChart chart = GeneratePieChartUtil.createPieChart("各级占比情况", legendNameList, dataList,
            JFreeChartUtil.createChartTheme("宋体"), Collections.emptyList(), Collections.emptyList());
        //在D盘目录下生成图片
        File p = new File(imagePath);
        if (!p.exists()) {
            p.mkdirs();
        }
        String imageName = System.currentTimeMillis() + "_饼图" + ".jpeg";
        File file = new File(p.getPath() + "/" + imageName);
        try {
            if (file.exists()) {
                file.delete();
            }
            ChartUtils.saveChartAsJPEG(file, chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(file.exists());
    }

    @Test
    public void testLineChart() throws Exception {
        int day = 30;
        // x轴名称列表
        LocalDate beginDate = LocalDate.now().minusDays(30);
        // 根据localdate获取近30天的日期，时间格式MM-dd，时间靠前的排前面
        List<String> xAxisNameList =
            IntStream.range(0, day).mapToObj(i -> beginDate.plusDays(i).toString().substring(5))
                .collect(Collectors.toList());
        // 图例名称列表
        List<String> legendNameList = new ArrayList<>(Arrays.asList("当日新增事件", "当日新增宕机事件"));
        // 数据列表
        List<List<Object>> dataList = new ArrayList<>();
        List<Object> newEventList =
            IntStream.range(0, day).mapToObj(i -> (int)(Math.random() * 50)).collect(Collectors.toList());
        List<Object> newFaultList =
            IntStream.range(0, day).mapToObj(i -> (int)(Math.random() * 10)).collect(Collectors.toList());
        dataList.add(newEventList);
        dataList.add(newFaultList);

        JFreeChart chart =
            GenerateChartUtil.createLineChart("最近 30 天事件趋势", legendNameList, xAxisNameList, dataList,
                JFreeChartUtil.createChartTheme("宋体"), null, null);
        //在D盘目录下生成图片
        File p = new File(imagePath);
        if (!p.exists()) {
            p.mkdirs();
        }
        String imageName = System.currentTimeMillis() + "_折线图" + ".jpeg";
        File file = new File(p.getPath() + "/" + imageName);
        try {
            if (file.exists()) {
                file.delete();
            }
            ChartUtils.saveChartAsJPEG(file, chart, 1200, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(file.exists());
    }
}
