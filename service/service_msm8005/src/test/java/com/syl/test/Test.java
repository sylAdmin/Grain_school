package com.syl.test;

import com.syl.msmservice.MsmApplication;
import com.syl.msmservice.service.MsmService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = MsmApplication.class)
@RunWith(SpringRunner.class)
public class Test {

    @Autowired
    private MsmService msmservice;

    @org.junit.Test
    public void test01() throws Exception {
        msmservice.send("18953458694");
        System.out.println(msmservice.getCode());
    }
}
