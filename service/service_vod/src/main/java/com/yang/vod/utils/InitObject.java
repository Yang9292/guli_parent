package com.yang.vod.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 阿里云上传音视频的SDK初始化类与初始化方法：在后端服务器中可以代替浏览器去访问网络API接口
 */
public class InitObject {

    //填入AccessKey信息：SDK初始化方法
    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws Exception {
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

}
