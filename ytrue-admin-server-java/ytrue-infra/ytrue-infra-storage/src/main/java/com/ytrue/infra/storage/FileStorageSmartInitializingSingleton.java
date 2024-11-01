package com.ytrue.infra.storage;

import cn.hutool.core.util.StrUtil;
import com.ytrue.infra.storage.factory.FileStorageFactory;
import com.ytrue.infra.storage.factory.platform.FileStoragePlatformFactory;
import com.ytrue.infra.storage.platform.FileStorage;
import com.ytrue.infra.storage.properties.BaseStorageProperties;
import com.ytrue.infra.storage.properties.StoragePropertiesProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Collections;
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


    @Override
    public void afterSingletonsInstantiated() {
        // 检查应用上下文是否为 AnnotationConfigServletWebServerApplicationContext 的实例
        if (applicationContext instanceof GenericApplicationContext ac) {
            // 注册 FileStorage 实例到应用上下文中，并获取注册的 FileStorage 实例映射
            Map<String, FileStorage> fileStorageMap = registerFileStorageBeans(ac);

            // 初始化 FileStorageFactory，确保所有存储实例在应用上下文中可用
            FileStorageFactory.initializeFileStoragesFromFileStorageMap(fileStorageMap);
        }
    }

    /**
     * 处理存储配置，将 FileStorage 实例注册到应用上下文中。
     *
     * @param ac Spring 应用上下文
     * @return 注册的 FileStorage 实例映射
     */
    private Map<String, FileStorage> registerFileStorageBeans(GenericApplicationContext ac) {
        // 从应用上下文中获取 StoragePropertiesProvider 实例
        StoragePropertiesProvider propertiesProvider = applicationContext.getBean(StoragePropertiesProvider.class);
        if (!propertiesProvider.isEnabled()) {
            return Collections.emptyMap(); // 如果未启用，返回空映射
        }

        // 构建平台工厂映射
        Map<String, FileStoragePlatformFactory> platformFactoryMap = buildPlatformFactoryMap(applicationContext);

        // 获取存储配置映射
        Map<String, Map<String, Object>> storageMap = propertiesProvider.getStorageMap();

        // 存储注册的 FileStorage 实例映射
        Map<String, FileStorage> registeredStorageMap = new HashMap<>();

        // 遍历存储配置
        storageMap.entrySet().stream()
                .filter(entry -> StrUtil.isNotBlank(entry.getKey())) // 过滤掉空标签
                .forEach(entry -> {
                    String tag = entry.getKey(); // 获取存储标签
                    Map<String, Object> config = entry.getValue(); // 获取配置项
                    String platform = (String) config.get("platform"); // 获取平台名称

                    // 跳过空平台
                    if (StrUtil.isBlank(platform)) {
                        return;
                    }

                    // 根据平台名称获取对应的工厂
                    FileStoragePlatformFactory factory = platformFactoryMap.get(platform);
                    // 跳过未注册的平台
                    if (factory == null) {
                        return;
                    }

                    // 获取存储属性并创建 FileStorage 实例
                    BaseStorageProperties storageProperties = propertiesProvider.getStorageProperties(tag, factory.getStoragePropertiesClass());
                    if (!storageProperties.isEnabled()) {
                        return; // 跳过未启用的存储属性
                    }

                    FileStorage fileStorage = factory.createInstance(storageProperties);
                    // 跳过未创建的 FileStorage 实例
                    if (fileStorage == null) {
                        return;
                    }

                    // 通过 FileStorageFactoryBean 创建bean 我要保障 factory.createInstance 的 fileStorage 和 ico里面 fileStorage 是一个对象
                    FileStorageFactoryBean fileStorageFactoryBean = new FileStorageFactoryBean(fileStorage);
                    GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                    beanDefinition.setBeanClass(fileStorageFactoryBean.getClass());
                    // 设置构造参数，类型为 FileStorage
                    ConstructorArgumentValues constructorArgs = new ConstructorArgumentValues();
                    constructorArgs.addGenericArgumentValue(fileStorage);
                    beanDefinition.setConstructorArgumentValues(constructorArgs);

                    // 注册 BeanDefinition
                    String beanName = tag + "FileStorage";
                    // 注册 FileStorage 实例
                    ac.registerBeanDefinition(beanName, beanDefinition);
                    // 将注册的实例添加到映射中
                    // registeredStorageMap.put(tag, ac.getBean(beanName,fileStorage.getClass()));
                    registeredStorageMap.put(tag, fileStorage);
                });

        // 返回注册的 FileStorage 实例映射
        return registeredStorageMap;
    }


    /**
     * 构建平台工厂映射。
     *
     * @param applicationContext Spring 应用上下文，用于获取所有的 FileStoragePlatformFactory 实例。
     * @return 返回一个映射，键为平台名称，值为对应的 FileStoragePlatformFactory 实例。
     * @throws RuntimeException 如果发现重复的平台名称，将抛出此异常。
     */
    private Map<String, FileStoragePlatformFactory> buildPlatformFactoryMap(ApplicationContext applicationContext) {
        // 从应用上下文中获取所有的 FileStoragePlatformFactory 实例
        Map<String, FileStoragePlatformFactory> factories = applicationContext.getBeansOfType(FileStoragePlatformFactory.class);

        // 创建一个用于存储平台工厂的映射
        Map<String, FileStoragePlatformFactory> platformFactoryMap = new HashMap<>();

        // 遍历所有工厂
        for (FileStoragePlatformFactory factory : factories.values()) {
            // 获取当前工厂的平台注册名
            String platform = factory.platform();

            // 检查平台名是否有效
            if (StrUtil.isNotBlank(platform)) {
                // 检查映射中是否已经存在该平台
                if (platformFactoryMap.containsKey(platform)) {
                    // 如果存在，抛出异常，提示重复的平台名称
                    throw new RuntimeException("已存在平台: " + platform);
                }
                // 将当前工厂放入映射中
                platformFactoryMap.put(platform, factory);
            }
        }

        // 返回构建好的平台工厂映射
        return platformFactoryMap;
    }


}
