/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.pay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lew.gateway.pay.constant.PayType;
import lombok.Data;

/**
 * @author Yolen
 * @date 2022/8/14
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayRequest {
    private String to;
    private String from;
    private Integer money;

    private PayType payType;
}
