package com.yang.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.yang.oss.service.OssService;
import com.yang.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadFileAvator(MultipartFile file) {

        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //获得输入流
            InputStream inputStream = file.getInputStream();
            //获得文件上传名称
            String originalFilename = file.getOriginalFilename();
            //解决重名文件会覆盖问题
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            originalFilename = uuid+originalFilename;
            //按照日期对文件进行分类
            String dateTime = new DateTime().toString("yyyy/MM/dd");
            originalFilename = dateTime +"/" + originalFilename;
            // 创建PutObject请求。
            //第一个参数 BUCKET_NAME的值
            //第二个参数 上传到oss中的文件路径和文件名
            //第三个参数 输入流
            ossClient.putObject(bucketName, originalFilename, inputStream);
            //关闭ossClient
            ossClient.shutdown();
            //获取文件url
            //https://edu-9292.oss-cn-beijing.aliyuncs.com/cc.jpg
            String url = "https://"+bucketName+"."+endpoint+"/"+originalFilename;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
