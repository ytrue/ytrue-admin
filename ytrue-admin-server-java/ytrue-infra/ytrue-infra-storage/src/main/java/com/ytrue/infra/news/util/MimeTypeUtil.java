package com.ytrue.infra.news.util;

import cn.hutool.core.util.ArrayUtil;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

public class MimeTypeUtil {

    /**
     * 获取首选的文件扩展名。
     * 根据文件名和 MIME 类型，返回一个最合适的文件扩展名。
     *
     * @param fileName 文件名，包括扩展名
     * @param mimeType 文件的 MIME 类型
     * @return 首选的文件扩展名
     * @throws MimeTypeException 如果无法解析 MIME 类型时抛出异常
     */
    public static String getPreferredFileExtension(String fileName, String mimeType) throws MimeTypeException {
        // 根据文件名获取文件后缀
        String fileSuffixByFileName = getFileExtensionByFileName(fileName);

        // 根据 MIME 类型获取所有相关的文件后缀
        String[] fileSuffixByMimeTypeArray = getFileExtensionByMimeType(mimeType);

        // 如果文件名中的后缀与 MIME 类型匹配，返回文件名中的后缀
        if (ArrayUtil.contains(fileSuffixByMimeTypeArray, fileSuffixByFileName)) {
            return fileSuffixByFileName;
        }
        // 否则返回 MIME 类型对应的第一个后缀
        return fileSuffixByMimeTypeArray[0];
    }

    /**
     * 通过 MIME 类型获取文件扩展名。
     * 根据给定的 MIME 类型，返回一个或多个与之相关联的文件扩展名。
     *
     * @param mimeType 文件的 MIME 类型
     * @return 与 MIME 类型相关联的文件扩展名数组
     * @throws MimeTypeException 如果无法解析 MIME 类型时抛出异常
     */
    public static String[] getFileExtensionByMimeType(String mimeType) throws MimeTypeException {
        // 获取所有 MIME 类型对象
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        // 根据 MIME 类型名称获取具体的 MIME 类型对象
        MimeType type = allTypes.forName(mimeType);
        // 获取与该 MIME 类型相关联的文件扩展名，并返回数组
        return type.getExtensions().toArray(new String[0]);
    }

    /**
     * 通过文件名获取文件扩展名。
     * 根据文件名，提取出最后的扩展名部分。
     *
     * @param fileName 文件名，包括扩展名
     * @return 文件的扩展名，如果没有扩展名则返回空字符串
     */
    public static String getFileExtensionByFileName(String fileName) {
        // 查找最后一个点号的位置
        int lastDotIndex = fileName.lastIndexOf(".");
        // 如果找到点号并且不在开头位置，返回从点号开始的后缀
        if (lastDotIndex != -1 && lastDotIndex != 0) {
            return fileName.substring(lastDotIndex);
        } else {
            // 如果文件名不包含点号，则返回空字符串
            return "";
        }
    }
}
