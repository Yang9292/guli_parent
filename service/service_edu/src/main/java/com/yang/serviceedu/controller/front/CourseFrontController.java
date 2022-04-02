package com.yang.serviceedu.controller.front;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.commonutils.JwtUtils;
import com.yang.commonutils.R;
import com.yang.commonutils.orderVo.CourseWebVoOrder;
import com.yang.serviceedu.client.OrdersClient;
import com.yang.serviceedu.entity.EduCourse;
import com.yang.serviceedu.entity.frontVo.CourseFrontVo;
import com.yang.serviceedu.entity.frontVo.CourseWebVo;
import com.yang.serviceedu.entity.vo.ChapterVo;
import com.yang.serviceedu.entity.vo.CourseInfoForm;
import com.yang.serviceedu.service.EduChapterService;
import com.yang.serviceedu.service.EduCourseService;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(description = "前台课程控制器")
@CrossOrigin
@RestController
@RequestMapping("/serviceedu/courseFront")
public class CourseFrontController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private OrdersClient ordersClient;


    //前台课程带条件分页查询
    @PostMapping("getCourseFrontList/{nowpage}/{limit}")
    public R getCourseFrontList(@PathVariable Integer nowpage,
                                @PathVariable Integer limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo){

        Page<EduCourse> page = new Page<>(nowpage,limit);

        Map<String,Object> map = eduCourseService.getCourseFrontList(page,courseFrontVo);

        return R.ok().data("map",map);
    }


    //前台 根据课程ID返回课程基本信息和大纲小节信息、教师信息
    @GetMapping("getFrontCourseInfo/{courseId}")

    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request){

        //根据课程ID查询课程基本信息
        CourseWebVo courseWebVo = eduCourseService.getFrontCourseInfo(courseId);

        //根据课程ID查询章节小节
        List<ChapterVo> chapterVideo = eduChapterService.getChapterVideo(courseId);

        //根据课程id和用户id 查询当前课程是否已经购买过
        boolean isBuy = ordersClient.isBuyCourse(courseId, JwtUtils.getMemberIdByJwtToken(request));


        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideo",chapterVideo).data("isBuy",isBuy);

    }

    //根据课程id查询课程信息 用于order远程调用
    @GetMapping("getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id){
        CourseWebVo courseInfo = eduCourseService.getFrontCourseInfo(id);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfo,courseWebVoOrder);
        return courseWebVoOrder;
    }
}
