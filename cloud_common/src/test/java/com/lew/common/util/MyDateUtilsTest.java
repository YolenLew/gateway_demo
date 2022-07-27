package com.lew.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

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
    public void testClockWithZone() {
        Clock c1 = Clock.systemUTC(); //系统默认UTC时钟（当前瞬时时间 System.currentTimeMillis()）
        //这么来会采用系统默认的时区
        Clock c2 = Clock.systemDefaultZone(); //系统默认时区时钟（当前瞬时时间）
        //输出那两个能看到效果
        log.info("c1：{}", c1); //SystemClock[Z]  这个其实用得最多
        log.info("c2：{}", c2); //SystemClock[Asia/Shanghai]
        assertEquals(c1.getZone(), ZoneOffset.UTC.normalized());
        assertEquals(c2.getZone(), ZoneId.systemDefault());
        //可以获取到和时区敏感的对象
        Clock c3 = Clock.system(ZoneId.of("Europe/Paris")); //巴黎时区
        Clock c5 = Clock.offset(c1, Duration.ofSeconds(2)); //相对于系统默认时钟两秒的时钟
        log.info("c3: {}", c3);
        log.info("c5: {}", c5);
    }

    @Test
    public void testInstanceUntil() {
        //自带的解析ZoneId.systemDefault()
        Instant temp = Instant.parse("2022-07-16T10:23:40.00Z");
        log.info("temp: {}", temp);

        Instant now = Instant.now();
        long hourDuration = 25L;
        Instant instant = now.plusSeconds(TimeUnit.HOURS.toSeconds(hourDuration));
        assertEquals(1L, now.until(instant, ChronoUnit.DAYS));
        assertEquals(hourDuration, now.until(instant, ChronoUnit.HOURS));
        assertEquals(-hourDuration, instant.until(now, ChronoUnit.HOURS));

        Instant start = Instant.now();
        log.info("do something...");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Instant end = Instant.now();
        //计算时间差 采用Duration来处理时间戳的差
        Duration timeElapsed = Duration.between(start, end);
        long millis = timeElapsed.toMillis();
        log.info("millis: {}", millis);
        LocalTime evning = LocalTime.of(21, 0);
        log.info("evning: {}", evning);
    }

    @Test
    public void testZoneId() {
        ZoneId localZoneId = ZoneId.of("Asia/Shanghai");
        assertEquals(Clock.systemDefaultZone().getZone(), localZoneId);
    }

    @Test
    public void dateTimeFormatter() {
        //字符串转化为日期对象
        String dateStr = "2022年07月17日";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        // 专门的api解析
        LocalDate localDate = LocalDate.parse(dateStr, dateTimeFormatter);
        log.info("localDate: {}", localDate);

        //日期转换为字符串
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm a");
        String nowStr = now.format(formatter);
        log.info("nowStr: {}", nowStr);

        //2022-07-17
        String isoFormat = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.of(2022, 7, 17));
        assertEquals("2022-07-17", isoFormat);
        //20220717
        String basicIsoFormat = DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.of(2022, 7, 17));
        assertEquals("20220717", basicIsoFormat);
        //2022-07-17T09:10:00
        String isoDateTimeFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.of(2022, 7, 17, 9, 10, 0));
        assertEquals("2022-07-17T09:10:00", isoDateTimeFormat);
    }

    /**
     * Date和LocalDate、LocalTime等互相转化的的思想也很简单：借助LocalDateTime对象就万无一失
     */
    @Test
    public void dateToDateTime() {
        Date date = new Date();
        Instant instant = date.toInstant();

        //以ZoneId.systemDefault转换成LocalDateTime后，就可以随意转换了
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // 方式一：使用LocalDate、LocalTime的from
        LocalDate localDate = LocalDate.from(localDateTime);
        LocalTime localTime = LocalTime.from(localDateTime);

        // 方式二：直接to的方式 LocalDateTime -> LocalDate LocalTime
        LocalDate toLocalDate = localDateTime.toLocalDate();
        LocalTime toLocalTime = localDateTime.toLocalTime();

        assertEquals(localDate, toLocalDate);
        assertEquals(localTime, toLocalTime);
    }

    /**
     * LocalDate、LocalTime转化为Date：借助的中间变量Instant即可
     */
    @Test
    public void localToDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        Instant instant;
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset zoneOffset = OffsetDateTime.now().getOffset();

        //LocalDateTime转Instant转Date
        instant = localDateTime.atZone(zoneId).toInstant();
        Instant toInstant = localDateTime.toInstant(zoneOffset);
        assertEquals(instant, toInstant);
        Date fromLocalDateTime = Date.from(instant);
        log.info("fromLocalDateTime: {}", fromLocalDateTime);

        //LocalDate转Instant转Date
        instant = localDate.atStartOfDay(zoneId).toInstant();
        Date fromLocalDate = Date.from(instant);
        log.info("fromLocalDate: {}", fromLocalDate);

        //LocalTime转Instant转Date（很麻烦 一般杜绝这样使用吧）
        //必须先借助localDate转换成localDateTime 在转成instant 再转date
        LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);
        instant = dateTime.atZone(zoneId).toInstant();
        Date fromLocalTime = Date.from(instant);
        log.info("fromLocalTime: {}", fromLocalTime);
    }

    @Test
    public void getAllDateBetweenPeriod() {
        LocalDate start = LocalDate.of(2021, Month.NOVEMBER, 3);
        LocalDate end = LocalDate.of(2022, Month.JULY, 24);

        long monthCount = ChronoUnit.MONTHS.between(start, end);
        log.info("monthCount: {}",monthCount);
        List<LocalDate> localDateList = Stream.iterate(start, localDate -> localDate.plusMonths(1)).limit(monthCount + 1).collect(Collectors.toList());
        assertEquals(monthCount + 1, localDateList.size());
    }
}