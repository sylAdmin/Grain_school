package com.syl.staservice.controller;


import com.syl.commonutils.R;
import com.syl.staservice.service.StatisticsDailyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author sylup
 * @since 2022-04-19
 */
@RestController
@RequestMapping("/staservice/sta")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    @ApiOperation(value = "统计某一天注册人数，生成统计数据")
    @PostMapping("registerCount/{day}")
    public R registerCount(@PathVariable String day){
        statisticsDailyService.registerCount(day);
        return R.ok();
    }

    @ApiOperation(value = "图表显示返回两部分数据，日期json数组，数量json数组")
    @GetMapping("showDate/{begin}/{end}")
    public R showData(@PathVariable String begin,
                      @PathVariable String end){
        Map<String,Object> map = statisticsDailyService.getShowData(begin,end);
        return R.ok().data(map);
    }
}

