package com.ytrue.infra.storage.util;

import cn.hutool.core.util.StrUtil;

/**
 * @author ytrue
 * @date 2023/5/7 18:05
 * @description PathUtil
 */
public class PathUtil {

    /**
     * 拼接url
     *
     * @param strList
     * @return
     */
    public static String montagePath(String... strList) {

        StringBuilder retStr = new StringBuilder();
        for (String s : strList) {
            String str = s;
            // 空白不处理
            if (StrUtil.isBlank(str)) {
                continue;
            }

            // 判断有没有开头和结尾有没有 / 或者 \
            if (str.startsWith("/") || str.startsWith("\\")) {
                str = StrUtil.sub(str, 1, str.length());

            }

            if (str.endsWith("/") || str.endsWith("\\")) {
                str = StrUtil.sub(str, 0, -1);
            }
            str = str.replaceAll("\\\\", "/");
            String separator = "".contentEquals(retStr) ? "" : "/";
            retStr.append(separator).append(str);
        }
        return retStr.toString();
    }
}
