package com.yang.serviceedu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.commonutils.R;
import com.yang.servicebase.exceptionhandler.GuliException;
import com.yang.serviceedu.entity.EduTeacher;
import com.yang.serviceedu.entity.vo.TeachQuery;
import com.yang.serviceedu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-18
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/serviceedu/teacher")
@CrossOrigin
public class EduTeacherController {

    //注入service
    @Autowired
    private EduTeacherService eduTeacherService;

    //查询全部讲师信息
    @ApiOperation("查询所有讲师")
    @GetMapping("findAll")     //rest风格，GET表示查询
    public R findAll(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

    //删除讲师信息
    @ApiOperation("逻辑删除讲师")
    @DeleteMapping("{id}")
    public R deleteTeacherById(
            @ApiParam(name = "id",value = "讲师ID",required = true)
            @PathVariable String id){

        boolean b = eduTeacherService.removeById(id);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //分页查询讲师信息
    @ApiOperation("分页查询讲师信息")
    @GetMapping("pageTeacher/{nowpage}/{limit}")
    public R pageTeacher(
            @ApiParam(name = "nowpage",value = "当前页",required = true)
            @PathVariable Integer nowpage,
            @ApiParam(name = "limit",value = "每页显示的数量",required = true)
            @PathVariable Integer limit){
        //创建对象
        Page<EduTeacher> page = new Page<>(nowpage,limit);
        //调用方法实现分页
        //调用方法时，底层把分页所得到的数据封装到page对象里面
        eduTeacherService.page(page,null);
        //获取信息的总行数
        long total = page.getTotal();
        //获取分页后讲师信息
        List<EduTeacher> records = page.getRecords();

        return R.ok().data("totals",total).data("rows",records);

    }

    //带条件查询并分页
    @ApiOperation("多条件分页查询")
    @PostMapping("pageTeacherCondition/{nowpage}/{limit}")
    public R pageTeacherCondition(
            @PathVariable Integer nowpage,
            @PathVariable Integer limit,
            @RequestBody(required = false) TeachQuery teachQuery){

        //创建page对象
        Page<EduTeacher> page = new Page<>(nowpage,limit);
        //构建条件
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        //多条件组合查询
        //mybatis中动态sql类似
        String name = teachQuery.getName();
        Integer level = teachQuery.getLevel();
        String begin = teachQuery.getBegin();
        String end = teachQuery.getEnd();
        //如果name不为空则进行拼接
        if (!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        if (!StringUtils.isEmpty(level)){
            queryWrapper.eq("level",level);
        }
        if (!StringUtils.isEmpty(begin)){
            queryWrapper.gt("gmt_create",begin);
        }
        if (!StringUtils.isEmpty(end)){
            queryWrapper.like("gmt_create",end);
        }
        //排序
        queryWrapper.orderByDesc("gmt_create");
        //调用方法进行分页查询
        eduTeacherService.page(page,queryWrapper);
        //获得数据总行数
        long total = page.getTotal();
        //获取条件查询后的分页信息
        List<EduTeacher> records = page.getRecords();
        return R.ok().data("totals",total).data("rows",records);
    }

    //添加讲师
    @ApiOperation("添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){

        boolean save = eduTeacherService.save(eduTeacher);

        if (save){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //根据id查询讲师
    @ApiOperation("根据ID查询讲师")
    @GetMapping("selTeaById/{id}")
    public R selectTeacherById(@PathVariable Long id){
        EduTeacher teacher = eduTeacherService.getById(id);
        return R.ok().data("teacher",teacher);
    }

    //修改讲师信息
    @ApiOperation("修改讲师信息")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean b = eduTeacherService.updateById(eduTeacher);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }


}

