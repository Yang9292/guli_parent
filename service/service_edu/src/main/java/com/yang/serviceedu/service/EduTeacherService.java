package com.yang.serviceedu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.serviceedu.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-18
 */
public interface EduTeacherService extends IService<EduTeacher> {

    Map<String,Object> getTeacherFrontPageList(Page<EduTeacher> page);
}
