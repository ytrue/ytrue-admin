package com.ytrue.infra.storage.properties;


import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * StoragePropertiesProvider 是一个配置类，负责加载和管理所有存储平台的配置。
 * 通过 Spring 的 @ConfigurationProperties 注解，能够将配置文件中的内容映射到 Java 对象中。
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "ytrue.storage")
public class StoragePropertiesProvider {

    /**
     * 存储平台配置的集合
     * -- SETTER --
     * 设置存储平台的配置。
     *
     * @param storage 存储平台配置的映射
     * <p>
     * Map<String, String> 对应得是BaseStorageProperties子类
     */
    private Map<String, Map<String, Object>> storageMap = new HashMap<>();

    private boolean enabled = false;


    public <T extends BaseStorageProperties> T getStorageProperties(String tag, Class<T> storagePropertiesClass) {
        Gson gson = new Gson();

        T properties = null;

        for (String key : storageMap.keySet()) {
            Map<String, Object> value = storageMap.get(key);
            if (key.equals(tag)) {
                // 序列化设置的吧
                String json = gson.toJson(value);
                properties = gson.fromJson(json, storagePropertiesClass);
            }
        }
        if (Objects.isNull(properties)) {
            throw new NullPointerException("null");
        }

        return properties;
    }
}
