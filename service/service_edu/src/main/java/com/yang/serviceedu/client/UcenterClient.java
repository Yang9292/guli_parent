package com.yang.serviceedu.client;

import com.yang.serviceedu.client.impl.UcenterClientImpl;
import com.yang.serviceedu.client.impl.VodClientImpl;
import com.yang.serviceedu.entity.vo.MemberVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Component  //将这个接口交给spring管理
@FeignClient(value = "service-ucenter",fallback = UcenterClientImpl.class)
public interface UcenterClient {

    @GetMapping("/ucenter/member/getUcenterMemberById/{id}")
    public MemberVo getUcenterMemberById(@PathVariable("id") String id);

    //小程序 根据用户ID获取用户信息
    @GetMapping("/ucenter/member/miniUserInfo/{id}")
    public MemberVo miniUserInfo(@RequestParam("id") String id);
}
