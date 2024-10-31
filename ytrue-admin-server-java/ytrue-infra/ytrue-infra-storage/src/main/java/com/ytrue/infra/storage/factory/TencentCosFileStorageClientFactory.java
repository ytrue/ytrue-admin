package com.ytrue.infra.storage.factory;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import com.ytrue.infra.storage.properties.TencentCosStorageProperties;
import lombok.Getter;

/**
 * 腾讯云 COS 文件存储客户端工厂实现。
 * 此类负责创建和管理腾讯云 COS 客户端实例。
 *
 * @author ytrue
 * @date 2024/05/23
 */
public class TencentCosFileStorageClientFactory implements FileStorageClientFactory<COSClient> {

    @Getter
    private final TencentCosStorageProperties config;  // 腾讯云 COS 存储配置

    private volatile COSClient client;  // 腾讯云 COS 客户端实例

    /**
     * 带参数的构造函数，用于初始化腾讯云 COS 存储配置。
     *
     * @param config 腾讯云 COS 存储的配置信息，包括 Secret ID、Secret Key 和 Region
     */
    public TencentCosFileStorageClientFactory(TencentCosStorageProperties config) {
        this.config = config;
    }

    /**
     * 获取腾讯云 COS 客户端实例。
     * 该方法实现了懒加载机制，确保客户端在首次调用时创建，
     * 并在多线程环境中保持线程安全。
     *
     * @return 返回腾讯云 COS 客户端实例
     */
    @Override
    public COSClient getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    // 创建 COSCredentials 对象，包含 Secret ID 和 Secret Key
                    COSCredentials cred = new BasicCOSCredentials(config.getSecretId(), config.getSecretKey());

                    // 创建 ClientConfig 对象，配置 Region 和 HTTP 协议
                    ClientConfig clientConfig = new ClientConfig(new Region(config.getRegion()));
                    clientConfig.setHttpProtocol(HttpProtocol.https);

                    // 创建 COSClient 实例
                    client = new COSClient(cred, clientConfig);
                }
            }
        }
        return client;
    }
}
