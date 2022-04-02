package com.yang.msm.service.impl;

import com.alibaba.fastjson.JSON;
import com.yang.msm.service.MsmService;
import com.yang.servicebase.exceptionhandler.GuliException;
import com.zhenzi.sms.ZhenziSmsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MsmServiceImpl implements MsmService {

    //从属性文件中获取相应值
    @Value("${apiUrl}")
    String apiUrl;
    @Value("${appId}")
    String appId;
    @Value("${appSecret}")
    String appSecret;
    @Value("${templateId}")
    String templateId;

    //整合榛子云短信，发送验证码服务
    @Override
    public boolean sendMessage(String phone, String code){
        if (StringUtils.isEmpty(phone)) {
            //号码为空，直接返回false
            throw new GuliException(20001,"接收者号码为空！");
        }
        if (StringUtils.isEmpty(code)) {
            //号码为空，直接返回false
            throw new GuliException(20002,"验证码为空！");
        }

        //创建一个榛子云客户端
        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);

        //封装需要最终发送的数据
        Map<String, Object> params = new HashMap<>();
        //接收者号码
        params.put("number", phone);
        //短信模板id
        params.put("templateId", templateId);
        //短信模板参数
        String[] templateParams = new String[2];
        templateParams[0] = code;
        templateParams[1] = "5分钟";
        params.put("templateParams", templateParams);
        //设置消息id
        String messageId = UUID.randomUUID().toString().substring(0,10);
        params.put("messageId", messageId);
        //发送消息
        try {
            String result = client.send(params);
            System.out.println("发送结果："+result);

            String result2 = client.balance();
            System.out.println("当前剩余的短信条数："+result2);

            String result3 = client.findSmsByMessageId(messageId);
            System.out.println("查询已经发送的短信：" + result3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
