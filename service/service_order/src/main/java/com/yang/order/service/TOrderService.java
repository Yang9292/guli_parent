package com.yang.order.service;

import com.yang.order.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author YangYoung
 * @since 2022-04-01
 */
public interface TOrderService extends IService<TOrder> {

    //通过课程ID生成订单
    String createdOrder(String courseId, String memberIdByJwtToken);
}
