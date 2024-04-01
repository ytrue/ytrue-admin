package com.ytrue.infra.core.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.ytrue.infra.core.constant.DateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author ytrue
 * @date 2023-11-22 9:34
 * @description 雪花分布式id获取
 */
public class SnowFlake {


    /**
     * 机器id
     */
    private static long workerId = 0L;

    /**
     * 机房id
     */
    public static long datacenterId = 0L;

    private static Snowflake snowflake;

    /**
     * 初始化配置
     *
     * @param workerId
     * @param datacenterId
     */
    public static void initialize(long workerId, long datacenterId) {
        snowflake = IdUtil.getSnowflake(workerId, datacenterId);
    }

    public static long getId() {
        return snowflake.nextId();
    }

    /**
     * 生成字符，带有前缀
     *
     * @param prefix
     * @return
     */
    public static String createStr(String prefix) {
        // 获取当前日期
        LocalDate localDate = LocalDate.now();
        // 格式化日期
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern(DateFormat.PURE_DATE_PATTERN));
        // 拼接
        return prefix + formattedDate + SnowFlake.getId();
    }

    public static String getIdStr() {
        return snowflake.nextId() + "";
    }
}
