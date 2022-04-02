package com.yang.serviceedu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.commonutils.R;
import com.yang.serviceedu.entity.EduCourse;
import com.yang.serviceedu.entity.vo.CourseInfoForm;
import com.yang.serviceedu.entity.vo.CoursePublishVo;
import com.yang.serviceedu.entity.vo.CourseQuery;
import com.yang.serviceedu.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-22
 */
@RestController
@RequestMapping("/serviceedu/course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;

    //条件查询带分页功能
    @PostMapping("pageCourseCondition/{nowpage}/{limit}")
    public R pageCourseCondition(@PathVariable int nowpage,
                                  @PathVariable int limit,
                                  @RequestBody(required = false) CourseQuery courseQuery){
        //创建Page对象
        Page<EduCourse> page = new Page<>(nowpage,limit);
        //构建查询条件
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //多条件查询
        String parentId = courseQuery.getParentId();
        String teacherId = courseQuery.getTeacherId();
        String subjectId = courseQuery.getSubjectId();

        if (!StringUtils.isEmpty(parentId)){
            wrapper.like("subject_parentId",parentId);
        }
        if (!StringUtils.isEmpty(teacherId)){
            wrapper.eq("teacher_id",teacherId);
        }
        if (!StringUtils.isEmpty(subjectId)){
            wrapper.gt("subject_id",subjectId);
        }
        //排序
        wrapper.orderByDesc("gmt_create");
        //调用方法进行分页查询
        eduCourseService.page(page,wrapper);
        //获得数据总行数
        long total = page.getTotal();
        //获取条件查询后的分页信息
        List<EduCourse> records = page.getRecords();
        return R.ok().data("totals",total).data("rows",records);

    }

    //添加课程
    @PostMapping("addCourse")
    public R addCourseInfo(@RequestBody CourseInfoForm courseInfoForm){

        String id = eduCourseService.addCourseInfo(courseInfoForm);
        return R.ok().data("courseId",id);
    }

    //根据课程id查询课程信息
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){

        CourseInfoForm courseInfoForm = eduCourseService.getCourseInfo(courseId);

        return R.ok().data("courseInfoForm",courseInfoForm);
    }

    //修改课程信息
    @PostMapping("uploadCourseInfo")

    public R uploadCourseInfo(@RequestBody CourseInfoForm courseInfoForm){

        eduCourseService.uploadCourseInfo(courseInfoForm);

        return R.ok();
    }

    //根据id最终提交表单信息
    @GetMapping("publishCourse/{courseId}")
    public R publishCourse(@PathVariable String courseId){

        CoursePublishVo coursePublishVo = eduCourseService.getPublishCourse(courseId);

        return R.ok().data("coursePublish",coursePublishVo);

    }

    //最终提交按钮
    @PostMapping("publishButton/{courseId}")
    public R publishButton(@PathVariable String courseId){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        eduCourse.setStatus("Normal");
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }

    //删除课程
    @DeleteMapping("/removeCourseById/{courseId}")
    public R removeCourseById(@PathVariable("courseId") String courseId){
        boolean flag = eduCourseService.deleteCourseById(courseId);
        if (flag) {
            return R.ok();
        }else {
//            throw new LmfException(20001,"课程删除失败！");
            return R.error();
        }
    }

}

