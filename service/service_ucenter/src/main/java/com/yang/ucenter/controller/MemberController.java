package com.yang.ucenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.commonutils.JwtUtils;
import com.yang.commonutils.R;
import com.yang.commonutils.orderVo.UcenterMemberOrder;
import com.yang.ucenter.entity.Member;
import com.yang.ucenter.entity.vo.MemberVo;
import com.yang.ucenter.entity.vo.RegisterVo;
import com.yang.ucenter.service.MemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author YangYoung
 * @since 2022-03-28
 */
@RestController
@RequestMapping("/ucenter/member")
@CrossOrigin
public class MemberController {

    @Autowired
    private MemberService memberService;

    //登录
    //实现单点登录，要返回一个token值
    @PostMapping("login")
    public R login(@RequestBody Member member){

        String token = memberService.login(member);
        return R.ok().data("token",token);
    }

    //注册
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok().message("注册成功");
    }

    //根据token获取用户信息
    @GetMapping("getUserInfo")
    public R getUserInfo(HttpServletRequest request){

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        Member member = memberService.getById(memberId);

        return R.ok().data("userInfo",member);
    }

    //根据用户ID获取用户信息
    @GetMapping("getUcenterMemberById/{id}")
    public MemberVo getUcenterMemberById(@PathVariable String id){
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);

        Member memberById = memberService.getOne(wrapper);

        MemberVo memberVo = new MemberVo();
        BeanUtils.copyProperties(memberById,memberVo);

        return memberVo;
    }

    //根据用户Id获取用户信息，保存至订单表
    @GetMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id){

        Member member = memberService.getById(id);
        UcenterMemberOrder ucenterMemberOrder=new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return  ucenterMemberOrder;
    }


}

