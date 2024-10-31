package com.ytrue.infra.storage;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.storage.enums.FileStoragePlatformEnum;
import com.ytrue.infra.storage.platform.*;
import com.ytrue.infra.storage.properties.*;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件存储初始化单例类，实现 SmartInitializingSingleton 接口。
 * 用于在 Spring 容器初始化后，自动注册文件存储相关的 Bean。
 *
 * @author ytrue
 * @description: FileStorageSmartInitializingSingleton
 * @date 2024/10/30 17:21
 */
public class FileStorageSmartInitializingSingleton implements SmartInitializingSingleton {

    private final ApplicationContext applicationContext;

    /**
     * 构造函数，接收 Spring 应用上下文。
     *
     * @param applicationContext Spring 应用上下文
     */
    public FileStorageSmartInitializingSingleton(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 在所有单例 Bean 初始化完成后调用此方法。
     * 主要用于创建文件存储映射并注册到 Spring 容器中。
     */
    @Override
    public void afterSingletonsInstantiated() {
        StoragePropertiesProvider propertiesProvider = applicationContext.getBean(StoragePropertiesProvider.class);

        if (applicationContext instanceof AnnotationConfigServletWebServerApplicationContext ac) {
            Map<String, BaseStorageProperties> storagePropertiesMap = createStoragePropertiesMap(propertiesProvider);
            Map<String, FileStorage> fileStorageMap = createFileStorageMap(storagePropertiesMap);

            registerFileStorageBeans(ac, fileStorageMap);
            // FileStorageFactory#STORAGE_INSTANCES 初始化
            FileStorageFactory.initializeFileStoragesFromApplicationContext(applicationContext);
        }
    }

    /**
     * 创建存储属性映射。
     *
     * @param storagePropertiesProvider 存储属性提供者
     * @return 存储属性映射
     */
    private Map<String, BaseStorageProperties> createStoragePropertiesMap(StoragePropertiesProvider storagePropertiesProvider) {
        Map<String, BaseStorageProperties> propertiesMap = new HashMap<>();

        storagePropertiesProvider.getStorageMap().forEach((key, value) -> {
            String platform = (String) value.get("platform");
            if (StrUtil.isBlank(platform)) return;

            FileStoragePlatformEnum platformEnum = getPlatformEnum(platform);
            if (platformEnum == null) return;

            Class<? extends BaseStorageProperties> propertiesClass = getPropertiesClass(platformEnum);
            if (propertiesClass != null) {
                propertiesMap.put(key, storagePropertiesProvider.getStorageProperties(key, propertiesClass));
            }
        });
        return propertiesMap;
    }

    /**
     * 创建文件存储映射。
     *
     * @param storagePropertiesMap 存储属性映射
     * @return 文件存储映射
     */
    private Map<String, FileStorage> createFileStorageMap(Map<String, BaseStorageProperties> storagePropertiesMap) {
        Map<String, FileStorage> fileStorageMap = new HashMap<>();

        storagePropertiesMap.forEach((key, properties) -> {
            FileStoragePlatformEnum platformEnum = getPlatformEnum(properties.getPlatform());
            if (platformEnum == null) return;

            FileStorage fileStorage = createFileStorage(platformEnum, properties);
            if (fileStorage != null) {
                fileStorageMap.put(key, fileStorage);
            }
        });

        return fileStorageMap;
    }

    /**
     * 注册文件存储 Bean。
     *
     * @param ac             应用上下文
     * @param fileStorageMap 文件存储映射
     */
    private void registerFileStorageBeans(AnnotationConfigServletWebServerApplicationContext ac, Map<String, FileStorage> fileStorageMap) {
        fileStorageMap.forEach((key, fileStorage) -> {
            if (fileStorage instanceof AbstractFileStorage<?> abstractFileStorage) {
                String beanName = key + "FileStorage";
                ac.registerBean(beanName, fileStorage.getClass(), abstractFileStorage.getConfig());
            }
        });
    }

    /**
     * 根据平台字符串获取对应的枚举。
     *
     * @param platform 平台字符串
     * @return 平台枚举
     */
    private FileStoragePlatformEnum getPlatformEnum(String platform) {
        try {
            return FileStoragePlatformEnum.valueOf(platform.toUpperCase());
        } catch (Exception e) {
            return null; // 转换失败返回 null
        }
    }

    /**
     * 根据平台枚举获取对应的属性类。
     *
     * @param platformEnum 平台枚举
     * @return 对应的属性类
     */
    private Class<? extends BaseStorageProperties> getPropertiesClass(FileStoragePlatformEnum platformEnum) {
        return switch (platformEnum) {
            case OSS -> AliyunOssStorageProperties.class;
            case LOCAL -> LocalStorageProperties.class;
            case KODO -> QiniuKodoStorageProperties.class;
            case COS -> TencentCosStorageProperties.class;
        };
    }

    /**
     * 根据平台枚举和属性创建文件存储对象。
     *
     * @param platformEnum 平台枚举
     * @param properties   存储属性
     * @return 文件存储对象
     */
    private FileStorage createFileStorage(FileStoragePlatformEnum platformEnum, BaseStorageProperties properties) {
        return switch (platformEnum) {
            case OSS -> new AliyunOssFileStorage((AliyunOssStorageProperties) properties);
            case LOCAL -> null; // 本地存储未实现
            case KODO -> new QiniuKodoFileStorage((QiniuKodoStorageProperties) properties);
            case COS -> new TencentCosFileStorage((TencentCosStorageProperties) properties);
        };
    }
}
