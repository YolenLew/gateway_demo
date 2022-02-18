package com.lew.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lew.entity.CommonResult;
import com.lew.entity.User;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Yolen
 * @date 2022/2/18
 */
public class PortalControllerTest {

    private RestTemplate restTemplate = new RestTemplate();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JavaType constructJavaType(ParameterizedType genericTypePtz) {
        Type rawType = genericTypePtz.getRawType();
        Type actualTypeArgument = genericTypePtz.getActualTypeArguments()[0];
        if (actualTypeArgument instanceof ParameterizedType) {
            JavaType javaType = constructJavaType((ParameterizedType) actualTypeArgument);
            return MAPPER.getTypeFactory().constructParametricType((Class<?>) rawType, javaType);
        }
        return MAPPER.getTypeFactory().constructParametricType((Class<?>) rawType, (Class<?>)actualTypeArgument);

    }

    @Test
    public void testRecursiveType() throws NoSuchFieldException, JsonProcessingException {
        Class<PortalController> clazz = PortalController.class;
        Field property = clazz.getDeclaredField("property");
        ParameterizedType genericTypePtz = (ParameterizedType)property.getGenericType();
        JavaType javaType = constructJavaType(genericTypePtz);
        String json = "{\"code\":200,\"message\":\"操作成功\",\"data\":[{\"name\":\"Tom\",\"gender\":null,\"age\":10},{\"name\":\"Tom\",\"gender\":null,\"age\":10}]}";
        Object readValue = MAPPER.readValue(json, javaType);
        System.out.println(readValue);
    }

    @Test
    public void testDoubleDeserializeImplicitParamType() throws NoSuchFieldException {
        String json = "{\"code\":200,\"message\":\"操作成功\",\"data\":[{\"name\":\"Tom\",\"gender\":null,\"age\":10},{\"name\":\"Tom\",\"gender\":null,\"age\":10}]}";
        // 获取类字节类型
        Class<PortalController> clazz = PortalController.class;
        Field property = clazz.getDeclaredField("property");
        Type genericType = property.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type firstTypeArgument = parameterizedType.getActualTypeArguments()[0];
            if (firstTypeArgument instanceof ParameterizedType) {
                ParameterizedType firstParamType = (ParameterizedType) firstTypeArgument;
                Type firstTypeInnerRawType = firstParamType.getRawType();
                Type firstTypeInnerArgs = firstParamType.getActualTypeArguments()[0];

            }
        }
    }

    @Test
    public void testSingleDeserializeImplicitParamType() throws NoSuchFieldException, JsonProcessingException {
        String json = "{\"code\":200,\"message\":\"操作成功\",\"data\":{\"name\":\"Tom\",\"gender\":null,\"age\":10}}";
        // 获取类字节类型
        Class<PortalController> clazz = PortalController.class;
        Field property = clazz.getDeclaredField("singleNested");
        Type genericType = property.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Class rawType = (Class) parameterizedType.getRawType();
            Class firstTypeArgument = (Class) parameterizedType.getActualTypeArguments()[0];
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(rawType, firstTypeArgument);
            Object readValue = MAPPER.readValue(json, javaType);
            System.out.println(readValue);
        }
    }

    @Test
    public void testDeserializeObviousParamType() throws JsonProcessingException {
        Object result = restTemplate.getForObject("http://localhost:8093/portal/health", Object.class);
        TypeReference<CommonResult<List<User>>> typeReference = new TypeReference<CommonResult<List<User>>>() {
        };
        String json = MAPPER.writeValueAsString(result);
        // {"code":200,"message":"操作成功","data":[{"name":"Tom","gender":null,"age":10},{"name":"Tom","gender":null,"age":10}]}
        System.out.println(json);
        result = restTemplate.getForObject("http://localhost:8093/portal/singleNested", Object.class);
        json = MAPPER.writeValueAsString(result);
        // {"code":200,"message":"操作成功","data":{"name":"Tom","gender":null,"age":10}}
        System.out.println(json);
    }

    @Test
    public void testParameterizedType() throws NoSuchMethodException, NoSuchFieldException {
        Class<PortalController> clazz = PortalController.class;
        Field property = clazz.getDeclaredField("property");
        Type genericType = property.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            System.out.println("rawType: " + parameterizedType.getRawType());
            Type firstTypeArgument = parameterizedType.getActualTypeArguments()[0];
            System.out.println("first actual type: " + firstTypeArgument);
            if (firstTypeArgument instanceof ParameterizedType) {
                ParameterizedType firstParamType = (ParameterizedType) firstTypeArgument;
                System.out.println("firstTypeInnerRawType: " + firstParamType.getRawType());
                System.out.println("firstTypeInnerArgs actual type: " + firstParamType.getActualTypeArguments()[0]);
            }
        }
    }

}