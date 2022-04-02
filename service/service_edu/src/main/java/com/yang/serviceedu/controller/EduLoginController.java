package com.yang.serviceedu.controller;

import com.yang.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/serviceedu/user")
@CrossOrigin    //解决跨域问题
public class EduLoginController {

    //登录
    @PostMapping("login")
    public R login(){
        return R.ok().data("token","admin");
    }

    //获取信息
    @GetMapping("info")
    public R info() {
        return R.ok().data("roles", "[admin]").data("name", "admin").data("avatar", "https://edu-9292.oss-cn-beijing.aliyuncs.com/2022/03/21/u%3D178892670%2C2966992691%26fm%3D253%26fmt%3Dauto%26app%3D138%26f%3DJPEG.webp");
    }
}
