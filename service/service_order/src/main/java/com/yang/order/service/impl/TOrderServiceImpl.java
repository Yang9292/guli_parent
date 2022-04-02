package com.yang.order.service.impl;

import com.yang.commonutils.orderVo.CourseWebVoOrder;
import com.yang.commonutils.orderVo.UcenterMemberOrder;
import com.yang.order.client.CourseClient;
import com.yang.order.client.UcenterClient;
import com.yang.order.entity.TOrder;
import com.yang.order.mapper.TOrderMapper;
import com.yang.order.service.TOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.order.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author YangYoung
 * @since 2022-04-01
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private UcenterClient ucenterClient;

    //通过课程ID生成订单
    @Override
    public String createdOrder(String courseId, String memberIdByJwtToken) {

        //远程调用 根据课程ID获取课程信息
        CourseWebVoOrder courseInfoOrder = courseClient.getCourseInfoOrder(courseId);
        //远程调用 根据用户ID获取用户信息
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(memberIdByJwtToken);

        TOrder order = new TOrder();

        order.setOrderNo(OrderNoUtil.getOrderNo()); //设置订单号

        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName(courseInfoOrder.getTeacherName());
        order.setTotalFee(courseInfoOrder.getPrice());

        order.setMemberId(memberIdByJwtToken);
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());
        order.setStatus(0);  //支付状态 0 未支付 1 已支付
        order.setPayType(1); //支付方式  1表示微信
        baseMapper.insert(order);

        //返回订单号
        return order.getOrderNo();

    }
}
