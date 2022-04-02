package com.yang.ucenter.utils;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 常量类：从属性配置文件中获取相关属性值
 */
@Component
public class ConstantPropertiesUtil implements InitializingBean {

    @Value("${wx.open.app_id}")
    private String appId;
    @Value("${wx.open.app_secret}")
    private String appSecret;
    @Value("${wx.open.redirect_url}")
    private String redirectUrl;

    public static String WX_APP_ID;
    public static String WX_APP_SECRET;
    public static String WX_REDIRECT_URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        WX_APP_ID = this.appId;
        WX_APP_SECRET = this.appSecret;
        WX_REDIRECT_URL = this.redirectUrl;
    }
}
