/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author Yolen
 * @date 2022/5/21
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleRange {
    private String role;
    private List<String> skills;
}
