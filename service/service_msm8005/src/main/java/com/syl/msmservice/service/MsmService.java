package com.syl.msmservice.service;

import java.util.HashMap;

public interface MsmService {
    boolean send(String phone) throws Exception;

    //返回验证码
    String getCode();
}
