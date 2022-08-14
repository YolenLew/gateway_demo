package com.lew.pay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lew.pay.constant.PayType;
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
