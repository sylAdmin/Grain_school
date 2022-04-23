package com.syl.msmservice.client;

import com.syl.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-ucenter")
public interface UcenterClient {

    /**
     * 在发送验证码时，判断用户手机号是否已经注册
     */
    @GetMapping("/educenter/member/isRegister/{phone}")
    public boolean isRegister(@PathVariable("phone") String phone);
}
