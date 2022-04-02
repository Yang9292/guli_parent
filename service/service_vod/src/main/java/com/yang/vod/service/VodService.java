package com.yang.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {

    //返回上传视频的id
    String uploadVideoAliyun(MultipartFile file);
    //根据视频id，删除阿里云上面对应的视频
    void deleteVideoById(String videoId);

    void deleteBatchVideo(List<String> videoIdList);
}
