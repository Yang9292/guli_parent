package com.yang.serviceedu.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.commonutils.R;
import com.yang.serviceedu.entity.EduCourse;
import com.yang.serviceedu.entity.EduTeacher;
import com.yang.serviceedu.service.EduCourseService;
import com.yang.serviceedu.service.EduTeacherService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/serviceedu/teacherFront")
public class TeacherFrontController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @Autowired
    private EduCourseService eduCourseService;

    //前台分页查询讲师
    @GetMapping("/getTeacherPage/{nowpage}/{limit}")
    public R getTeacherPage(@PathVariable Integer nowpage,
                             @PathVariable Integer limit){

        Page<EduTeacher> page = new Page<>(nowpage,limit);
        Map<String,Object> map = eduTeacherService.getTeacherFrontPageList(page);
        return R.ok().data("map",map);
    }

    //根据ID查询指定讲师和课程
    @GetMapping("getTeacherInfo/{id}")
    public R getTeacherInfo(@PathVariable String id){

        //根据ID查询指定讲师信息
        EduTeacher teacher = eduTeacherService.getById(id);

        //根据ID查询课程信息
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",id);
        List<EduCourse> courseList = eduCourseService.list(wrapper);

        return R.ok().data("teacher",teacher).data("courseList",courseList);
    }

    //小程序根据分类查询讲师信息
    @GetMapping("minigetTeacherInfo/{sort}")
    public R minigetTeacherInfo(@PathVariable String sort){
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.eq("sort",sort);
        List<EduTeacher> list = eduTeacherService.list(wrapper);
        return R.ok().data("teacher",list);

    }

    //小程序 根据名称搜索讲师
    @PostMapping("searchTeacher")
    public R searchTeacher(@RequestParam String name){
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.like("name",name);
        List<EduTeacher> list = eduTeacherService.list(wrapper);
        return R.ok().data("list",list);
    }

}
