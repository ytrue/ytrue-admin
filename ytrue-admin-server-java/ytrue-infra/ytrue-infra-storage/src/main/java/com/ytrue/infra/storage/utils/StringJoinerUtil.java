package com.ytrue.infra.storage.utils;

import cn.hutool.core.util.StrUtil;

/**
 * @description: StringJoinerUtil
 * @author ytrue
 * @date 2024/10/29 15:06
 */
public class StringJoinerUtil {

    /**
     * 将多个字符串拼接为以斜杠分隔的形式。
     *
     * @param parts 可变参数，多个字符串
     * @return 拼接后的字符串
     */
    public static String joinWithSlash(String... parts) {
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            if (StrUtil.isNotBlank(part)) {
                // 移除前后的斜杠，并添加到结果中
                result.append(part.replaceAll("^/|/$", "")).append("/");
            }
        }

        // 如果结果不为空，去掉最后一个斜杠
        if (!result.isEmpty()) {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }
}
