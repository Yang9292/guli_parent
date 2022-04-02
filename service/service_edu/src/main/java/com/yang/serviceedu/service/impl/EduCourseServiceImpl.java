package com.yang.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.servicebase.exceptionhandler.GuliException;
import com.yang.serviceedu.entity.EduCourse;
import com.yang.serviceedu.entity.EduCourseDescription;
import com.yang.serviceedu.entity.frontVo.CourseFrontVo;
import com.yang.serviceedu.entity.frontVo.CourseWebVo;
import com.yang.serviceedu.entity.vo.CourseInfoForm;
import com.yang.serviceedu.entity.vo.CoursePublishVo;
import com.yang.serviceedu.mapper.EduCourseMapper;
import com.yang.serviceedu.service.EduChapterService;
import com.yang.serviceedu.service.EduCourseDescriptionService;
import com.yang.serviceedu.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.serviceedu.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-22
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;
    //课程小节service类注入
    @Autowired
    private EduVideoService eduVideoService;
    //课程章节service类注入
    @Autowired
    private EduChapterService eduChapterService;

    @Override
    public String addCourseInfo(CourseInfoForm courseInfoForm) {

        //向edu_course表中添加信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm,eduCourse);
        int insert = baseMapper.insert(eduCourse);

        if (insert <= 0){
            throw new GuliException(20001,"课程信息添加失败...");
        }

        //向edu_course_description表中添加信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        String id = eduCourse.getId();
        eduCourseDescription.setId(id);
        eduCourseDescription.setDescription(courseInfoForm.getDescription());
        eduCourseDescriptionService.save(eduCourseDescription);

        return id;
    }

    //根据课程id查询课程信息
    @Override
    public CourseInfoForm getCourseInfo(String courseId) {

        //创建最终对象将数据进行封装
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        //查询course表中的数据
        EduCourse eduCourse = baseMapper.selectById(courseId);
        //对course表中的数据进行封装
        if (eduCourse != null) {
            BeanUtils.copyProperties(eduCourse, courseInfoForm);
        }

        //查询简介表中的数据
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(courseId);
        //对数据进行封装
        courseInfoForm.setDescription(eduCourseDescription.getDescription());

        return courseInfoForm;
    }

    //修改课程信息
    @Override
    public void uploadCourseInfo(CourseInfoForm courseInfoForm) {

        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm,eduCourse);
        int update = baseMapper.updateById(eduCourse);

        if (update <= 0){
            throw new GuliException(20001,"修改课程信息失败....");
        }
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoForm,eduCourseDescription);
        eduCourseDescriptionService.updateById(eduCourseDescription);

    }
    //根据id最终提交表单信息
    @Override
    public CoursePublishVo getPublishCourse(String courseId) {
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(courseId);
        if (publishCourseInfo != null) {
            return publishCourseInfo;
        }else {
            throw new GuliException(20001,"查不到最终表单数据...");
        }
    }

    @Override
    public boolean deleteCourseById(String courseId) {
        //1.根据课程id，删除课程里面的小节
        eduVideoService.removeVideoById(courseId);
        //2.根据课程id，删除课程里面的章节
        eduChapterService.deleteChapterById(courseId);
        //3.根据课程id，删除课程描述
        eduCourseDescriptionService.removeById(courseId);
        //4.根据课程id，删除课程
        boolean b = this.removeById(courseId);
        if (b) {
            return true;
        }else {
            throw new GuliException(20001,"删除课程失败！");
        }
    }

    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> page, CourseFrontVo courseFrontVo) {

        //查询条件初始值
        String title = null;
        String subjectId = null;
        String subjectParentId = null;
        String gmtCreateSort = null;
        String buyCountSort = null;
        String priceSort = null;
        String teacherId = null;

        //如果不为空，依次取值并赋值
        if (!StringUtils.isEmpty(courseFrontVo)){
            title = courseFrontVo.getTitle();
            subjectId = courseFrontVo.getSubjectId();
            subjectParentId = courseFrontVo.getSubjectParentId();
            gmtCreateSort = courseFrontVo.getGmtCreateSort();
            buyCountSort = courseFrontVo.getBuyCountSort();
            priceSort = courseFrontVo.getPriceSort();
            teacherId = courseFrontVo.getTeacherId();
        }
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断条件值是否为空，不为空拼接条件
        if (!StringUtils.isEmpty(subjectParentId)){//一级分类
            wrapper.eq("subject_parent_id",subjectParentId);
        }
        if (!StringUtils.isEmpty(subjectId)){//二级分类
            wrapper.eq("subject_id",subjectId);
        }
        if (!StringUtils.isEmpty(buyCountSort)){//关注度
            wrapper.orderByDesc("buy_count");
        }
        if (!StringUtils.isEmpty(priceSort)){//价格
            wrapper.orderByDesc("price");
        }
        if (!StringUtils.isEmpty(gmtCreateSort)){//最新，创建时间
            wrapper.orderByDesc("gmt_create");
        }

        //查询
        baseMapper.selectMapsPage(page,wrapper);
        long total = page.getTotal();//总记录数
        List<EduCourse> courseList = page.getRecords();//数据集合
        long size = page.getSize();//每页记录数
        long current = page.getCurrent();//当前页
        long pages = page.getPages();//总页数
        boolean hasPrevious = page.hasPrevious();//是否有上一页
        boolean hasNext = page.hasNext();//是否有下一页

        HashMap<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("list",courseList);
        map.put("size",size);
        map.put("current",current);
        map.put("pages",pages);
        map.put("hasPrevious",hasPrevious);
        map.put("hasNext",hasNext);
        return map;
    }

    //根据课程ID查询课程基本信息
    @Override
    public CourseWebVo getFrontCourseInfo(String courseId) {

        CourseWebVo courseWebVo = baseMapper.getFrontCourseInfo(courseId);
        return courseWebVo;
    }

}
