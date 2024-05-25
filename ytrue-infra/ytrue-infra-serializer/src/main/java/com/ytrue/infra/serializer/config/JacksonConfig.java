package com.ytrue.infra.serializer.config;


import cn.hutool.core.date.DatePattern;
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
import com.ytrue.infra.serializer.ser.BigNumberOrSpecifyFieldToStrSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //java8日期 Local系列序列化和反序列化模块
        JavaTimeModule javaTimeModule = javaTimeModule();
        // 雪花算法生成id，导致前端丢失精度处理
        SimpleModule longToStrModule = longToStrModule();
        objectMapper
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(javaTimeModule)
                .registerModule(longToStrModule);

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

        /*
         * @JsonFormat(timezone = TimeZone.GMT8, pattern = DateFormat.NORM_DATETIME_PATTERN)
         * @DateTimeFormat(pattern = DateFormat.NORM_DATETIME_PATTERN)
         * Date日期类型字符串全局处理, 默认格式为：yyyy-MM-dd HH:mm:ss
         * 局部处理某个Date属性字段接收或返回日期格式yyyy-MM-dd, 可采用@JsonFormat(pattern = "yyyy-MM-dd", timezone=TimeZone.GMT8)注解标注该属性
         */
        // 指定时区
        // objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN));

        return objectMapper;
    }


    /**
     * 雪花算法生成id，导致前端丢失精度处理
     *
     * @return
     */
    private SimpleModule longToStrModule() {
        // 针对项目特定的规则
        SimpleModule simpleModule = new SimpleModule();
        // long转字符串的
        BigNumberOrSpecifyFieldToStrSerializer bigNumSer = new BigNumberOrSpecifyFieldToStrSerializer(Number.class);
        bigNumSer.setPerfectMatchList(List.of("id", "pid", "ids"));
        bigNumSer.setSuffixMatchList(List.of("Id", "Pid", "Ids"));
        bigNumSer.setEnableBigNumToStr(true);

        simpleModule.addSerializer(Long.class, bigNumSer);
        simpleModule.addSerializer(BigInteger.class, bigNumSer);
        simpleModule.addSerializer(Long.TYPE, bigNumSer);

//        看需求自行配置
//        simpleModule.addSerializer(Number.class, bigNumSer);
//        simpleModule.addSerializer(Integer.TYPE, bigNumSer);
//        simpleModule.addSerializer(Double.TYPE, bigNumSer);
//        simpleModule.addSerializer(Float.TYPE, bigNumSer);
//        simpleModule.addSerializer(Short.TYPE, bigNumSer);
//        simpleModule.addSerializer(Byte.TYPE, bigNumSer);
        return simpleModule;
    }


    /**
     * java8日期 Local系列序列化和反序列化模块
     *
     * @return
     */
    public JavaTimeModule javaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));

        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));

        return javaTimeModule;
    }

}
