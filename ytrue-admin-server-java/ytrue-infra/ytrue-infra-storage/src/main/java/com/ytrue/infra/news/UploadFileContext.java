package com.ytrue.infra.news;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.news.wrapper.FileWrapper;
import lombok.Data;

/**
 * 上传文件上下文类，用于保存上传文件的信息和处理逻辑。
 * 包含文件的保存名称、保存路径和文件包装器。
 *
 * @author ytrue
 * @date 2024/10/29
 */
@Data
public class UploadFileContext {

    /**
     * 保存文件名称。
     * 用于指定在服务器或存储系统中保存的文件名称。
     */
    private String saveFileName;

    /**
     * 保存路径。
     * 表示文件在服务器或存储系统中的存储路径。
     */
    private String savePath;

    /**
     * 文件包装器。
     * 封装文件的相关信息和操作，例如文件内容、元数据等。
     */
    private FileWrapper fileWrapper;

    /**
     * 获取保存路径。
     * 如果保存路径为空或为空白，返回一个空字符串；否则，返回保存路径的值。
     *
     * @return 返回保存路径字符串
     */
    public String getSavePath() {
        if (StrUtil.isBlank(savePath)) {
            return StrUtil.EMPTY;
        }
        return savePath;
    }
}
