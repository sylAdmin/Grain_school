package com.syl.staservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syl.staservice.client.UcenterClient;
import com.syl.staservice.entity.StatisticsDaily;
import com.syl.staservice.mapper.StatisticsDailyMapper;
import com.syl.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author sylup
 * @since 2022-04-19
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void registerCount(String day) {
        //添加记录前删除表中相同日期内容
        QueryWrapper<StatisticsDaily> qw = new QueryWrapper<>();
        qw.eq("date_calculated",day);
        baseMapper.delete(qw);

        //远程调用得到某一天注册人数
        Integer countRegister= (Integer) ucenterClient.countRegister(day).getData().get("countRegister");
        //把获取数据添加到数据库，统计分析表里
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegister); //注册人数
        sta.setDateCalculated(day); //统计日期

        sta.setVideoViewNum(RandomUtils.nextInt(100,200));
        sta.setLoginNum(RandomUtils.nextInt(100,200));
        sta.setCourseNum(RandomUtils.nextInt(100,200));
        baseMapper.insert(sta);
    }

    @Override
    public Map<String, Object> getShowData(String begin, String end) {
        //根据条件查询对应的数据
        QueryWrapper<StatisticsDaily> qw = new QueryWrapper<>();
        qw.between("date_calculated",begin,end);
        qw.select("date_calculated","register_num","login_num","video_view_num","course_num"); //查询这两个列的信息
        List<StatisticsDaily> staList = baseMapper.selectList(qw);
        //前端要求数组json结构，对应后端java代码是list集合
        //创建两个list集合，一个日期list，一个数量list
        ArrayList<String> date_calculatedList = new ArrayList<>();
        ArrayList<Integer> registerList = new ArrayList<>();
        ArrayList<Integer> loginList = new ArrayList<>();
        ArrayList<Integer> videoViewList = new ArrayList<>();
        ArrayList<Integer> courseList = new ArrayList<>();
        for (StatisticsDaily daily : staList) {
            date_calculatedList.add(daily.getDateCalculated()); //封装日期
            registerList.add(daily.getRegisterNum());
            loginList.add(daily.getLoginNum());
            videoViewList.add(daily.getVideoViewNum());
            courseList.add(daily.getCourseNum());
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("date_calculatedList",date_calculatedList);
        map.put("registerList",registerList);
        map.put("loginList",loginList);
        map.put("videoViewList",videoViewList);
        map.put("courseList",courseList);
        return map;
    }
}
