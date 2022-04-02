package com.yang.oss.controller;

import com.yang.commonutils.R;
import com.yang.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;

    @PostMapping
    //上传头像
    public R uploadFileAvator(MultipartFile file){
       String url = ossService.uploadFileAvator(file);
        return R.ok().data("url",url);
    }
}
