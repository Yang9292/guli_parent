package com.yang.codeDemo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yang.commonutils.MD5;
import com.yang.ucenter.entity.Member;
import com.yang.ucenter.service.MemberService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class test {

    @Test
    public void test(){
        String psw = "123456";
        System.out.println(MD5.encrypt(psw));
    }
}
