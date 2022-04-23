package com.syl.eduorder.controller;


import com.syl.commonutils.R;
import com.syl.eduorder.service.PayLogService;
import com.syl.servicebase.exception.GuLiException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author sylup
 * @since 2022-04-18
 */
@RestController
@RequestMapping("/eduorder/paylog")
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    /**
     * 通过订单号：生成微信支付二维码
     */
    @ApiOperation(value = "生成微信支付二维码")
    @GetMapping("/createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        Map map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

    @ApiOperation(value = "查询订单支付状态")
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        if(map == null){
            throw new GuLiException(20001,"支付出错了,请重试");
        }
        //如果map里面不为空，通过map获取订单状态
        if(map.get("trade_state").equals("SUCCESS")){
            //添加记录到支付表，更新订单表订单状态
            payLogService.updateOrdersStatus(map);
            return R.ok();
        }
        return R.ok().code(25000).message("支付中......请稍后.....").data("code",25000);
    }

}

