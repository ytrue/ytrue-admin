package com.ytrue.infra.news.factory;

import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import com.ytrue.infra.storage.properties.TencentCosStorageProperties;
import lombok.Getter;

/**
 * @author ytrue
 * @date 2024/5/23 18:05
 * @description TencentCosFileStorageClientFactory
 */
public class TencentCosFileStorageClientFactory implements FileStorageClientFactory<COSClient> {

    @Getter
    private TencentCosStorageProperties config;

    private volatile COSClient client;

    public TencentCosFileStorageClientFactory(TencentCosStorageProperties config) {
        this.config = config;
    }

    @Override
    public COSClient getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    COSCredentials cred = new BasicCOSCredentials(config.getSecretId(), config.getSecretKey());
                    ClientConfig clientConfig = new ClientConfig(new Region(config.getRegion()));
                    clientConfig.setHttpProtocol(HttpProtocol.https);
                    client = new COSClient(cred, clientConfig);
                }
            }
        }
        return client;
    }
}
