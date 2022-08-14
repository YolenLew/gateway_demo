package com.lew.pay.model;

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
