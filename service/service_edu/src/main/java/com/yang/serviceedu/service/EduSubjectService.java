package com.yang.serviceedu.service;

import com.yang.commonutils.R;
import com.yang.serviceedu.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.serviceedu.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-22
 */
public interface EduSubjectService extends IService<EduSubject> {

    //读取文件内容业务逻辑
    R saveSubject(EduSubjectService eduSubjectService, MultipartFile multipartFile);

    //获取分类课程列表
    List<OneSubject> getAllSubject();
}
