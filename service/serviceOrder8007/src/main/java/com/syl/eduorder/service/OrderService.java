package com.syl.eduorder.service;

import com.syl.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author sylup
 * @since 2022-04-18
 */
public interface OrderService extends IService<Order> {

    String createdOrders(String courseId, String userId);
}
