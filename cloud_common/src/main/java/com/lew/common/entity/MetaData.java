/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yolen
 * @date 2022/9/24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaData {
    private String id;

    private String clusterName;

    private String memory;

    private String cpu;
}
