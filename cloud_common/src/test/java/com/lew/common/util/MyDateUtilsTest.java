package com.lew.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * @author Yolen
 * @date 2022/7/3
 */
@Slf4j
public class MyDateUtilsTest {

    @Before
    public void setUp() {
    }

    /**
     * 版权声明：本文为CSDN博主「方向盘(YourBatman)」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/f641385712/article/details/81429389
     */
    @Test
    public void testInstant() {
        Clock c1 = Clock.systemUTC(); //系统默认UTC时钟（当前瞬时时间 System.currentTimeMillis()）
        //这么来会采用系统默认的时区
        Clock c2 = Clock.systemDefaultZone(); //系统默认时区时钟（当前瞬时时间）
        //输出那两个能看到效果
        log.info("c1：{}", c1); //SystemClock[Z]  这个其实用得最多
        log.info("c2：{}", c2); //SystemClock[Asia/Shanghai]
        Assert.assertEquals(c1.getZone(), ZoneOffset.UTC.normalized());
        Assert.assertEquals(c2.getZone(), ZoneId.systemDefault());
        //可以获取到和时区敏感的对象
        Clock c3 = Clock.system(ZoneId.of("Europe/Paris")); //巴黎时区
        Clock c5 = Clock.offset(c1, Duration.ofSeconds(2)); //相对于系统默认时钟两秒的时钟
    }
}