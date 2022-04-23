package com.syl.msmservice.service.impl;

import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.syl.commonutils.R;
import com.syl.msmservice.client.UcenterClient;
import com.syl.msmservice.service.MsmService;
import com.syl.msmservice.utils.RandomUtil;
import com.syl.servicebase.exception.GuLiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class MsmServiceImpl implements MsmService {

    String code;
    @Autowired
    private UcenterClient ucenterClient;

    /**
     * 发送验证码
     */
    @Override
    public boolean send(String phone) throws Exception {

        //远程调用ucenter，判断用户手机号是否已经注册
        boolean isRegisters = ucenterClient.isRegister(phone);
        if(isRegisters){
            throw new GuLiException(20001,"该手机号已经注册,换个手机号试试！");
        } //没有注册执行以下逻辑

        code = RandomUtil.getFourBitRandom();  //4位验证码
        com.aliyun.dysmsapi20170525.Client client = createClient("LTAI5tNBkkTAsFNVuxPxmBbQ", "AyI1XK80MnGjIBdKh4ByvM9s9LF321");
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName("阿里云短信测试")
                .setTemplateCode("SMS_154950909")
                .setPhoneNumbers(phone)
                .setTemplateParam("{code:"+code+"}");
        // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse response = client.sendSms(sendSmsRequest);
        String message = response.getBody().message;
        if("OK".equals(message)){
            System.out.println("手机验证码：" + code);
            return true;
        }
        return false;
    }

    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    /**
     * 返回当前验证码
     * @return
     */
    @Override
    public String getCode() {
        return code;
    }
}
