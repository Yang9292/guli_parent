package com.yang.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.yang.commonutils.R;
import com.yang.servicebase.exceptionhandler.GuliException;
import com.yang.vod.service.VodService;
import com.yang.vod.utils.ConstantVodUtils;
import com.yang.vod.utils.InitObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/servicevod/video")
@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;

    @ApiOperation("上传视频到阿里云，返回视频id")
    @PostMapping("/uploadAliyunVideo")
    public R uploadAliyunVideo(MultipartFile file){
        //返回上传视频的id
        String videoId = vodService.uploadVideoAliyun(file);
        return R.ok().data("videoId",videoId);
    }

    //根据视频id，删除阿里云上面对应的视频
    @ApiOperation("根据视频id，删除阿里云上面对应的视频")
    @DeleteMapping("/deleteVideoById/{videoId}")
    public R deleteVideoById(@PathVariable("videoId") String videoId){
        vodService.deleteVideoById(videoId);
        return R.ok();
    }

    @ApiOperation("根据视频id，批量删除阿里云上面对应的小节视频")
    @DeleteMapping("/delete-batch")
    public R deleteBatchVideo(@RequestParam("videoIdList") List<String> videoIdList){
        vodService.deleteBatchVideo(videoIdList);
        return R.ok();
    }

    //根据视频ID获取视频播放凭证
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id) {
        try {
            //创建初始化对象
            DefaultAcsClient client = InitObject.initVodClient(
                    ConstantVodUtils.KEY_ID,
                    ConstantVodUtils.KEY_SECRET);
            //创建获取凭证request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            //向request设置视频id
            request.setVideoId(id);
            //调用方法得到凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth", playAuth);
        } catch (Exception e) {
            throw new GuliException(20001,"获取视频凭证失败....");
        }
    }

}
