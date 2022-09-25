/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.common.entity;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Yolen
 * @date 2022/9/25
 */
@Data
public class ClusterData {
    private Set<String> memList = new HashSet<>();

    private Set<String> cpuList = new HashSet<>();
}
