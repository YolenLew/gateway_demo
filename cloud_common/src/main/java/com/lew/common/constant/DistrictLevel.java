/*
 * Copyright (c) 2023. This is my custom copyright information.2023-2023. All rights reserved.
 */

package com.lew.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Yolen
 * @date 2023/12/10
 */
@Getter
@ToString
@AllArgsConstructor
public enum DistrictLevel {
    DEFAULT(0, "默认"),
    PERSONAL(1, "个人"),
    COUNTY(2, "县区"),
    CITY(3, "城市"),
    PROVINCE(4, "省份"),
    NATIONAL(5, "全国"),
    ;

    private final Integer code;
    private final String name;
}
