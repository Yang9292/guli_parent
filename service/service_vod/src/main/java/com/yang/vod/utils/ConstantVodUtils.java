package com.yang.vod.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

/**
 * 获取属性文件中的阿里云vod配置信息，
 */
@Component
public class ConstantVodUtils implements InitializingBean {

    @Value("${aliyun.vod.file.keyid}")
    private String keyId;
    @Value("${aliyun.vod.file.keysecret}")
    private String keySecret;

    public static String KEY_ID;
    public static String KEY_SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        KEY_ID = this.keyId;
        KEY_SECRET = this.keySecret;
    }
}
