package com.ytrue.infra.core.config;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.ytrue.infra.core.constant.DateFormat;
import com.ytrue.infra.core.ser.IdFieldLongTypeToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author ytrue
 * @date 2022/5/30 11:50
 * @description Jackson配置
 */

@Configuration
public class JacksonConfig {


    /**
     * Date转换器
     * 采用hutool的日期解析工具类Dateutil可以匹配任意格式日期字符串
     */
    @Bean
    public Converter<String, Date> dateConverter() {
        return source -> DateUtil.parse(source.trim());
    }

    /**
     * LocalDate转换器
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return source -> LocalDate.parse(source, DateTimeFormatter.ofPattern(DateFormat.NORM_DATE_PATTERN));
    }

    /**
     * LocalTime转换器
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return source -> LocalTime.parse(source, DateTimeFormatter.ofPattern(DateFormat.NORM_TIME_PATTERN));
    }

    /**
     * LocalDateTime转换器
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return source -> LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DateFormat.NORM_DATETIME_PATTERN));
    }

    /**
     * Json序列化和反序列化转换器
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        //java8日期 Local系列序列化和反序列化模块
        JavaTimeModule javaTimeModule = javaTimeModule();
        // 针对项目特定的规则
        SimpleModule simpleModule = new SimpleModule();
        // 雪花算法生成id，导致前端丢失精度处理
        simpleModule.addSerializer(Long.class, IdFieldLongTypeToStringSerializer.BIG_NUMBER_ENHANCE_INSTANCE);
        simpleModule.addSerializer(BigInteger.class, IdFieldLongTypeToStringSerializer.BIG_NUMBER_ENHANCE_INSTANCE);
        simpleModule.addSerializer(Long.TYPE, IdFieldLongTypeToStringSerializer.BIG_NUMBER_ENHANCE_INSTANCE);

        objectMapper
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(javaTimeModule)
                .registerModule(simpleModule);


        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        // 忽略json字符串中不识别的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略无法转换的对象
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // PrettyPrinter 格式化输出
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        //NULL不参与序列化--null是要参数序列化的,所以这里注释
        //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 指定时区
        objectMapper.setTimeZone(TimeZone.getTimeZone(com.ytrue.infra.core.constant.TimeZone.GMT8));

        /*
         * @JsonFormat(timezone = TimeZone.GMT8, pattern = DateFormat.NORM_DATETIME_PATTERN)
         * @DateTimeFormat(pattern = DateFormat.NORM_DATETIME_PATTERN)
         * Date日期类型字符串全局处理, 默认格式为：yyyy-MM-dd HH:mm:ss
         * 局部处理某个Date属性字段接收或返回日期格式yyyy-MM-dd, 可采用@JsonFormat(pattern = "yyyy-MM-dd", timezone=TimeZone.GMT8)注解标注该属性
         */
        objectMapper.setDateFormat(new SimpleDateFormat(DateFormat.NORM_DATETIME_PATTERN));

        return objectMapper;
    }

    public static JavaTimeModule javaTimeModule() {
        //java8日期 Local系列序列化和反序列化模块
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateFormat.NORM_DATETIME_PATTERN)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateFormat.NORM_DATE_PATTERN)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateFormat.NORM_TIME_PATTERN)));

        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateFormat.NORM_DATETIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateFormat.NORM_DATE_PATTERN)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateFormat.NORM_TIME_PATTERN)));

        return javaTimeModule;
    }


    public static GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        // 使用jackson序列化，使用这个会加@class ,这里要处理 LocalDateTime
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        genericJackson2JsonRedisSerializer.configure(objectMapper -> {
            //java8日期 Local系列序列化和反序列化模块
            objectMapper
                    .registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(javaTimeModule());
        });

        return genericJackson2JsonRedisSerializer;
    }

}
