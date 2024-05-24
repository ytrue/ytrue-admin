package com.ytrue.infra.news;

import com.ytrue.infra.news.wrapper.FileWrapper;
import lombok.Data;

@Data
public class UploadPretreatment {

    /**
     * 保存文件名称
     */
    private String saveFileName;

    /**
     * fileWrapper
     */
    private FileWrapper fileWrapper;
}
