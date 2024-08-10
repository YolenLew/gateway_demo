/*
 * Copyright (c) 2024. This is my custom copyright information.2024-2024. All rights reserved.
 */

package com.lew.common.util;

import com.lew.common.entity.tree.CatalogDTO;
import com.lew.common.entity.tree.DetailDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Yolen
 * @date 2024/7/28
 */
class TreeUtilTest {
    @Test
    void testFlatListToForest() {
        List<CatalogDTO> catalogs = Arrays.asList(
            new CatalogDTO(1L, 0L, "广东省", 0, null, null),
            new CatalogDTO(2L, 0L, "江苏省", 0, null, null),
            new CatalogDTO(3L, 0L, "福建省", 0, null, null),
            new CatalogDTO(11L, 1L, "广州市", 0, null, null),
            new CatalogDTO(12L, 1L, "中山市", 0, null, null),
            new CatalogDTO(13L, 1L, "东莞市", 0, null, null),
            new CatalogDTO(110L, 11L, "天河区", 0, null, null),
            new CatalogDTO(111L, 11L, "白云区", 0, null, null),
            new CatalogDTO(112L, 11L, "海珠区", 0, null, null),
            new CatalogDTO(120L, 12L, "石岐", 0, null, null),
            new CatalogDTO(1200L, 120L, "桂园", 0, null, null));

        List<DetailDTO> details = Arrays.asList(
            new DetailDTO(1L, 110L, "广东-广州-天河-五山"),
            new DetailDTO(5L, 110L, "广东-广州-天河-石牌"),
            new DetailDTO(2L, 111L, "广东-广州-白云-三元里"),
            new DetailDTO(3L, 1200L, "广东-中山-石岐-桂园"),
            new DetailDTO(4L, 2L, "江苏-二级详情")
        );

        List<CatalogDTO> catalogTreeList = TreeUtil.flatListToForest(catalogs, details);
        Assertions.assertTrue(CollectionUtils.isNotEmpty(catalogTreeList));
    }
}