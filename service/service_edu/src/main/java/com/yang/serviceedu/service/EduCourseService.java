package com.yang.serviceedu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.serviceedu.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.serviceedu.entity.frontVo.CourseFrontVo;
import com.yang.serviceedu.entity.frontVo.CourseWebVo;
import com.yang.serviceedu.entity.vo.CourseInfoForm;
import com.yang.serviceedu.entity.vo.CoursePublishVo;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-22
 */
public interface EduCourseService extends IService<EduCourse> {

    //添加课程信息
    String addCourseInfo(CourseInfoForm courseInfoForm);
    //根据课程id查询课程信息
    CourseInfoForm getCourseInfo(String courseId);
    //修改课程信息
    void uploadCourseInfo(CourseInfoForm courseInfoForm);
    //根据id最终提交表单信息
    CoursePublishVo getPublishCourse(String courseId);

    boolean deleteCourseById(String courseId);
    //前台课程带条件分页查询
    Map<String,Object> getCourseFrontList(Page<EduCourse> page, CourseFrontVo courseFrontVo);
    //根据课程ID查询课程基本信息
    CourseWebVo getFrontCourseInfo(String courseId);

}
