package com.lew.pay;

import com.lew.pay.model.PayRequest;
import com.lew.pay.model.PayResult;

/**
 * @author Yolen
 * @date 2022/8/14
 */
public interface PayHandler {
    PayResult pay(PayRequest payRequest);
}
