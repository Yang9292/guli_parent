package com.yang.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.servicebase.exceptionhandler.GuliException;
import com.yang.serviceedu.entity.EduChapter;
import com.yang.serviceedu.entity.EduVideo;
import com.yang.serviceedu.entity.vo.ChapterVo;
import com.yang.serviceedu.entity.vo.VideoVo;
import com.yang.serviceedu.mapper.EduChapterMapper;
import com.yang.serviceedu.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.serviceedu.service.EduVideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-22
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    //根据课程id,获取所有章节数据
    @Override
    public List<ChapterVo> getChapterVideo(String courseId) {

        //根据课程id查询出章节信息
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        List<EduChapter> eduChapters = baseMapper.selectList(wrapper);

        //根据课程id出小节信息
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        List<EduVideo> eduVideos = eduVideoService.list(queryWrapper);

        //创建最终集合对数据进行封装
        List<ChapterVo> finalList = new ArrayList<>();
        //对章节信息进行封装
        for(EduChapter eduChapter:eduChapters){

            ChapterVo chapterVo = new ChapterVo();

            BeanUtils.copyProperties(eduChapter,chapterVo);

            //对小节信息进行封装
            for (EduVideo eduVideo : eduVideos){
                if (eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo = new VideoVo();

                    BeanUtils.copyProperties(eduVideo,videoVo);

                    chapterVo.getChildren().add(videoVo);
                }
            }

            //将结果加入到最终集合中并返回

            finalList.add(chapterVo);

        }

        return finalList;
    }

    @Override
    public boolean deleteChapterById(String chapterId) {
        //首先根据传过来的参数查询小节表
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = eduVideoService.count(wrapper);

        if (count>0){//证明章节目录下存在小节，不能删除
            throw new GuliException(20001,"章节下存在小节，不能删除...");
        }else {
            int i = baseMapper.deleteById(chapterId);
            return i>0;
        }
    }
}
