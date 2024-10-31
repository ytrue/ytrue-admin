package com.ytrue.infra.storage.detect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 基于 Tika 实现的文件 MIME 类型识别器。
 * 此类利用 Apache Tika 库来检测给定文件或字节流的 MIME 类型。
 */
@Getter
@Setter
@NoArgsConstructor
public class TikaContentTypeDetect implements ContentTypeDetect {

    /**
     * 创建并返回一个新的 Tika 实例。
     *
     * @return Tika 实例
     */
    public Tika getTika() {
        return new Tika();
    }

    /**
     * 根据给定的文件识别其 MIME 类型。
     *
     * @param file 要检测的文件
     * @return 文件的 MIME 类型，如果无法识别则返回 null
     * @throws IOException 如果在读取文件时发生错误
     */
    @Override
    public String detect(File file) throws IOException {
        return getTika().detect(file);
    }

    /**
     * 根据给定的字节数组识别其 MIME 类型。
     *
     * @param bytes 要检测的字节数组
     * @return 字节数组的 MIME 类型，如果无法识别则返回 null
     */
    @Override
    public String detect(byte[] bytes) {
        return getTika().detect(bytes);
    }

    /**
     * 根据给定的字节数组和文件名识别其 MIME 类型。
     *
     * @param bytes   要检测的字节数组
     * @param filename 文件名，可能用于更准确的 MIME 类型识别
     * @return 字节数组的 MIME 类型，如果无法识别则返回 null
     */
    @Override
    public String detect(byte[] bytes, String filename) {
        return getTika().detect(bytes, filename);
    }

    /**
     * 根据给定的输入流和文件名识别其 MIME 类型。
     *
     * @param in       要检测的输入流
     * @param filename 文件名，可能用于更准确的 MIME 类型识别
     * @return 输入流的 MIME 类型，如果无法识别则返回 null
     * @throws IOException 如果在读取输入流时发生错误
     */
    @Override
    public String detect(InputStream in, String filename) throws IOException {
        return getTika().detect(in, filename);
    }
}
