package com.syl.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syl.commonutils.R;
import com.syl.eduservice.client.VodClient;
import com.syl.eduservice.entity.EduVideo;
import com.syl.eduservice.service.EduVideoService;
import com.syl.servicebase.exception.GuLiException;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.print.DocFlavor;
import java.util.List;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-04-01
 */
@RestController
@RequestMapping("/eduservice/video")

public class EduVideoController {

    @Autowired
    private EduVideoService eduVideoService;

    @Resource
    private VodClient vodClient;

    @ApiModelProperty(value = "根据id查询单个小节")
    @GetMapping("getOneVideo/{videoId}")
    public R getOneVideo(@PathVariable String videoId){
        QueryWrapper<EduVideo> qw = new QueryWrapper<>();
        qw.eq("id",videoId);
        EduVideo eduVideo = eduVideoService.getOne(qw);
        return R.ok().data("eduVideo",eduVideo);
    }

    @ApiModelProperty(value = "添加小节")
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        if(StringUtils.isEmpty(eduVideo.getTitle())){
            throw new GuLiException(20001,"课时名称不能为空");
        }
        boolean flag = eduVideoService.save(eduVideo);
        if(flag){
            return R.ok();
        }
        return R.error();
    }

    @ApiModelProperty(value = "小节修改")
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        boolean flag = eduVideoService.updateById(eduVideo);
        if(flag){
            return R.ok();
        }
        return R.error();
    }

    @ApiModelProperty(value = "小节删除(视频也一并删除)")
    @DeleteMapping("{removeId}")
    public R removeVideo(@PathVariable String removeId){
        EduVideo eduVideo = eduVideoService.getById(removeId);  //根据课时id查询视频id
        String videoSourceId = eduVideo.getVideoSourceId();
        if(!StringUtils.isEmpty(videoSourceId)){
            //删除视频
            R result = vodClient.removeVideo(videoSourceId);
            if(result.getCode() == 20001){
                throw new GuLiException(20001,"删除视频失败,熔断机制.......");
            }
        }
        //删除小节
        boolean flag = eduVideoService.removeById(removeId);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }


}

