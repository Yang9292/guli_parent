package com.yang.ucenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.yang.commonutils.JwtUtils;
import com.yang.ucenter.entity.Member;
import com.yang.ucenter.service.MemberService;
import com.yang.ucenter.utils.ConstantPropertiesUtil;
import com.yang.ucenter.utils.HttpClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.net.URLEncoder;

@Api(description = "微信二维码登录")
@CrossOrigin
@Controller
@RequestMapping("/api/ucenter1/wx")
public class WxApiController {

    @Autowired
    private MemberService memberService;

    @ApiOperation("微信二维码回调地址")
    @GetMapping("/callback")
    public String callback(String code,String state){
        //访问微信固定地址：会返回一个token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token"+
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        //拼接url地址
        baseAccessTokenUrl = String.format(baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_APP_ID,
                ConstantPropertiesUtil.WX_APP_SECRET,
                code);

        //使用httpclient【不用浏览器，也能模拟器出浏览器的请求和响应过程】发送请求，得到返回的结果
        //请求上面拼接好的地址，会得到一个json字符串（即，格式为json格式，但数据类型为String，但是可以很好的转为json对象），
        // 需要转换为json对象才可以使用
        String accessTokenInfo = null;
        try {
            accessTokenInfo = HttpClientUtils.get(baseAccessTokenUrl);
            //打印一下
            System.out.println("accessTokenInfo：" + accessTokenInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //从 accessTokenInfo 中获取出  access_token 和 openid 的值
        //将 accessTokenInfo 转换成 map集合，根据map的key 就可以获取对应的value
        //使用json转换工具
        Gson gson = new Gson();
        //将json字符串转换为一个map集合，当然你也可以转换为其他类型对象
        HashMap map = gson.fromJson(accessTokenInfo, HashMap.class);
        String access_token = (String) map.get("access_token");
        String openid = (String) map.get("openid");

        //判断数据库是否存在相同的微信内容
        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        Member member = memberService.getOne(wrapper);
        if (member == null) {
            //如果为null，表示为新用户
            //拿着 access_token 和 openid 的值再去请求微信提供的固定地址，访问微信的资源服务器，获取用户个人信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);

            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println(resultUserInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //将json字符串，转换为对象使用
            HashMap<String,Object> hashMap = gson.fromJson(resultUserInfo, HashMap.class);
            String nickname = (String) hashMap.get("nickname");
            String headimgurl = (String) hashMap.get("headimgurl");

            //将从微信固定地址获取的用户个人信息，存储到本地数据库里面
            member = new Member();
            member.setNickname(nickname);
            member.setAvatar(headimgurl);
            member.setOpenid(openid);
            memberService.save(member);
        }

        //使用jwt根据member对象生成token字符串
        String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
        //返回路径带上jwtToken令牌
        System.out.println("*************"+jwtToken);
        return "redirect:http://localhost:3000?token=" + jwtToken;
    }

    @ApiOperation("获取微信二维码登录")
    @GetMapping("/login")
    public String getWxCode(){
        //%s 相当于?占位符
        //这是固定地址：
        String url = "https://open.weixin.qq.com/connect/qrconnect?"
                + "appid=%s"
                + "&redirect_uri=%s"
                + "&response_type=code"
                + "&scope=snsapi_login"
                + "&state=%s"
                + "#wechat_redirect";

        //对redirect_uri进行URLEncoder编码
        String redirect_uri = ConstantPropertiesUtil.WX_REDIRECT_URL;
        try {
            //参数1：待编码字符串 参数2：编码方式
            redirect_uri = URLEncoder.encode(redirect_uri, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //利用String的format方法，将占位符的数据填充进去
        url = String.format(url, ConstantPropertiesUtil.WX_APP_ID, redirect_uri, "Yang");

        //请求重定向
        return "redirect:" + url;
    }
}