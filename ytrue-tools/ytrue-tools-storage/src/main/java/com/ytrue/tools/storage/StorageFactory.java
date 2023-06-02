package com.ytrue.tools.storage;

import cn.hutool.core.util.ReflectUtil;
import com.ytrue.tools.storage.exception.StorageRuntimeException;
import com.ytrue.tools.storage.platform.IStorage;
import com.ytrue.tools.storage.properties.IStorageProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ytrue
 * @date 2023/4251 15:40
 * @description StorageFactory
 */
public class StorageFactory {

    public static Map<String, IStorage> storageSingletonObject = new HashMap<>();

    public static Map<String, Class<? extends IStorage>> storageSingletonClassName = new HashMap<>();

    private StorageFactory() {
    }

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static GenericStorageImpl getInstance(String key) {
        IStorage storage = storageSingletonObject.get(key);
        if (storage == null) {
            throw new StorageRuntimeException("未定义该类型");
        }
        // 增强一下
        return new GenericStorageImpl(storage);
    }

    /**
     * 获取实例
     *
     * @param key
     * @param config
     * @return
     */
    public static GenericStorageImpl getInstance(String key, IStorageProperties config) {
        Class<? extends IStorage> storageClass = storageSingletonClassName.get(key);
        if (storageClass == null) {
            throw new StorageRuntimeException("未定义该类型");
        }
        // 反射区构建
        return new GenericStorageImpl(ReflectUtil.newInstance(storageClass, config));
    }

    /**
     * 添加或者修改
     *
     * @param key
     * @param storage
     */
    public static void registerStorage(String key, IStorage storage) {
        storageSingletonObject.put(key, storage);
        storageSingletonClassName.put(key, storage.getClass());
    }

    /**
     * 删除
     *
     * @param key
     */
    public static void removeStorage(String key) {
        storageSingletonObject.remove(key);
        storageSingletonClassName.remove(key);
    }
}
