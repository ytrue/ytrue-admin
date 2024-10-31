package com.ytrue.infra.storage.builder;

import com.ytrue.infra.storage.entity.UploadFileContext;
import com.ytrue.infra.storage.wrapper.FileWrapper;

/**
 * UploadFileContextBuilder 用于方便地创建 UploadFileContext 实例。
 *
 * <p>此类通过链式调用设置文件包装器、保存路径和保存文件名等属性，最终生成对应的 UploadFileContext 实例。</p>
 */
public class UploadFileContextBuilder {

    private Object source;              // 文件源（可以是不同类型的文件）
    private FileWrapper fileWrapper;    // 文件包装器
    private String savePath;            // 保存路径
    private String saveFileName;        // 保存文件名

    /**
     * 设置文件源。
     *
     * @param source 文件源（可以是文件路径、字节数组等）
     * @return 当前 FileWrapperBuilder 实例
     */
    public UploadFileContextBuilder withSource(Object source) {
        this.source = source;
        return this;
    }

    /**
     * 设置文件包装器。
     *
     * @param fileWrapper 文件包装器实例
     * @return 当前 UploadFileContextBuilder 实例
     */
    public UploadFileContextBuilder withFileWrapper(FileWrapper fileWrapper) {
        this.fileWrapper = fileWrapper;
        return this;
    }

    /**
     * 设置保存路径。
     *
     * @param savePath 保存路径
     * @return 当前 UploadFileContextBuilder 实例
     */
    public UploadFileContextBuilder withSavePath(String savePath) {
        this.savePath = savePath;
        return this;
    }

    /**
     * 设置保存文件名。
     *
     * @param saveFileName 保存文件名
     * @return 当前 UploadFileContextBuilder 实例
     */
    public UploadFileContextBuilder withSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
        return this;
    }

    /**
     * 创建 UploadFileContext 实例。
     *
     * @return 构建的 UploadFileContext 实例
     * @throws IllegalArgumentException 如果文件包装器未设置
     */
    public UploadFileContext build() {
        if (fileWrapper == null && source != null) {
            try {
                fileWrapper = new FileWrapperBuilder().withSource(source).build();
            } catch (Exception ignored) {
            }
        }

        // 如果 fileWrapper 仍为 null，抛出异常
        if (fileWrapper == null) {
            throw new IllegalArgumentException("必须设置 FileWrapper 或提供源");
        }

        // 创建并返回 UploadFileContext 实例
        UploadFileContext context = new UploadFileContext();
        context.setFileWrapper(fileWrapper);
        context.setSavePath(savePath);
        context.setSaveFileName(saveFileName);
        return context;
    }
}
