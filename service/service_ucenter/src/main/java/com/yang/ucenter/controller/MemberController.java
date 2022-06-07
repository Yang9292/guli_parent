package com.yang.ucenter.controller;


import com.alibaba.nacos.common.util.Md5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yang.commonutils.JwtUtils;
import com.yang.commonutils.MD5;
import com.yang.commonutils.R;
import com.yang.commonutils.orderVo.UcenterMemberOrder;
import com.yang.servicebase.exceptionhandler.GuliException;
import com.yang.ucenter.entity.Member;
import com.yang.ucenter.entity.vo.MemberVo;
import com.yang.ucenter.entity.vo.RegisterVo;
import com.yang.ucenter.service.MemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    //小程序登录

    @PostMapping("miniLogin")
    public R miniLogin(@RequestParam String mobile,@RequestParam String password){
        QueryWrapper<Member> wrapper = new QueryWrapper();
        wrapper.eq("mobile",mobile);
        Member member = memberService.getOne(wrapper);
        if (StringUtils.isEmpty(member)){
            throw new GuliException(20001,"该手机未注册");
        }
        if (!member.getPassword().equals(MD5.encrypt(password))){
            throw new GuliException(20001,"密码错误");
        }
        return R.ok().data("member",member);

    }

    //小程序 根据用户ID查询用户信息

    @GetMapping("miniUserInfo/{id}")
    public MemberVo miniUserInfo(@RequestParam String id){
        Member byId = memberService.getById(id);
        MemberVo memberVo = new MemberVo();
        BeanUtils.copyProperties(byId,memberVo);
        return memberVo;
    }

    //小程序端  注册功能
    @GetMapping("miniRegister")
    public R miniRegister(@RequestParam String nickName,@RequestParam String mobile,@RequestParam String password){

        memberService.miniRegister(nickName,mobile,password);
        return R.ok().message("注册成功");

    }

    //修改用户信息
    @GetMapping("updataUserInfo")
    public R updataUserInfo(@RequestParam String userid,@RequestParam String name,
                            @RequestParam String passwordone,@RequestParam String passwordtwo,
                            @RequestParam String url){
        Member member = memberService.getById(userid);
        if (StringUtils.isEmpty(member)){
            throw new GuliException(20001,"账号信息为空");
        }
        String password1 = MD5.encrypt(passwordone);
        if (!StringUtils.isEmpty(password1)) {
            member.setPassword(password1);
        }
        if(StringUtils.isEmpty(password1)){
            member.setPassword(member.getPassword());
        }
        if (!StringUtils.isEmpty(name)){
            member.setNickname(name);
        }else {
            member.setNickname(member.getNickname());
        }
        if (!StringUtils.isEmpty(url)){
            member.setAvatar(url);
        }else {
            member.setAvatar(member.getAvatar());
        }
        memberService.saveOrUpdate(member);
        return R.ok();
    }


    //后台 查询一天中注册的人数
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
       Integer count =  memberService.countRegister(day);
       return R.ok().data("count",count);
    }

    //获取全部用户信息
    @GetMapping("getAllInfo")
    public R getAllInfo(){
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");
        List<Member> list = memberService.list(wrapper);
        return R.ok().data("list",list);
    }


}

