//package com.ytrue.infra.serializer.config;
//
//import cn.hutool.core.date.DatePattern;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//
//@Configuration
//public class GsonConfig {
//
//
//    @Bean
//    public Gson gson() {
//        // gson定制
//        GsonBuilder gsonBuilder = new GsonBuilder();
//
//        gsonBuilder.disableHtmlEscaping();
//        // PrettyPrinter 格式化输出
//        gsonBuilder.setPrettyPrinting();
//
//        // 指定时区
//        gsonBuilder.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
//        //gsonBuilder.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//
//        // 设置 Gson 解析器的宽松模式
//        gsonBuilder.setLenient();
//
//        // NULL也是参与序列化的
//        gsonBuilder.serializeNulls();
//
//        // 处理java8日期部分
//        buildGsonTimeModule(gsonBuilder);
//        // 插件gson对象
//        return gsonBuilder.create();
//    }
//
//
//    public void buildGsonTimeModule(GsonBuilder gsonBuilder) {
//        //java8日期 Local系列序列化和反序列化模块
//        // LocalDateTime
//        gsonBuilder.registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (localDateTime, type, jsonSerializationContext) ->
//                jsonSerializationContext.serialize(localDateTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN))));
//        gsonBuilder.registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (jsonElement, type, jsonDeserializationContext) ->
//                LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
//
//        // LocalDate
//        gsonBuilder.registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (localDate, type, jsonSerializationContext) ->
//                jsonSerializationContext.serialize(localDate.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN))));
//        gsonBuilder.registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (jsonElement, type, jsonDeserializationContext) ->
//                LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN)));
//
//        // LocalTime
//        gsonBuilder.registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (localTime, type, jsonSerializationContext) ->
//                jsonSerializationContext.serialize(localTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN))));
//        gsonBuilder.registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (jsonElement, type, jsonDeserializationContext) ->
//                LocalTime.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN)));
//    }
//
//}
