package com.lew.pay.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Yolen
 * @date 2022/8/14
 */
@Getter
@ToString
@AllArgsConstructor
public enum PayType {
    AliPay(10, "支付宝"),
    WechatPay(20, "微信支付"),
    BankCardPay(30, "银行卡");

    /**
     * 编码值
     */
    private Integer code;

    /**
     * 描述
     */
    private String name;
}
