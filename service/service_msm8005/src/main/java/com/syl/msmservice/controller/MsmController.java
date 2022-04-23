package com.syl.msmservice.controller;

import com.syl.commonutils.R;
import com.syl.msmservice.service.MsmService;
import com.syl.msmservice.utils.RandomUtil;
import com.syl.servicebase.exception.GuLiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 发送短信方法
     */
    @ApiOperation(value = "发送验证码")
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone) throws Exception {
        //1、从redis获取验证码，如果获取到就直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)){
            throw new GuLiException(20001,"验证码已发送，不能重复发送");
        }
        //2、如果redis获取不到进行阿里云发送
        boolean isSend = msmService.send(phone);
        if(isSend){
            //如果发送成功,把发送成功验证码放到redis里面[
            //设置有效时间 5分钟
            redisTemplate.opsForValue().set(phone,msmService.getCode(),1, TimeUnit.MINUTES);
            return R.ok();
        }
        return R.error().message("短信发送失败,手机号可能不存在");
    }
}
