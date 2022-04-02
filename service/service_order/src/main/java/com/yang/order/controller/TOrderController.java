package com.yang.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.regexp.internal.RE;
import com.yang.commonutils.JwtUtils;
import com.yang.commonutils.R;
import com.yang.order.entity.TOrder;
import com.yang.order.service.TOrderService;
import jdk.nashorn.internal.objects.NativeRangeError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author YangYoung
 * @since 2022-04-01
 */
@RestController
@RequestMapping("/serviceorder/order")
@CrossOrigin
public class TOrderController {

    @Autowired
    private TOrderService tOrderService;


    //通过课程ID生成订单
    @GetMapping("createdOrder/{courseId}")
    public R createdOrder(@PathVariable String courseId,HttpServletRequest request){

        String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);
        String orderNo = tOrderService.createdOrder(courseId,memberIdByJwtToken);

        return R.ok().data("orderId",orderNo);
    }

    //根据订单号查询订单
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        QueryWrapper<TOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderId);
        TOrder order = tOrderService.getOne(wrapper);
        return R.ok().data("item",order);
    }


    //根据课程ID和用户ID查询订单支付状态
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,@PathVariable String memberId){
        QueryWrapper<TOrder> wrapper =new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.eq("status",1);
        int count = tOrderService.count(wrapper);
        if (count>0){
            return true;
        }else {
            return false;
        }
    }

}

