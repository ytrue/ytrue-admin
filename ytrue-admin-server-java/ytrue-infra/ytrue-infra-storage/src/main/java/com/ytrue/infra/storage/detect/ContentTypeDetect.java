package com.ytrue.infra.storage.detect;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 识别文件的 MIME 类型的接口。
 * 该接口提供多种方法用于检测给定文件或字节流的 MIME 类型。
 */
public interface ContentTypeDetect {

    /**
     * 根据给定的文件识别其 MIME 类型。
     *
     * @param file 要检测的文件
     * @return 文件的 MIME 类型，如果无法识别则返回 null
     * @throws IOException 如果在读取文件时发生错误
     */
    String detect(File file) throws IOException;

    /**
     * 根据给定的字节数组识别其 MIME 类型。
     *
     * @param bytes 要检测的字节数组
     * @return 字节数组的 MIME 类型，如果无法识别则返回 null
     */
    String detect(byte[] bytes);

    /**
     * 根据给定的字节数组和文件名识别其 MIME 类型。
     *
     * @param bytes   要检测的字节数组
     * @param filename 文件名，可能用于更准确的 MIME 类型识别
     * @return 字节数组的 MIME 类型，如果无法识别则返回 null
     */
    String detect(byte[] bytes, String filename);

    /**
     * 根据给定的输入流和文件名识别其 MIME 类型。
     *
     * @param in       要检测的输入流
     * @param filename 文件名，可能用于更准确的 MIME 类型识别
     * @return 输入流的 MIME 类型，如果无法识别则返回 null
     * @throws IOException 如果在读取输入流时发生错误
     */
    String detect(InputStream in, String filename) throws IOException;
}
