package com.yang.serviceedu.controller.front;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.commonutils.R;
import com.yang.serviceedu.entity.EduCourse;
import com.yang.serviceedu.entity.EduTeacher;
import com.yang.serviceedu.service.EduCourseService;
import com.yang.serviceedu.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/serviceedu/indexFront")
public class IndexFrontController {
    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduTeacherService eduTeacherService;

    //查询前8名热门课程以及前4名热门讲师

    @GetMapping("index")
    public R index(){
        //查询前8名课程
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("limit 8");
        List<EduCourse> courseList = eduCourseService.list(queryWrapper);

        //前4名讲师
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 4");
        List<EduTeacher> teacherList = eduTeacherService.list(wrapper);


        return R.ok().data("courseList",courseList).data("teacherList",teacherList);
    }
}
