package com.ytrue.infra.storage.properties;

import lombok.Data;

/**
 * BaseStorageProperties 是所有存储平台配置的基类，包含了公共的属性，如存储桶名称和公共域名。
 *
 * <p>该类定义了所有存储服务平台所需的基本配置属性，子类可以根据不同的存储平台扩展具体配置。
 * 通过使用该基类，可以避免在每个存储平台的配置类中重复定义相同的属性。</p>
 */
@Data
public class BaseStorageProperties {

    /**
     * 公共域名，用于访问存储在该平台上的文件。
     * <p>此属性用于构建访问URL，使得用户能够通过域名直接访问存储的文件。</p>
     */
    protected String domain;

    /**
     * 文件主机，表示文件存储的基础路径或主机名。
     * <p>此属性可用于定义文件的存储位置或组织结构，便于管理和访问。</p>
     */
    protected String fileHost;

    /**
     * 所属平台
     *
     * @see com.ytrue.infra.storage.enums.FileStoragePlatformEnum
     */
    protected String platform;


    protected boolean enabled = Boolean.TRUE;
}
