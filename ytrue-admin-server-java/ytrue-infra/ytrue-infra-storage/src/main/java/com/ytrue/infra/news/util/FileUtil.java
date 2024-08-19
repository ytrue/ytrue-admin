package com.ytrue.infra.news.util;

import cn.hutool.core.util.ArrayUtil;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

public class FileUtil {


    /**
     * 获取首选的文件扩展名
     *
     * @param fileName
     * @param mimeType
     * @return
     * @throws MimeTypeException
     */
    public static String getPreferredFileExtension(String fileName, String mimeType) throws MimeTypeException {
        String fileSuffixByFileName = getFileExtensionByFileName(fileName);

        String[] fileSuffixByMimeTypeArray = getFileExtensionByMimeType(mimeType);

        if (ArrayUtil.contains(fileSuffixByMimeTypeArray, fileSuffixByFileName)) {
            return fileSuffixByFileName;
        }
        return fileSuffixByMimeTypeArray[0];
    }


    /**
     * 通过 mimeType 获取文件后缀
     *
     * @param mimeType
     * @return
     * @throws MimeTypeException
     */
    public static String[] getFileExtensionByMimeType(String mimeType) throws MimeTypeException {
        // 获取MIME类型对象
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        MimeType type = allTypes.forName(mimeType);
        // 获取文件后缀
        return type.getExtensions().toArray(new String[0]);
    }

    /**
     * 通过文件名称获取 文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileExtensionByFileName(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex != -1 && lastDotIndex != 0) {
            return fileName.substring(lastDotIndex);
        } else {
            // 如果文件名不包含点号，则返回空字符串
            return "";
        }
    }
}
