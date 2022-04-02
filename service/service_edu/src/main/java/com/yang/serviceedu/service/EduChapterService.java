package com.yang.serviceedu.service;

import com.yang.serviceedu.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.serviceedu.entity.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-22
 */
public interface EduChapterService extends IService<EduChapter> {

    //根据课程id,获取所有章节数据
    List<ChapterVo> getChapterVideo(String courseId);
    //删除章节信息
    boolean deleteChapterById(String chapterId);
}
