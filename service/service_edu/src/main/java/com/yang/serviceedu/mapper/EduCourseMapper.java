package com.yang.serviceedu.mapper;

import com.yang.serviceedu.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.serviceedu.entity.frontVo.CourseWebVo;
import com.yang.serviceedu.entity.vo.CourseInfoForm;
import com.yang.serviceedu.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-22
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    //根据ID获取最终提交表单
    public CoursePublishVo getPublishCourseInfo(String courseId);

    //根据课程ID查询课程基本信息
    CourseWebVo getFrontCourseInfo(String courseId);

}
