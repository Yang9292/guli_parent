package com.yang.serviceedu.controller;


import com.yang.commonutils.R;
import com.yang.serviceedu.entity.EduChapter;
import com.yang.serviceedu.entity.vo.ChapterVo;
import com.yang.serviceedu.service.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/serviceedu/chapter")
@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService eduChapterService;

    //根据课程id,获取所有章节数据
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){

        List<ChapterVo> list = eduChapterService.getChapterVideo(courseId);

        return R.ok().data("allChapterVideo",list);
    }
    //添加章节数据
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        eduChapterService.save(eduChapter);
        return R.ok();
    }

    //根据章节id查询章节信息
    @GetMapping("getChapterInfo/{chapterId}")

    public R getChapterInfo(@PathVariable String chapterId ){

        EduChapter byId = eduChapterService.getById(chapterId);

        return R.ok().data("chapter",byId);

    }

    //修改章节信息
    @PostMapping("uploadChapter")
    public R uploadChapter(@RequestBody EduChapter eduChapter){

        eduChapterService.updateById(eduChapter);
        return R.ok();

    }

    //删除章节信息
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){

        boolean flag = eduChapterService.deleteChapterById(chapterId);
        if (flag){
            return R.ok();
        }else {
            return R.error();
        }

    }

}

