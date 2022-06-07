package com.yang.serviceedu.client.impl;


import com.yang.serviceedu.client.UcenterClient;
import com.yang.serviceedu.entity.vo.MemberVo;
import org.springframework.stereotype.Component;

@Component
public class UcenterClientImpl implements UcenterClient {
    @Override
    public MemberVo getUcenterMemberById(String id) {
        return null;
    }

    @Override
    public MemberVo miniUserInfo(String id) {
        return null;
    }
}
