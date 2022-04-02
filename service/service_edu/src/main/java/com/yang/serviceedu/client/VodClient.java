package com.yang.serviceedu.client;

import com.yang.commonutils.R;
import com.yang.serviceedu.client.impl.VodClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component  //将这个接口交给spring管理
@FeignClient(value = "service-vod",fallback = VodClientImpl.class)
public interface VodClient {

    //下面就是要从远程服务模块中具体调用的方法，需要和原方法写的一样
    @DeleteMapping("/servicevod/video/deleteVideoById/{videoId}")
    public R deleteVideoById(@PathVariable("videoId") String videoId);

    @DeleteMapping("/servicevod/video/delete-batch")
    public R deleteBatchVideo(@RequestParam("videoIdList") List<String> videoIdList);
}
