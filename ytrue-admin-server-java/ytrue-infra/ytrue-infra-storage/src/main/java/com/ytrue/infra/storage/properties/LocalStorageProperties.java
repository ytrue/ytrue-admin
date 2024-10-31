package com.ytrue.infra.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description LocalStorageProperties
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class LocalStorageProperties extends BaseStorageProperties {

    /**
     * 本地存储路径
     */
    protected String fileHost = "storage";
}
