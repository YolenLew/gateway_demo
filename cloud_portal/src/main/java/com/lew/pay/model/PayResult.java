package com.lew.pay.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Yolen
 * @date 2022/8/14
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayResult implements Serializable {
    private static final long serialVersionUID = -8818646237703457624L;

    private Integer code;

    private String message;

    private Integer status;
}
