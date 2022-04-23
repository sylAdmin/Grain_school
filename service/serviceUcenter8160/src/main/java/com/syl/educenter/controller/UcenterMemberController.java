package com.syl.educenter.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.syl.commonutils.JwtUtils;
import com.syl.commonutils.R;
import com.syl.commonutils.ordervo.UcenterMemberOrder;
import com.syl.educenter.entity.UcenterMember;
import com.syl.educenter.entity.vo.RegisterVo;
import com.syl.educenter.service.UcenterMemberService;
import com.syl.servicebase.exception.GuLiException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author syl
 * @since 2022-04-10
 */
@RestController
@RequestMapping("/educenter/member")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService ucenterMemberService;

    /**
     * 判断用户手机号是否已经注册
     */
    @GetMapping("isRegister/{phone}")
    public boolean isRegister(@PathVariable String phone){
        QueryWrapper<UcenterMember> qw = new QueryWrapper<>();
        qw.eq("mobile",phone);
        UcenterMember ucenterMember = ucenterMemberService.getOne(qw);
        if(ucenterMember != null){
            return true;
        }
        return false;
    }

    /**
     * 调用service方法实现登录
     * @param ucenterMember 用户信息实体类
     * @return 返回token值，使用jwt生成
     */
    @ApiOperation(value = "登录用JWT实现")
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember ucenterMember){
        String token = ucenterMemberService.login(ucenterMember);
        return R.ok().data("token",token);
    }

    /**
     * 用户注册
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo){
        ucenterMemberService.register(registerVo);
        return R.ok().data("message","注册成功");
    }

    /**
     * 根据token查询数据库获取用户信息显示在页面
     */
    @ApiOperation(value="根根据token获取用户信息")
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        QueryWrapper<UcenterMember> qw = new QueryWrapper<>();
        qw.eq("id",memberId);
        UcenterMember ucenterMember = ucenterMemberService.getOne(qw);
        return R.ok().data("userInfo",ucenterMember);
    }

    /**
     * 根据用户id更新用户头像
     */
    @ApiOperation(value="根据用户id更新用户头像")
    @PostMapping("updateUserInfo")
    public R updateUserInfo(@RequestBody UcenterMember ucenterMember){
        ucenterMemberService.updateById(ucenterMember);
        return R.ok();
    }

    /**
     * 根据用户id获取用户信息
     */
    @ApiOperation(value = "根据用户id获取用户信息")
    @GetMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id){
        UcenterMember ucenterMember = ucenterMemberService.getById(id);
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(ucenterMember,ucenterMemberOrder);
        return ucenterMemberOrder;
    }

    /**
     * 查询某一天注册人数
     */
    @ApiOperation(value = "查询某一天注册人数")
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
       Integer count = ucenterMemberService.countRegisterDay(day);
       return R.ok().data("countRegister",count);
    }

}

