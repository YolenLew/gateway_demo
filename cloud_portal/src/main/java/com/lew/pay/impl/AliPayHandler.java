package com.lew.pay.impl;

import com.lew.pay.PayHandler;
import com.lew.pay.annotation.PayTypeAnnotation;
import com.lew.pay.constant.PayType;
import com.lew.pay.model.PayRequest;
import com.lew.pay.model.PayResult;
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
