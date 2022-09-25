/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.pay;

import com.lew.gateway.pay.model.PayRequest;
import com.lew.gateway.pay.model.PayResult;

/**
 * @author Yolen
 * @date 2022/8/14
 */
public interface PayHandler {
    PayResult pay(PayRequest payRequest);
}
