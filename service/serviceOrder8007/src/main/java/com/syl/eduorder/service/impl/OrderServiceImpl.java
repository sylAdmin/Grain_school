package com.syl.eduorder.service.impl;

import com.syl.commonutils.ordervo.CourseWebVoOrder;
import com.syl.commonutils.ordervo.UcenterMemberOrder;
import com.syl.eduorder.client.EduClient;
import com.syl.eduorder.client.UcenterClient;
import com.syl.eduorder.entity.Order;
import com.syl.eduorder.mapper.OrderMapper;
import com.syl.eduorder.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syl.eduorder.utils.OrderNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author sylup
 * @since 2022-04-18
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    /**
     * 创建订单，返回订单号
     * @param courseId  要购买的课程id
     * @param userId 购买用户的id
     * @return
     */
    @Override
    public String createdOrders(String courseId, String userId) {
        //通过远程调用根据用户id获取用户信息
        UcenterMemberOrder userInfoOrder = ucenterClient.getUserInfoOrder(userId);
        //通过远程调用根据课程id获取课程信息
        CourseWebVoOrder courseInfoOrder = eduClient.getCourseInfoOrder(courseId);

        Order order = new Order();
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfoOrder.getTitle());
        order.setCourseCover(courseInfoOrder.getCover());
        order.setTeacherName(courseInfoOrder.getTeacherName());
        order.setTotalFee(courseInfoOrder.getPrice());
        order.setMemberId(userId);
        order.setMobile(userInfoOrder.getMobile());
        order.setNickname(userInfoOrder.getNickname());
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setStatus(0); //订单状态 (0:未支付 1:已支付)
        order.setPayType(1); //支付类型 1表示微信支付
        baseMapper.insert(order);
        return order.getOrderNo();
    }
}
