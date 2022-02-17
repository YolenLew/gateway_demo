package com.lew;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lew.entity.CommonResult;
import com.lew.entity.User;
import org.junit.Test;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Yolen
 * @date 2022/1/27
 */
public class GatewayApplicationTest {

    private char separator = PathContainer.Options.HTTP_PATH.separator();

    private static final ObjectMapper mapper = new ObjectMapper();

    private static User user = new User();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        user.setAge(10);
        user.setName("Tom");
    }

    @Test
    public void testJson() throws JsonProcessingException {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("code", "200");
        map.put("message", "操作成功");
        map.put("data", "hello");
        map.put("redundant", "unwanted property");
        String jsonStr = mapper.writeValueAsString(map);
        System.out.println("jsonStr ------------>");
        System.out.println(jsonStr);
        CommonResult commonResult = mapper.readValue(jsonStr, CommonResult.class);
        System.out.println("commonResult ------------>");
        System.out.println(commonResult);
    }

    @Test
    public void testConvertValue() throws JsonProcessingException {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("code", "200");
        map.put("message", "操作成功");
        map.put("data", user);
        map.put("redundant", "unwanted property");

        CommonResult commonResult = mapper.convertValue(map, CommonResult.class);
        System.out.println("commonResult ------------>");
        System.out.println(commonResult);
    }












    private PathPattern parse(String path) {
        PathPatternParser pp = new PathPatternParser();
//        pp.setMatchOptionalTrailingSlash(true);
        return pp.parse(path);
    }

    @Test
    public void optionalTrailingSeparators() {
        // LiteralPathElement
        PathPattern pp = parse("/resource");
        assertTrue(pp.matches(PathContainer.parsePath("/resource")));
        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
        pp = parse("/resource/");
        assertFalse(pp.matches(PathContainer.parsePath("/resource")));
        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
        assertFalse(pp.matches(PathContainer.parsePath("/resource/1")));
        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
        // RegexPathElement
        pp = parse("/{var1}_{var2}");
        assertTrue(pp.matches(PathContainer.parsePath("/res1_res2")));
        assertEquals("res1", pp.matchAndExtract(PathContainer.parsePath("/res1_res2")).getUriVariables().get("var1"));
        assertEquals("res2", pp.matchAndExtract(PathContainer.parsePath("/res1_res2")).getUriVariables().get("var2"));
        assertTrue(pp.matches(PathContainer.parsePath("/res1_res2/")));
        assertEquals("res1", pp.matchAndExtract(PathContainer.parsePath("/res1_res2/")).getUriVariables().get("var1"));
        assertEquals("res2", pp.matchAndExtract(PathContainer.parsePath("/res1_res2/")).getUriVariables().get("var2"));
        assertFalse(pp.matches(PathContainer.parsePath("/res1_res2//")));
        pp = parse("/{var1}_{var2}/");
        assertFalse(pp.matches(PathContainer.parsePath("/res1_res2")));
        assertTrue(pp.matches(PathContainer.parsePath("/res1_res2/")));
        assertEquals("res1", pp.matchAndExtract(PathContainer.parsePath("/res1_res2/")).getUriVariables().get("var1"));
        assertEquals("res2", pp.matchAndExtract(PathContainer.parsePath("/res1_res2/")).getUriVariables().get("var2"));
        assertFalse(pp.matches(PathContainer.parsePath("/res1_res2//")));
        pp = parse("/{var1}*");
        assertTrue(pp.matches(PathContainer.parsePath("/a")));
        assertTrue(pp.matches(PathContainer.parsePath("/a/")));
        // no characters for var1
        assertFalse(pp.matches(PathContainer.parsePath("/")));
        // no characters for var1
        assertFalse(pp.matches(PathContainer.parsePath("//")));

    }

    @Test
    public void testRegexParse() {
        PathPattern pp = parse("/*/{*var2}");
        assertTrue(pp.matches(PathContainer.parsePath("/a/1.jpg")));
        assertTrue(pp.matches(PathContainer.parsePath("/a/b/1.jpg")));
        assertTrue(pp.matches(PathContainer.parsePath("/a/b/c/1.jpg")));
        pp = parse("{var1}.{var2}/{*var3}");
        assertTrue(pp.matches(PathContainer.parsePath(new StringBuilder("/a/1.jpg").reverse().toString())));
        assertTrue(pp.matches(PathContainer.parsePath(new StringBuilder("/a/b/1.jpg").reverse().toString())));
        assertTrue(pp.matches(PathContainer.parsePath(new StringBuilder("/a/b/c/1.jpg").reverse().toString())));
        assertFalse(pp.matches(PathContainer.parsePath(new StringBuilder("/a").reverse().toString())));
        assertFalse(pp.matches(PathContainer.parsePath(new StringBuilder("/a/1").reverse().toString())));
    }
