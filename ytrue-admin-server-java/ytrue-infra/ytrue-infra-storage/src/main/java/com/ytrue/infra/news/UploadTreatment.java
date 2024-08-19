package com.ytrue.infra.news;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.news.wrapper.FileWrapper;
import lombok.Data;

@Data
public class UploadTreatment {

    /**
     * 保存文件名称
     */
    private String saveFileName;

    /**
     * 保存路劲
     */
    private String savePath;

    /**
     * fileWrapper
     */
    private FileWrapper fileWrapper;

    public String getSavePath() {
        if (StrUtil.isBlank(savePath)) {
            return StrUtil.EMPTY;
        }
        return savePath;
    }
}
