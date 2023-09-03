/*
 * Copyright (c) 2023. This is my custom copyright information.2023-2023. All rights reserved.
 */

package com.lew.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author Yolen
 * @date 2023/7/25
 */
@Slf4j
public class JacksonUtil {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    private JacksonUtil() {
    }

    static {
        // 设置输入:禁止把POJO中值为null的字段映射到json字符串中(注意：只对VO起作用；对Map List不起作用)
        MAPPER.setSerializationInclusion(Include.NON_NULL)
            // 忽略未知字段: 反序列化时，属性不存在的兼容处理
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // 忽略空Bean转json的错误
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    // -------------------------------------------------------------------------------------
    // ----------------------             【实体类相关】                  ----------------------
    // -------------------------------------------------------------------------------------

    /**
     * 对象转Json格式字符串 参考：<a href="https://www.cnblogs.com/christopherchan/p/11071098.html">利用Jackson封装常用JsonUtil工具类</a>
     *
     * @param obj 对象
     * @return Json格式字符串
     */
    public static <T> String obj2Str(T obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        try {
            return obj instanceof String ? (String)obj : MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("failed to parse Object to String : {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 字符串转换为自定义对象
     *
     * @param json  要转换的字符串
     * @param clazz 自定义对象的class对象
     * @return 自定义对象
     */
    public static <T> T str2Obj(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json) || Objects.isNull(clazz)) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T)json : MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("Parse String to Object error : {}", e.getMessage(), e);
            return null;
        }
    }

    public static <T> T str2Obj(String json, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(json) || Objects.isNull(typeReference)) {
            return null;
        }

