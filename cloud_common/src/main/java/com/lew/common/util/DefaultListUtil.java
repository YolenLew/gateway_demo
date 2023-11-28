
/*
 * Copyright (c) 2023. This is my custom copyright information.2023-2023. All rights reserved.
 */

package com.lew.common.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 自封装的集合工具类
 *
 * @author Yolen
 * @date 2023/11/28
 */
public class DefaultListUtil {
    private DefaultListUtil() {
    }

    /**
     * 按某字段去重<p>
     * 方法一：通过stream流<p>
     * 通过Collectors.collectingAndThen的Collectors.toCollection，里面用TreeSet在构造函数中指定字段<p>
     * 优点：代码简洁易懂<p>
     * 缺点：元素顺序发生变化
     *
     * @param list         list
     * @param keyExtractor 要比较的字段提取器
     * @param <T>          要去重的元素类型
     * @param <U>          要比较的字段类型
     * @return 去重后的字段集合
     */
    public static <T, U extends Comparable<? super U>> List<T> distinctByCompare(List<T> list,
        Function<? super T, ? extends U> keyExtractor) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        Objects.requireNonNull(keyExtractor);
        return list.stream().filter(Objects::nonNull).collect(Collectors.collectingAndThen(
            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(keyExtractor))), ArrayList::new));
    }

    /**
     * 临时定义一个HashMap，配合filter方法可灵活的按字段去重，保持了原列表的顺序.<p>
     * 不足之处是内部定义了一个HashMap，有一定内存占用，并且多了一个方法定义
     *
     * @param list         list
     * @param keyExtractor 要比较的字段提取器
     * @param <T>          要去重的元素类型
     * @param <U>          要比较的字段类型
     * @return 去重后的字段集合
     */
    public static <T, U> List<T> distinctByMap(List<T> list, Function<? super T, ? extends U> keyExtractor) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        Objects.requireNonNull(keyExtractor);
        Map<U, Boolean> map = new ConcurrentHashMap<>();
        return list.stream().filter(Objects::nonNull)
            .filter(t -> Objects.isNull(map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE)))
            .collect(Collectors.toList());
    }

    /**
     * 封装定义一个去重方法，配合filter方法可灵活的按字段去重，保持了原列表的顺序.<p>
     * 不足之处是内部定义了一个HashMap，有一定内存占用，并且多了一个方法定义
     *
     * @param list         list
     * @param keyExtractor 要比较的字段提取器
     * @param <T>          要去重的元素类型
     * @param <U>          要比较的字段类型
     * @return 去重后的字段集合
     */
    public static <T, U> List<T> distinctByField(List<T> list, Function<? super T, ? extends U> keyExtractor) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        Objects.requireNonNull(keyExtractor);
        return list.stream().filter(Objects::nonNull).filter(predicateByKey(keyExtractor)).collect(Collectors.toList());
    }

    /**
     * 判断返回值是否等于null来判断是否过滤，如果等于null，说明该对象的不重复,返回true
     * <p>
     * putIfAbsent，该方法作用是:
     * 1、如果key不存在或者key已存在但是值为null，就put进去。
     * 2、put进去时返回null，不put进去时返回原值。
     *
     * @param keyExtractor 要比较的字段提取器
     * @param <T>          要去重的元素类型
     * @param <U>          要比较的字段类型
     * @return 对象是否重复的断言函数
     */
    public static <T, U> Predicate<T> predicateByKey(Function<? super T, U> keyExtractor) {
        Map<U, Boolean> map = new ConcurrentHashMap<>();
        return t -> Objects.isNull(map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE));
    }

}
