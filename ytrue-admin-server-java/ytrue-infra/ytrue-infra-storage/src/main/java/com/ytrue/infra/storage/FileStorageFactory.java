package com.ytrue.infra.storage;

import com.ytrue.infra.storage.platform.FileStorage;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件存储工厂类，用于根据平台类型创建和管理对应的 FileStorage 实例。
 * 该类实现了 ApplicationContextAware 接口，可以获取 Spring 容器的上下文，
 * 并在初始化时自动注册所有 FileStorage 类型的 Bean。
 */
public class FileStorageFactory {

    // 存储所有 FileStorage 实例的线程安全 Map
    private static final Map<String, FileStorage> STORAGE_INSTANCES = new ConcurrentHashMap<>();

    // 标志位
    private static boolean initialized = false;


    /**
     * 获取存储实例的不可修改视图。
     *
     * @return 不可修改的 STORAGE_INSTANCES 视图
     */
    public static Map<String, FileStorage> getStorageInstances() {
        return Collections.unmodifiableMap(STORAGE_INSTANCES);
    }


    /**
     * 从 Spring 容器中获取所有 FileStorage 类型的 Bean，并将其注册到 STORAGE_INSTANCES 中。
     * 该方法应由 FileStorageSmartInitializingSingleton 调用，确保在所有单例 Bean 初始化完成后执行。
     *
     * @param applicationContext Spring 应用上下文
     * @throws IllegalStateException 如果已经初始化过
     */
    public static void initializeFileStoragesFromApplicationContext(ApplicationContext applicationContext) {
        if (initialized) {
            throw new IllegalStateException("FileStorage 实例已经初始化过，不可重复调用。");
        }

        // 获取所有 FileStorage 类型的 Bean
        Map<String, FileStorage> fileStorageMap = applicationContext.getBeansOfType(FileStorage.class);

        for (String beanName : fileStorageMap.keySet()) {
            String key = extractKeyFromBeanName(beanName);

            // 检查是否已存在相同的 key
            if (STORAGE_INSTANCES.containsKey(key)) {
                throw new IllegalStateException("键已存在: " + key);
            }

            registerFileStorage(key, fileStorageMap.get(beanName));
        }
        // 设置为已初始化
        initialized = true;
    }

    /**
     * 根据给定的 key 获取对应的 FileStorage 实例。
     *
     * @param key 存储实例的键
     * @return 对应的 FileStorage 实例
     * @throws IllegalArgumentException 如果给定的 key 为 null
     * @throws NoSuchElementException   如果没有找到对应的 FileStorage 实例
     */
    public static FileStorage getFileStorage(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key 不能为 null");
        }

        FileStorage fileStorage = STORAGE_INSTANCES.get(key);
        if (fileStorage == null) {
            throw new NoSuchElementException("没有找到对应的 FileStorage 实例: " + key);
        }
        return fileStorage;
    }

    /**
     * 注册 FileStorage 实例，如果键已存在，则抛出异常。
     *
     * @param key         存储实例的键
     * @param fileStorage 要注册的 FileStorage 实例
     */
    public static void registerFileStorage(String key, FileStorage fileStorage) {
        if (STORAGE_INSTANCES.containsKey(key)) {
            throw new IllegalStateException("键已存在: " + key);
        }
        STORAGE_INSTANCES.put(key, fileStorage);
    }

    /**
     * 注册 FileStorage 实例，如果键已存在，则直接覆盖原有实例。
     *
     * @param key         存储实例的键
     * @param fileStorage 要注册的 FileStorage 实例
     */
    public static void registerFileStorageWithOverride(String key, FileStorage fileStorage) {
        // 直接覆盖
        STORAGE_INSTANCES.put(key, fileStorage);
    }

    /**
     * 从 STORAGE_INSTANCES 中删除指定的 FileStorage 实例。
     *
     * @param key 存储实例的键
     * @return 被删除的 FileStorage 实例，如果不存在则返回 null
     * @throws IllegalArgumentException 如果键为 null
     */
    public static FileStorage removeFileStorage(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key 不能为 null");
        }
        return STORAGE_INSTANCES.remove(key);
    }

    /**
     * 从 Bean 名称提取出对应的存储实例键。
     * 如果 Bean 名称以 "FileStorage" 结尾，则将其去掉，作为存储实例的键。
     *
     * @param beanName Spring 容器中的 Bean 名称
     * @return 提取出的存储实例键
     * @throws IllegalArgumentException 如果 beanName 为 null 或无效
     */
    private static String extractKeyFromBeanName(String beanName) {
        if (beanName == null) {
            throw new IllegalArgumentException("beanName 不能为 null");
        }

        // 使用正则表达式匹配以 "FileStorage" 结尾的情况
        String key = beanName.replaceAll("(FileStorage)$", "");

        // 如果 key 为空，抛出异常
        if (key.isEmpty()) {
            throw new IllegalArgumentException("无效的 beanName: " + beanName);
        }
        return key;
    }
}
