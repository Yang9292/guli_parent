package com.yang.order.service;

import com.yang.order.entity.TPayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author YangYoung
 * @since 2022-04-01
 */
public interface TPayLogService extends IService<TPayLog> {

    Map createNative(String orderNo);

    Map<String,String> queryPayStatus(String orderNo);

    void updateOrderStatus(Map<String,String> map);
}
