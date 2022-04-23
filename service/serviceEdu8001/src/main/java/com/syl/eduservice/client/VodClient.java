package com.syl.eduservice.client;

import com.syl.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "service-vod",fallback = VodFileDegradeFeignClient.class)  //出错兜底的方法
public interface VodClient {

    /**
     * 根据视频id删除阿里云视频（课时操作）
     */
    @DeleteMapping("/eduvod/video/removeVideo/{id}")
    public R removeVideo(@PathVariable("id") String id);

    /**
     * 删除多个视频（课程操作）
     */
    @DeleteMapping("/eduvod/video/delete-batch")
    public R deleteBatch(@RequestParam("videoList") List<String> videoList);
}
