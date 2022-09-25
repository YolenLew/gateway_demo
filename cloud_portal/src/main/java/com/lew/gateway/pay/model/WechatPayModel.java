/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.pay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

/**
 * @author Yolen
 * @date 2022/8/14
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class WechatPayModel {
    private String name;

    private String content;
}
