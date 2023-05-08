package com.ytrue.tools.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

/**
 * @author ytrue
 * @date 2023/5/7 18:05
 * @description LocalStorageProperties
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class LocalStorageProperties extends BaseStorageProperties {
    /**
     * 本地存储路径
     */
    private String fileHost = "storage/";

    /**
     * 访问域名
     */
    private String domain = "http://127.0.0.1:9932/";

}
