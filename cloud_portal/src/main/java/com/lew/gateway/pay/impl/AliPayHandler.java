/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.pay.impl;

import com.lew.gateway.pay.PayHandler;
import com.lew.gateway.pay.constant.PayType;
import com.lew.gateway.pay.model.PayRequest;
import com.lew.gateway.pay.model.PayResult;
import com.lew.gateway.pay.annotation.PayTypeAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Yolen
 * @date 2022/8/14
 */
@Slf4j
@Component
@PayTypeAnnotation(payType = PayType.AliPay)
public class AliPayHandler implements PayHandler {
    @Override
    public PayResult pay(PayRequest payRequest) {
        return PayResult.builder().message(PayType.AliPay.name()).code(0).status(0).build();
    }
}
