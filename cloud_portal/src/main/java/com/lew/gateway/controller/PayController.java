/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.controller;

import com.lew.common.entity.CommonResult;
import com.lew.gateway.pay.model.PayRequest;
import com.lew.gateway.pay.model.PayResult;
import com.lew.gateway.pay.PayHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author Yolen
 * @date 2022/8/14
 */
@Slf4j
@RestController
@RequestMapping(value = "/pay")
public class PayController {
    @PostMapping(value = "/route")
    public CommonResult<PayResult> route(@RequestBody PayRequest payRequest) {
        Objects.requireNonNull(payRequest, "request param can not be null");
        log.info("payRequest: {}", payRequest);
        PayResult payResult = PayHandlerContext.getPayHandler(payRequest.getPayType()).pay(payRequest);
        return CommonResult.success(payResult);
    }
}
