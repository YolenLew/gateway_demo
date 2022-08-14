package com.lew.controller;

import cn.hutool.core.lang.Singleton;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lew.common.entity.CommonResult;
import com.lew.common.entity.User;
import com.lew.pay.constant.PayType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.OK;

/**
 * @author Yolen
 * @date 2022/2/18
 */
@Slf4j
public class PortalControllerTest {

    private RestTemplate restTemplate = new RestTemplate();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String portalURL = "http://127.0.0.1:8093/portal";

    private static User user;

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        user = new User();
        user.setName("Tom");
        user.setAge(18);
        user.setGender("female");
    }

    @Test
    public void stringFormat() {
        PayType payType = null;
        String format = String.format(Locale.ROOT, "pay type [%s] is not supported", payType);
        log.info("format result of null: {}", format);
        format = String.format(Locale.ROOT, "pay type [%s] is not supported", PayType.AliPay);
        log.info("format result of nonnull: {}", format);
        Assert.assertTrue(format.contains(PayType.AliPay.name()));
    }

    @Test
    public void testXmlResp() {
        String url = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getRegionCountry";
        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(url, Object.class);
        System.out.println(responseEntity);
        ResponseEntity<Resource> resourceResponseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null), Resource.class);
        log.info("resourceResponseEntity: {}", resourceResponseEntity);
        Assert.assertEquals(OK, resourceResponseEntity.getStatusCode());

        url = "http://httpbin.org/html";
        System.out.println("----------------------------------");
        ResponseEntity<Resource> htmlResponseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null), Resource.class);
        log.info("resourceResponseEntity: {}", htmlResponseEntity);
        Assert.assertEquals(OK, htmlResponseEntity.getStatusCode());
    }

    @Test
    public void testSingleton() {
        Object single = Singleton.get("single");
        System.out.println(single);
    }

    @Test
    public void testRawProperty() throws NoSuchFieldException {
        Class<PortalController> clazz = PortalController.class;
        Field property = clazz.getDeclaredField("rawProperty");
        Type genericType = property.getGenericType();
        // private CommonResult rawProperty;
        Assert.assertFalse(genericType instanceof ParameterizedType);
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
        String json = "{\"code\":200,\"pay\":\"操作成功\",\"data\":[{\"name\":\"Tom\",\"gender\":null,\"age\":10},{\"name\":\"Tom\",\"gender\":null,\"age\":10}]}";
        Object readValue = MAPPER.readValue(json, javaType);
        System.out.println(readValue);
    }

    @Test
    public void testDoubleDeserializeImplicitParamType() throws NoSuchFieldException {
        String json = "{\"code\":200,\"pay\":\"操作成功\",\"data\":[{\"name\":\"Tom\",\"gender\":null,\"age\":10},{\"name\":\"Tom\",\"gender\":null,\"age\":10}]}";
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
        String json = "{\"code\":200,\"pay\":\"操作成功\",\"data\":{\"name\":\"Tom\",\"gender\":null,\"age\":10}}";
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
        // {"code":200,"pay":"操作成功","data":[{"name":"Tom","gender":null,"age":10},{"name":"Tom","gender":null,"age":10}]}
        System.out.println(json);
        result = restTemplate.getForObject("http://localhost:8093/portal/singleNested", Object.class);
        json = MAPPER.writeValueAsString(result);
        // {"code":200,"pay":"操作成功","data":{"name":"Tom","gender":null,"age":10}}
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