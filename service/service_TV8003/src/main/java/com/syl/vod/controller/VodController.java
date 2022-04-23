package com.syl.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.syl.commonutils.R;
import com.syl.servicebase.exception.GuLiException;
import com.syl.vod.config.ConstantVodConfig;
import com.syl.vod.config.InitObject;
import com.syl.vod.service.VodService;
import com.syl.vod.utils.InitVodClient;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
public class VodController {

    @Autowired
    private VodService vodService;

    @Autowired
    private ConstantVodConfig constantVodConfig;

    @ApiOperation(value = "上传视频到阿里云")
    @PostMapping("uploadAlyVideo")
    public R uploadAlyVideo(MultipartFile file){
        //返回上传视频的id
        String videoId = vodService.uploadVideoAly(file);
        return R.ok().data("videoId",videoId);
    }

    @ApiOperation(value="删除阿里云视频")
    @DeleteMapping("removeVideo/{id}")
    public R removeVideo(@PathVariable String id){
        try {
            DefaultAcsClient client = InitVodClient.initVodClient(constantVodConfig.getKeyid(), constantVodConfig.getKeysecret());
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //向request设置视频id
            request.setVideoIds(id);
            //调用初始话对象的方法来实现删除
            client.getAcsResponse(request);
            return R.ok();
        }catch (Exception e){
            throw new GuLiException(20001,"删除视频失败");
        }
    }

    @ApiOperation(value = "删除多个视频")
    @DeleteMapping("delete-batch")
    public R deleteBatch(@RequestParam("videoList") List<String> videoList){
        vodService.removeMoreAlyVideo(videoList);
        return R.ok();
    }

    @ApiOperation(value = "根据视频id获取视频播放地址")
    @GetMapping("getPlayUrl/{id}")
    public R getPlayUrl(@PathVariable String id){
        try {
            //创建初始化对象
            DefaultAcsClient client = InitObject.initVodClient(constantVodConfig.getKeyid(),constantVodConfig.getKeysecret());
            //创建获取视频地址request和response
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            //向request对象设置视频id()
            request.setVideoId(id);
            //调用初始化对象里面的方法，传递request，获取数据
            GetPlayInfoResponse response = client.getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                String playURL = playInfo.getPlayURL();
                return R.ok().data("playURL",playURL);
            }
        }catch(Exception e){
            throw new GuLiException(20001,"视频播放失败");
        }
        return R.error();
    }
}