        try {
            return (T)(typeReference.getType().equals(String.class) ? json : MAPPER.readValue(json, typeReference));
        } catch (Exception e) {
            log.error("Parse String to Object error : {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 反序列化复杂嵌套泛型的实体类(比如CommonResult<List<User>>这种复杂的嵌套实体类)
     *
     * @param json             json
     * @param parameterClasses 内层嵌套泛型参数
     * @param <T>              返回值泛型
     * @return 嵌套泛型的实体类
     */
    public static <T> T str2GenericObj(String json, Class<?>... parameterClasses) {
        try {
            JavaType javaType = constructParameterizedType(null, parameterClasses);
            return MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static JavaType constructParameterizedType(JavaType javaType, Class<?>... parameterClasses) {
        if (Objects.isNull(javaType) && parameterClasses.length == 1) {
            return MAPPER.getTypeFactory().constructType(parameterClasses[parameterClasses.length - 1]);
        }
        // 首次初始化
        if (Objects.isNull(javaType)) {
            JavaType type = MAPPER.getTypeFactory().constructType(parameterClasses[parameterClasses.length - 1]);
            Class<?>[] outerClasses = new Class<?>[parameterClasses.length - 1];
            System.arraycopy(parameterClasses, 0, outerClasses, 0, outerClasses.length);
            return constructParameterizedType(type, outerClasses);
        }

        // 递归终止
        if (parameterClasses.length == 1) {
            return MAPPER.getTypeFactory()
                .constructParametricType(parameterClasses[parameterClasses.length - 1], javaType);
        }

        // 循环构造
        JavaType newType =
            MAPPER.getTypeFactory().constructParametricType(parameterClasses[parameterClasses.length - 1], javaType);
        Class<?>[] outerClasses = new Class<?>[parameterClasses.length - 1];
        System.arraycopy(parameterClasses, 0, outerClasses, 0, outerClasses.length);
        return constructParameterizedType(newType, outerClasses);
    }

    // -------------------------------------------------------------------------------------
    // ----------------------             【集合相关】                  ----------------------
    // -------------------------------------------------------------------------------------

    /**
     * 将json数组转换为指定泛型的List
     *
     * @param json           json数组
     * @param elementClazzes 集合泛型
     * @param <T>            普通实体类（不包括嵌套泛型的类型，比如CommonResult<T>这种）
     * @return List集合
     */
    public static <T> List<T> str2List(String json, Class<T> elementClazzes) {
        if (StringUtils.isBlank(json) || Objects.isNull(elementClazzes)) {
            return Collections.emptyList();
        }

        try {
            JavaType javaType = constructParametricType(ArrayList.class, elementClazzes);
            return MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            log.error("Parse String to List error : {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    /**
     * 将json数组转换为指定泛型的Colllection集合(List、Set等)
     *
     * @param json            json数组
     * @param collectionClazz 集合容器
     * @param elementClazzes  集合泛型
     * @param <T>             集合泛型
     * @return Colllection集合
     */
    public static <T> T str2Collection(String json, Class<? extends Collection> collectionClazz,
        Class<?>... elementClazzes) {
        if (StringUtils.isBlank(json)) {
            return null;
        }

        try {
            JavaType javaType = constructParametricType(collectionClazz, elementClazzes);
            return MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            log.error("Parse String to Colllection error : {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取泛型的Collection Type(另请参考：TypeReference type = new TypeReference<List<T>>() {})
     *
     * @param collectionClazz 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     */
    public static JavaType constructParametricType(Class<?> collectionClazz, Class<?>... elementClasses) {
        return MAPPER.getTypeFactory().constructParametricType(collectionClazz, elementClasses);
    }

    /**
     * 构造 TypeReference 实例对象，不通用所以不推荐
     *
     * @param <T> 集合泛型
     * @return TypeReference
     */
    public static <T> TypeReference<List<T>> getListTypeRef() {
        return new TypeReference<List<T>>() {
        };
    }

    // -------------------------------------------------------------------------------------
    // ----------------------             【Map相关】                  ----------------------
    // -------------------------------------------------------------------------------------

    /**
     * json转map
     *
     * @param map   map
     * @param clazz 实体bean
     * @param <T>   泛型
     * @return 实体bean
     */
    public static <T> T map2Bean(Map<String, Object> map, Class<T> clazz) {
        return MAPPER.convertValue(map, clazz);
    }

    public static <K, V> Map<K, V> str2HashMap(String json, Class<K> keyClazz, Class<V> valueClazz) {
        return str2Map(json, HashMap.class, keyClazz, valueClazz);
    }

    /**
     * 将json转换为指定泛型的Map集合(HashMap、TreeMap等)
     *
     * @param json       json
     * @param mapClazz   Map类型
     * @param keyClazz   键类型
     * @param valueClazz 值类型
     * @param <K>        键类型
     * @param <V>        值类型
     * @return Map集合
     */
    public static <K, V> Map<K, V> str2Map(String json, Class<? extends Map> mapClazz, Class<K> keyClazz,
        Class<V> valueClazz) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyMap();
        }

        try {
            JavaType javaType = getMapType(mapClazz, keyClazz, valueClazz);
            return MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            log.error("Parse String to Map error : {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    /**
     * 获取泛型的Map Type（另外：TypeReference<Map<K, V>> typeRef = new TypeReference<Map<K, V>>() {};）
     *
     * @param mapClazz   map容器
     * @param keyClazz   键类型
     * @param valueClazz 值类型
     * @return map集合
     */
    public static JavaType getMapType(Class<? extends Map> mapClazz, Class<?> keyClazz, Class<?> valueClazz) {
        return MAPPER.getTypeFactory().constructMapType(mapClazz, keyClazz, valueClazz);
    }

    // -------------------------------------------------------------------------------------
    // ----------------------             【递归构造泛型】                  ----------------------
    // -------------------------------------------------------------------------------------

    /**
     * 根据ParameterizedType构造JavaType(比如根据已知属性CommonResult<List<User>> property进行构造)
     *
     * @param genericTypePtz ParameterizedType
     * @return JavaType
     */
    public static JavaType constructJavaType(ParameterizedType genericTypePtz) {
        Type rawType = genericTypePtz.getRawType();
        Type actualTypeArgument = genericTypePtz.getActualTypeArguments()[0];
        if (actualTypeArgument instanceof ParameterizedType) {
            JavaType javaType = constructJavaType((ParameterizedType)actualTypeArgument);
            return MAPPER.getTypeFactory().constructParametricType((Class<?>)rawType, javaType);
        }
        return MAPPER.getTypeFactory().constructParametricType((Class<?>)rawType, (Class<?>)actualTypeArgument);

    }

    // -------------------------------------------------------------------------------------
    // ----------------------             【代码封装整合】                  ----------------------
    // -------------------------------------------------------------------------------------

    /**
     * 封装包含异常的转换方法，避免冗余代码
     *
     * @param parser 转换方法
     * @param check  异常
     * @param <T>    结果类型
     * @return 转换结果
     */
    protected final <T> T tryParse(Callable<T> parser, Class<? extends Exception> check) {
        try {
            return parser.call();
        } catch (Exception ex) {
            log.error("Failed to try parse: error-[{}]", ex.getMessage(), ex);
            if (check.isAssignableFrom(ex.getClass())) {
                throw new RuntimeException("Cannot parse JSON", ex);
            }
            return null;
        }
    }
}
