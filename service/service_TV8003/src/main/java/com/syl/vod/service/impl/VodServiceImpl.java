package com.syl.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.syl.commonutils.R;
import com.syl.vod.config.ConstantVodConfig;
import com.syl.vod.service.VodService;
import com.syl.vod.utils.InitVodClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.syl.servicebase.exception.GuLiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class VodServiceImpl implements VodService {

    @Autowired
    private ConstantVodConfig constantVodConfig;

    @Override
    public String uploadVideoAly(MultipartFile file) {
        String keyid = constantVodConfig.getKeyid();
        String keysecret = constantVodConfig.getKeysecret();

        try {
            InputStream inputStream = file.getInputStream();
            //上传文件原始名称
            String originalFilename = file.getOriginalFilename();
            //上传之后显示的名程
            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));

            UploadStreamRequest request = new UploadStreamRequest(
                    keyid,keysecret,
                    title, originalFilename, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            String videoId = response.getVideoId();
            if (!response.isSuccess()) {
                String errorMessage = "阿里云上传错误：" + "code：" + response.getCode() + ", message：" + response.getMessage();
                log.warn(errorMessage);
                if(StringUtils.isEmpty(videoId)){
                    throw new GuLiException(20001, errorMessage);
                }
            }
            System.out.println("视频id："+videoId);
            return videoId;
        } catch (IOException e) {
            throw new GuLiException(20001, "guli vod 服务上传失败");
        }
    }

    @Override
    public void removeMoreAlyVideo(List<String> videoList) {
        try {
            DefaultAcsClient client = InitVodClient.initVodClient(constantVodConfig.getKeyid(), constantVodConfig.getKeysecret());
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //将集合中的值按照 , 进行拼接(例1,2,3,)
            String id = org.apache.commons.lang.StringUtils.join(videoList.toArray(), ",");
            //向request设置视频id
            request.setVideoIds(id);
            //调用初始话对象的方法来实现删除
            client.getAcsResponse(request);
        }catch (Exception e){
            throw new GuLiException(20001,"删除视频失败");
        }
    }
}
