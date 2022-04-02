package com.yang.serviceedu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.servicebase.exceptionhandler.GuliException;
import com.yang.serviceedu.client.VodClient;
import com.yang.serviceedu.entity.EduVideo;
import com.yang.serviceedu.mapper.EduVideoMapper;
import com.yang.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-22
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;


    @Override
    public void removeVideoById(String courseId) {

        //根据课程ID查询所有视频ID
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.select("video_source_id");
        List<EduVideo> eduVideos = baseMapper.selectList(queryWrapper);

        List<String> videosId = new ArrayList<>();
        for (int i = 0;i<eduVideos.size();i++){
            EduVideo eduVideo = eduVideos.get(i);
            String videoSourceId = eduVideo.getVideoSourceId();
            if (!StringUtils.isEmpty(videoSourceId)){
                videosId.add(videoSourceId);
            }else {
                throw new GuliException(20001,"videoSourceId为空");
            }
        }
        if (videosId.size() > 0){
            //根据多个视频ID删除多个视频
            vodClient.deleteBatchVideo(videosId);
        }else {
            throw new GuliException(20001,"videosId为空");
        }

        //删除小节
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
