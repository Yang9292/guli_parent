package com.yang.vod.service.impl;


import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.yang.servicebase.exceptionhandler.GuliException;
import com.yang.vod.service.VodService;
import com.yang.vod.utils.ConstantVodUtils;
import com.yang.vod.utils.InitObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

import static com.yang.vod.utils.InitObject.initVodClient;

@Service
public class VodServiceImpl implements VodService {

    @Override
    public String uploadVideoAliyun(MultipartFile file) {
        try{
            String accessKeyId = ConstantVodUtils.KEY_ID;
            String accessKeySecret = ConstantVodUtils.KEY_SECRET;
            //fileName：上传文件原始名称
            String fileName = file.getOriginalFilename();
            //title:上传到阿里云上面视频显示的名称
            String title = fileName.substring(0,fileName.lastIndexOf(".")); //上传之后文件的名称
            //inputStream：上传文件的输入流
            InputStream inputStream = file.getInputStream();

            UploadStreamRequest request = new UploadStreamRequest(accessKeyId,accessKeySecret,title,fileName,inputStream);
            UploadVideoImpl uploadVideo = new UploadVideoImpl();
            UploadStreamResponse response = uploadVideo.uploadStream(request);

            System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
            String videoId = null;
            if (response.isSuccess()) {
                System.out.println("成功！");
                videoId = response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                videoId = response.getVideoId();
                System.out.println("失败！");
                System.out.println(response.getMessage());
                System.out.println(response.getCode());
            }
            return videoId;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteVideoById(String videoId) {
        try {
            //初始化一个客户端
            DefaultAcsClient client =InitObject.initVodClient(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(videoId);
            DeleteVideoResponse response = client.getAcsResponse(request);
            System.out.println("RequestId = "+ response.getRequestId()+"\n");
        } catch (Exception e) {
            throw new GuliException(20001,"删除视频失败！");
        }
    }

    @Override
    public void deleteBatchVideo(List<String> videoIdList) {
        try {
            //初始化一个客户端
            DefaultAcsClient client =InitObject.initVodClient(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET);
            DeleteVideoRequest request = new DeleteVideoRequest();
            //将集合视频id转换为，以逗号隔开的字符串
            String videoId = StringUtils.join(videoIdList.toArray(), ",");//使用工具类遍历拼接
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(videoId);
            DeleteVideoResponse response = client.getAcsResponse(request);
            System.out.println("RequestId = "+ response.getRequestId()+"\n");
        } catch (Exception e) {
            throw new GuliException(20001,"批量删除小节视频失败！");
        }
    }
}
