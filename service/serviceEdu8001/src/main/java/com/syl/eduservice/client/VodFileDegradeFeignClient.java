package com.syl.eduservice.client;

import com.syl.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component  //当远程调用方法出错就执行这里的方法
public class VodFileDegradeFeignClient implements VodClient{
    @Override
    public R removeVideo(String id) {
        return R.error().message("删除视频出错了");
    }

    @Override
    public R deleteBatch(List<String> videoList) {
        return R.error().message("删除多个视频出错了");
    }
}
