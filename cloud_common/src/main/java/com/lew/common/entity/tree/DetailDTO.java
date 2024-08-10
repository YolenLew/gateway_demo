
/*
 * Copyright (c) 2024. This is my custom copyright information.2024-2024. All rights reserved.
 */

package com.lew.common.entity.tree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yolen
 * @date 2024/7/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailDTO {
    private Long id;
    private Long parentCatalogId;
    private String detailName;
}
