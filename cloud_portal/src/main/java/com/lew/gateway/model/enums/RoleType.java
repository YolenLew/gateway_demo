/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author Yolen
 * @date 2022/7/10
 */
public enum RoleType implements IEnum<Integer> {
    MONITOR(0),
    CHARACTER(1);

    private Integer value;

    RoleType(Integer value) {
        this.value = value;
    }

    /**
     * 枚举数据库存储值
     */
    @Override
    public Integer getValue() {
        return value;
    }
}
