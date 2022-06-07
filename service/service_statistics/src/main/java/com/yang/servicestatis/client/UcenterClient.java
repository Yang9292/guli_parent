package com.yang.servicestatis.client;

import com.yang.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    //后台 查询一天中注册的人数
    @GetMapping("/ucenter/member/countRegister/{day}")
    public R countRegister(@PathVariable("day") String day);
}
