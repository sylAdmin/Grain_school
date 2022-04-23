package com.syl.eduservice.client;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("service-order")
public interface OrderClient {

    @ApiOperation("根据课程id和用户id查询订单表中订单状态")
    @GetMapping("/eduorder/order/isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable("courseId") String courseId,
                               @PathVariable("memberId") String memberId);

    @ApiOperation("根据用户id查询已经购买课程的id")
    @GetMapping("/eduorder/order/isBuyCourses/{memberId}")
    public List<String> isBuyCourses(@PathVariable("memberId") String memberId);
}
