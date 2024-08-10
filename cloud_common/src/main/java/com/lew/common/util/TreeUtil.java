/*
 * Copyright (c) 2024. This is my custom copyright information.2024-2024. All rights reserved.
 */

package com.lew.common.util;

import com.lew.common.entity.tree.CatalogDTO;
import com.lew.common.entity.tree.DetailDTO;
import com.lew.common.entity.tree.ITreeNode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Yolen
 * @date 2024/7/28
 */
public class TreeUtil {
    /**
     * 树形菜单
     *
     * @param list list
     * @param <T>  T
     * @return tree
     */
    public static <T extends ITreeNode<T>> List<T> listToTree2(List<T> list) {
        // 第一次遍历: 记录节点间的父子关系
        Map<T, List<T>> relations = new HashMap<>();
        for (T node : list) {
            List<T> relation = relations.computeIfAbsent(node.parent(), p -> new LinkedList<>());
            relation.add(node);
        }
        // 父节点为null的即为根节点
        List<T> roots = relations.get(null);
        // 第二次遍历: 根据父子关系建立树
        Stack<T> stack = roots.stream().collect(Collectors.toCollection(Stack::new));
        while (!stack.isEmpty()) {
            T node = stack.pop();
            node.setChildren(relations.getOrDefault(node, Collections.emptyList()));
            stack.addAll(node.getChildren());
        }
        return roots;
    }

    /**
     * 构建树以及叶子树包含的详情信息
     *
     * @param flatList      flatList
     * @param detailDTOList detailDTOList
     * @return 节点树
     */
    public static List<CatalogDTO> flatListToForest(List<CatalogDTO> flatList, List<DetailDTO> detailDTOList) {
        Map<Long, List<DetailDTO>> detailByCataIdMap = detailDTOList.stream().filter(Objects::nonNull).map(e -> {
            if (Objects.isNull(e.getParentCatalogId())) {
                e.setParentCatalogId(-1L);
            }
            return e;
        }).collect(Collectors.groupingBy(DetailDTO::getParentCatalogId));

        Map<Long, List<CatalogDTO>> catalogByParentId =
            flatList.stream().collect(Collectors.groupingBy(CatalogDTO::getParentId));

        for (CatalogDTO catalog : flatList) {
            // 构建子树
            List<CatalogDTO> children = catalogByParentId.get(catalog.getId());
            catalog.setChildren(children);
            // 构建包含的详情树
            catalog.setDetails(detailByCataIdMap.get(catalog.getId()));
            catalog.setCount(CollectionUtils.size(catalog.getDetails()));
        }

        countCatalogDetail(detailByCataIdMap, catalogByParentId, flatList);

        // 假设根节点的 parentId 是 0
        return catalogByParentId.get(0L);
    }

    private static void countCatalogDetail(Map<Long, List<DetailDTO>> detailByCataIdMap,
        Map<Long, List<CatalogDTO>> catalogByParentId, List<CatalogDTO> flatList) {
        Map<Long, CatalogDTO> catalogById = flatList.stream()
            .collect(Collectors.toMap(CatalogDTO::getId, Function.identity(), (v1, v2) -> v2, HashMap::new));

        // 使用队列进行自底向上的广度优先搜索：统计detail数量
        // 将叶子节点加入队列
        Queue<CatalogDTO> queue = detailByCataIdMap.keySet().stream().map(catalogById::get).filter(Objects::nonNull)
            .collect(Collectors.toCollection(LinkedList::new));

        Set<Long> processedNodes = new HashSet<>();

        while (!queue.isEmpty()) {
            CatalogDTO current = queue.poll();

            // 跳过已处理过的节点
            if (processedNodes.contains(current.getId())) {
                continue;
            }
            processedNodes.add(current.getId());

            // 当前节点自身的DetailDTO数量
            int totalCount = current.getCount();
            List<CatalogDTO> children = catalogByParentId.getOrDefault(current.getId(), Collections.emptyList());
            for (CatalogDTO child : children) {
                // 累加子节点的DetailDTO数量
                totalCount += child.getCount();
            }

            // 更新当前节点的count值
            current.setCount(totalCount);

            // 自底向上：将父节点加入队列
            Long parentId = current.getParentId();
            if (!processedNodes.contains(parentId) && catalogById.containsKey(parentId)) {
                queue.offer(catalogById.get(parentId));
            }
        }
    }

    private TreeUtil() {
    }
}