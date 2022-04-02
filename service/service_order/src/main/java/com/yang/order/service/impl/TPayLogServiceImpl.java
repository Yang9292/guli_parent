package com.yang.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.yang.order.entity.TOrder;
import com.yang.order.entity.TPayLog;
import com.yang.order.mapper.TPayLogMapper;
import com.yang.order.service.TOrderService;
import com.yang.order.service.TPayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.order.utils.HttpClient;
import com.yang.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author YangYoung
 * @since 2022-04-01
 */
@Service
public class TPayLogServiceImpl extends ServiceImpl<TPayLogMapper, TPayLog> implements TPayLogService {

    @Autowired
    private TOrderService orderService;

    //生成微信支付二维码接口
    @Override
    public Map createNative(String orderNo) {

        try {
            //根据订单id获取订单信息
            QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            TOrder order = orderService.getOne(wrapper);
            // System.out.println("根据订单id获取订单信息-------------"+order);

            Map m = new HashMap();
            //1、设置支付参数
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("nonce_str", WXPayUtil.generateNonceStr()); //回调地址
            m.put("body", order.getCourseTitle());
            m.put("out_trade_no", orderNo); //订单号
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url","http://guli.shop/api/order/weixinPay/weixinNotify\n"); //回调地址
            m.put("trade_type", "NATIVE");

            //2、HTTPClient来根据URL访问第三方接口并且传递参数             固定地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //client设置参数                                          商户key 用来加密订单信息
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //System.out.println("第三方的数据resultMap---------------------"+resultMap);
            //4、封装返回结果集
            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));  //状态码
            map.put("code_url", resultMap.get("code_url"));      //二维码地址

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120,TimeUnit.MINUTES);

            return map;

        }catch (Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"生成二维码失败");
        }

    }

    //根据订单号查询订单状态
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {

        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");
            m.put("mch_id", "1558950191");
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            //6、转成Map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 添加记录到支付表  更新订单表支付状态
    @Override
    public void updateOrderStatus(Map<String, String> map) {

        //获取订单id
        String orderNo = map.get("out_trade_no");
        //根据订单id查询订单信息
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        TOrder order = orderService.getOne(wrapper);
        //判断是否已经支付
        if(order.getStatus().intValue() == 1) {return;}
        order.setStatus(1); // 1 代表已支付
        orderService.updateById(order); // 更新订单表支付状态

        // 添加记录到支付表
        TPayLog payLog=new TPayLog();

        payLog.setOrderNo(order.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);   //支付类型  默认微信 1
        payLog.setTotalFee(order.getTotalFee());   //总金额(分)
        payLog.setTradeState(map.get("trade_state"));   //支付状态
        payLog.setTransactionId(map.get("transaction_id"));  //流水号
        payLog.setAttr(JSONObject.toJSONString(map));
        //插入到支付日志表
        baseMapper.insert(payLog);

    }


}
