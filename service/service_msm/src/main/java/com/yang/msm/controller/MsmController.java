package com.yang.msm.controller;

import com.yang.commonutils.R;
import com.yang.msm.service.MsmService;
import com.yang.msm.utils.RandomUtil;
import com.yang.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@Api(description = "短信验证服务管理器")
@RequestMapping("/msmservice/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @ApiOperation("发送验证码")
    @GetMapping("/send/{phone}")
    public R sendMsm(@PathVariable String phone) throws Exception {
        //1.从redis中获取相应手机号的验证码值
        String token = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(token)) {
            //如果能够获取验证码值成功，则直接返回ok
            return R.ok();
        }
        //不能成功获取redis中的验证码值，在发送验证短信

        //生成一个随机验证码
        String code = RandomUtil.getSixBitRandom();
        System.out.println(code);

        boolean flag = msmService.sendMessage(phone,code);

        if (flag) {
            //如果发送成功，则存到redis中去，并设置过期时间(5分钟过期)
            redisTemplate.opsForValue().set(phone,code,5L, TimeUnit.MINUTES);
            return R.ok();
        }else {
            throw new GuliException(20001,"验证码发送失败！");
        }
    }

}
