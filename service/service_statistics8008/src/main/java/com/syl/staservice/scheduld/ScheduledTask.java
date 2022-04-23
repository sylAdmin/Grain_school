package com.syl.staservice.scheduld;

import com.syl.staservice.service.StatisticsDailyService;
import com.syl.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务(在特定时间执行相应方法)
 */
@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    //cron = "0/5 * * * * ?"  每隔5秒中执行一次这个方法
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void task1(){
//        System.out.println("-**************task1执行了...-");
//    }

    //在每天凌晨1点，把前一天数据进行数据查询添加
    @Scheduled(cron = "0 0 1 * * ?")
    public void TimingQuery(){
        //通过工具类得到前一天日期
        statisticsDailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));
    }
}
