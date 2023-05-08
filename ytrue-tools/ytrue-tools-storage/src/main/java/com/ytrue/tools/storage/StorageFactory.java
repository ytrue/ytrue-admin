package com.ytrue.tools.storage;

import cn.hutool.core.util.ReflectUtil;
import com.ytrue.tools.storage.exception.StorageRuntimeException;
import com.ytrue.tools.storage.platform.IStorage;
import com.ytrue.tools.storage.properties.BaseStorageProperties;

import java.util.HashMap;
import java.util.Map;

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
    public static IStorage getInstance(String key) {
        IStorage storage = storageSingletonObject.get(key);
        if (storage == null) {
            throw new StorageRuntimeException("未定义该类型");
        }
        return storage;
    }

    /**
     * 获取实例
     *
     * @param key
     * @param config
     * @return
     */
    public static IStorage getInstance(String key, BaseStorageProperties config) {
        Class<? extends IStorage> storageClass = storageSingletonClassName.get(key);
        if (storageClass == null) {
            throw new StorageRuntimeException("未定义该类型");
        }
        // 反射区构建
        return ReflectUtil.newInstance(storageClass, config);
    }

    /**
     * 添加或者修改
     *
     * @param key
     * @param storage
     */
    public static void putMap(String key, IStorage storage) {
        storageSingletonObject.put(key, storage);
        storageSingletonClassName.put(key, storage.getClass());
    }

    /**
     * 删除
     *
     * @param key
     */
    public static void removeMap(String key) {
        storageSingletonObject.remove(key);
        storageSingletonClassName.remove(key);
    }
}
