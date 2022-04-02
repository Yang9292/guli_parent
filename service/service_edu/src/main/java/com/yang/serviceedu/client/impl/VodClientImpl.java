package com.yang.serviceedu.client.impl;

import com.yang.commonutils.R;
import com.yang.serviceedu.client.VodClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 熔断机制处理类：
 */
@Component
public class VodClientImpl implements VodClient {

    /**
     *   若远程成功调用方法，则不执行；出错之后就会执行的兜底方法
     */
    @Override
    public R deleteVideoById(String videoId) {
        return R.error().message("熔断：根据小节id，删除阿里云视频失败！");
    }

    @Override
    public R deleteBatchVideo(List<String> videoIdList) {
        return R.error().message("熔断：根据小节id，批量阿里云删除视频失败！");
    }
}
