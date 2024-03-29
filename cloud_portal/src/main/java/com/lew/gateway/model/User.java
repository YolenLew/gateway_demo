/*
 * Copyright (c) 2022. This is my custom copyright information.2022-2022. All rights reserved.
 */

package com.lew.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Yolen
 * @date 2022/3/8
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class User {

    private Long id;

    private String name;

    private String wxCard;

    private Byte type;

    private String title;

    private String workYear;

    private Integer projectNumber;

    private BigDecimal avgScore;

    private BigDecimal avgScoreByA;

    private BigDecimal dailySalary;

    private Byte managerRank;

    private Byte isDeleted;

    private String imageHeadUrl;

    private Date createdTime;

    private Date updatedTime;

    private Integer revision;

    private Long loginId;

    private static final long serialVersionUID = 1L;
}
