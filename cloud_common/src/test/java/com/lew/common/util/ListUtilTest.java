/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.common.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author Yolen
 * @date 2022/9/18
 */
@Slf4j
public class ListUtilTest {
    @Test
    public void partitionList() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<List<Integer>> partitionList = Lists.partition(list, 4);
        // partitionList: [[1, 2, 3, 4], [5, 6, 7, 8], [9, 10]]
        log.info("partitionList: {}", partitionList);
        assertTrue(CollectionUtils.isNotEmpty(partitionList));
    }
}
