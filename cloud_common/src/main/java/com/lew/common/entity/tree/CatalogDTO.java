
/*
 * Copyright (c) 2024. This is my custom copyright information.2024-2024. All rights reserved.
 */

package com.lew.common.entity.tree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Yolen
 * @date 2024/7/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogDTO {
    private Long id;
    private Long parentId;
    private String catalogName;
    private Integer count;
    private List<CatalogDTO> children;
    private List<DetailDTO> details;
}
