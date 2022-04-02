package com.yang.serviceedu.controller;


import com.yang.commonutils.R;
import com.yang.serviceedu.entity.subject.OneSubject;
import com.yang.serviceedu.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-22
 */
@RestController
@RequestMapping("/serviceedu/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    //添加课程分类
    //获得上传的文件，把文件内容读取出来
    @PostMapping("addsubject")
    public R saveSubject(MultipartFile file){
        //调用读取文件内容方法
        eduSubjectService.saveSubject(eduSubjectService,file);
        return R.ok();
    }

    //获取全部课程列表
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        List<OneSubject> list = eduSubjectService.getAllSubject();
        return R.ok().data("list",list);
    }

}

