package com.ytrue.infra.news.detect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 基于 Tika 识别文件的 MIME 类型
 */
@Getter
@Setter
@NoArgsConstructor
public class TikaContentTypeDetect implements ContentTypeDetect {

    public Tika getTika() {
        return new Tika();
    }

    @Override
    public String detect(File file) throws IOException {
        return getTika().detect(file);
    }

    @Override
    public String detect(byte[] bytes) {
        return getTika().detect(bytes);
    }

    @Override
    public String detect(byte[] bytes, String filename) {
        return getTika().detect(bytes, filename);
    }

    @Override
    public String detect(InputStream in, String filename) throws IOException {
        return getTika().detect(in, filename);
    }
}