//    @Test
//    public void optionalTrailingSeparators() {
//        // LiteralPathElement
//        PathPattern pp = parse("/resource");;
//        assertTrue(pp.matches(PathContainer.parsePath("/resource")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        pp = parse("/resource/");
//        assertFalse(pp.matches(PathContainer.parsePath("/resource")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        // SingleCharWildcardPathElement
//        pp = parse("/res?urce");
//        assertTrue(pp.matches(PathContainer.parsePath("/resource")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        pp = parse("/res?urce/");
//        assertFalse(pp.matches(PathContainer.parsePath("/resource")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        // CaptureVariablePathElement
//        pp = parse("/{var}");
//        assertTrue(pp.matches(PathContainer.parsePath("/resource")));
//        assertEquals("resource", pp.matchAndExtract(PathContainer.parsePath("/resource")).get("var"));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertEquals("resource", pp.matchAndExtract(PathContainer.parsePath("/resource/")).get("var"));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        pp = parse("/{var}/");
//        assertFalse(pp.matches(PathContainer.parsePath("/resource")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertEquals("resource", pp.matchAndExtract(PathContainer.parsePath("/resource/")).get("var"));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        // CaptureTheRestPathElement
//        pp = parse("/{*var}");
//        assertTrue(pp.matches(PathContainer.parsePath("/resource")));
//        assertEquals(PathContainer.parsePath("/resource"), pp.matchAndExtract(PathContainer.parsePath("/resource")).get("var"));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertEquals("/resource/", pp.matchAndExtract(PathContainer.parsePath("/resource/")).get("var"));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource//")));
//        assertEquals("/resource//", pp.matchAndExtract("/resource//").get("var"));
//        assertTrue(pp.matches("//resource//"));
//        assertEquals("//resource//", pp.matchAndExtract("//resource//").get("var"));
//        // WildcardTheRestPathElement
//        pp = parse("/**");
//        assertTrue(pp.matches(PathContainer.parsePath("/resource")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource//")));
//        assertTrue(pp.matches("//resource//"));
//        // WildcardPathElement
//        pp = parse("/*");
//        assertTrue(pp.matches(PathContainer.parsePath("/resource")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        pp = parse("/*/");
//        assertFalse(pp.matches(PathContainer.parsePath("/resource")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        // RegexPathElement
//        pp = parse("/{var1}_{var2}");
//        assertTrue(pp.matches(PathContainer.parsePath("/res1_res2")));
//        assertEquals("res1", pp.matchAndExtract(PathContainer.parsePath("/res1_res2")).get("var1"));
//        assertEquals("res2", pp.matchAndExtract(PathContainer.parsePath("/res1_res2")).get("var2"));
//        assertTrue(pp.matches("/res1_res2/"));
//        assertEquals("res1", pp.matchAndExtract("/res1_res2/").get("var1"));
//        assertEquals("res2", pp.matchAndExtract("/res1_res2/").get("var2"));
//        assertFalse(pp.matches(PathContainer.parsePath("/res1_res2//")));
//        pp = parse("/{var1}_{var2}/");
//        assertFalse(pp.matches(PathContainer.parsePath("/res1_res2")));
//        assertTrue(pp.matches("/res1_res2/"));
//        assertEquals("res1", pp.matchAndExtract("/res1_res2/").get("var1"));
//        assertEquals("res2", pp.matchAndExtract("/res1_res2/").get("var2"));
//        assertFalse(pp.matches(PathContainer.parsePath("/res1_res2//")));
//        pp = parse("/{var1}*");
//        assertTrue(pp.matches("/a"));
//        assertTrue(pp.matches("/a/"));
//        // no characters for var1
//        assertFalse(pp.matches("/"));
//        // no characters for var1
//        assertFalse(pp.matches("//"));
//        // Now with trailing matching turned OFF
//        PathPatternParser parser = new PathPatternParser();
//        parser.setMatchOptionalTrailingSlash(false);
//        // LiteralPathElement
//        pp = parser.parse("/resource");;
//        assertTrue(pp.matches(PathContainer.parsePath("/resource")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource/")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        pp = parser.parse("/resource/");
//        assertFalse(pp.matches(PathContainer.parsePath("/resource")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        // SingleCharWildcardPathElement
//        pp = parser.parse("/res?urce");
//        assertTrue(pp.matches(PathContainer.parsePath("/resource")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource/")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        pp = parser.parse("/res?urce/");
//        assertFalse(pp.matches(PathContainer.parsePath("/resource")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        // CaptureVariablePathElement
//        pp = parser.parse("/{var}");
//        assertTrue(pp.matches(PathContainer.parsePath("/resource")));
//        assertEquals("resource", pp.matchAndExtract(PathContainer.parsePath("/resource")).get("var"));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource/")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        pp = parser.parse("/{var}/");
//        assertFalse(pp.matches(PathContainer.parsePath("/resource")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertEquals("resource", pp.matchAndExtract(PathContainer.parsePath("/resource/")).get("var"));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        // CaptureTheRestPathElement
//        pp = parser.parse("/{*var}");
//        assertTrue(pp.matches(PathContainer.parsePath("/resource")));
//        assertEquals(PathContainer.parsePath("/resource"), pp.matchAndExtract(PathContainer.parsePath("/resource")).get("var"));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertEquals("/resource/", pp.matchAndExtract(PathContainer.parsePath("/resource/")).get("var"));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource//")));
//        assertEquals("/resource//", pp.matchAndExtract("/resource//").get("var"));
//        assertTrue(pp.matches("//resource//"));
//        assertEquals("//resource//", pp.matchAndExtract("//resource//").get("var"));
//        // WildcardTheRestPathElement
//        pp = parser.parse("/**");
//        assertTrue(pp.matches(PathContainer.parsePath("/resource")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource//")));
//        assertTrue(pp.matches("//resource//"));
//        // WildcardPathElement
//        pp = parser.parse("/*");
//        assertTrue(pp.matches(PathContainer.parsePath("/resource")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource/")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        pp = parser.parse("/*/");
//        assertFalse(pp.matches(PathContainer.parsePath("/resource")));
//        assertTrue(pp.matches(PathContainer.parsePath("/resource/")));
//        assertFalse(pp.matches(PathContainer.parsePath("/resource//")));
//        // RegexPathElement
//        pp = parser.parse("/{var1}_{var2}");
//        assertTrue(pp.matches(PathContainer.parsePath("/res1_res2")));
//        assertEquals("res1", pp.matchAndExtract(PathContainer.parsePath("/res1_res2")).get("var1"));
//        assertEquals("res2", pp.matchAndExtract(PathContainer.parsePath("/res1_res2")).get("var2"));
//        assertFalse(pp.matches("/res1_res2/"));
//        assertFalse(pp.matches(PathContainer.parsePath("/res1_res2//")));
//        pp = parser.parse("/{var1}_{var2}/");
//        assertFalse(pp.matches(PathContainer.parsePath("/res1_res2")));
//        assertTrue(pp.matches("/res1_res2/"));
//        assertEquals("res1", pp.matchAndExtract("/res1_res2/").get("var1"));
//        assertEquals("res2", pp.matchAndExtract("/res1_res2/").get("var2"));
//        assertFalse(pp.matches(PathContainer.parsePath("/res1_res2//")));
//        pp = parser.parse("/{var1}*");
//        assertTrue(pp.matches("/a"));
//        assertFalse(pp.matches("/a/"));
//        // no characters for var1
//        assertFalse(pp.matches("/"));
//        // no characters for var1
//        assertFalse(pp.matches("//"));
//    }

}