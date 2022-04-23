package com.syl.eduservice.controller;

import com.syl.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
public class EduLoginController {

    @GetMapping("login")
    public R login(){
        return R.ok().data("token","admin");
    }

    @GetMapping("info")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://grain-school.oss-cn-hangzhou.aliyuncs.com/headPhoto/20220401205721.jpg");
    }
}
