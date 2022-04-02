package com.yang.serviceedu.controller;


import com.yang.commonutils.R;
import com.yang.serviceedu.client.VodClient;
import com.yang.serviceedu.entity.EduVideo;
import com.yang.serviceedu.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-22
 */
@RestController
@RequestMapping("/serviceedu/video")
@CrossOrigin
public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private VodClient vodClient;

    //添加小节
    @PostMapping("addEduVideo")
    public R addEduVideo(@RequestBody EduVideo eduVideo){

        eduVideoService.save(eduVideo);

        return R.ok();

    }

    //删除小节
    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable String videoId){

        //通过ID获取eduVideo对象
        EduVideo eduVideo = eduVideoService.getById(videoId);
        //根据eduVideo对象获取视频ID
        String videoSourceId = eduVideo.getVideoSourceId();
        //调用方法实现远程调用
        if (!StringUtils.isEmpty(videoSourceId)){
            vodClient.deleteVideoById(videoSourceId);
        }
        eduVideoService.removeById(videoId);
        return R.ok();
    }

    //根据id获取小节
    @GetMapping("getVideo/{videoId}")
    public R getVideoById(@PathVariable String videoId){

        EduVideo byId = eduVideoService.getById(videoId);

        return R.ok().data("eduVideo",byId);

    }

    //修改小节
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){

        eduVideoService.updateById(eduVideo);

        return R.ok();
    }

}

