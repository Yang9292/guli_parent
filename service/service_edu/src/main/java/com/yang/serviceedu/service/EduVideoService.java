package com.yang.serviceedu.service;

import com.yang.serviceedu.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-22
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeVideoById(String courseId);
}
