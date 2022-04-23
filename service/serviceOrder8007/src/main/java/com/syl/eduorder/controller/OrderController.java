package com.syl.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syl.commonutils.JwtUtils;
import com.syl.commonutils.R;
import com.syl.eduorder.entity.Order;
import com.syl.eduorder.service.OrderService;
import com.syl.servicebase.exception.GuLiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author sylup
 * @since 2022-04-18
 */
@RestController
@RequestMapping("/eduorder/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "生成订单")
    @GetMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request){
        String userId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(userId)){
            throw new GuLiException(20001,"请先进行登录，在进行操作");
        }
        String orderNo = orderService.createdOrders(courseId,userId);
        return R.ok().data("orderId",orderNo);
    }

    /**
     * 根据订单号查询订单信息
     */
    @ApiOperation("根据订单号查询订单信息")
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.eq("order_no",orderId);
        Order order = orderService.getOne(qw);
        return R.ok().data("item",order);
    }

    /**
     * 根据课程id和用户id查询订单表中订单状态
     */
    @ApiOperation("根据课程id和用户id查询订单表中订单状态")
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,
                         @PathVariable String memberId){
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.eq("course_id",courseId);
        qw.eq("member_id",memberId);
        qw.eq("status",1);
        int count = orderService.count(qw);
        if(count > 0){  //支付状态1已经支付
            return true;
        }
        return false;
    }

    /**
     * 根据用户id查询已经购买课程的id
     */
    @ApiOperation("根据用户id查询已经购买课程的id")
    @GetMapping("isBuyCourses/{memberId}")
    public List<String> isBuyCourses(@PathVariable String memberId){
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.eq("member_id",memberId);
        qw.eq("status",1);
        List<Order> list = orderService.list(qw);
        List<String> courseList = new ArrayList<>();
        for (Order order : list) {
            courseList.add(order.getCourseId());
        }
        return courseList;
    }
}

