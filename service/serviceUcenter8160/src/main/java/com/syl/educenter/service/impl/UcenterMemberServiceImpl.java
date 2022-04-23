package com.syl.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syl.commonutils.JwtUtils;
import com.syl.commonutils.MD5;
import com.syl.educenter.entity.UcenterMember;
import com.syl.educenter.entity.vo.RegisterVo;
import com.syl.educenter.mapper.UcenterMemberMapper;
import com.syl.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syl.servicebase.exception.GuLiException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2022-04-10
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public String login(UcenterMember ucenterMember) {
        //获取手机号和密码
        String mobile = ucenterMember.getMobile();
        String password = ucenterMember.getPassword();
        //对手加号和密码进行非空判断
        if(StringUtils.isEmpty(mobile) && StringUtils.isEmpty(password)){
            throw new GuLiException(20001,"手机号或密码不能为空");
        }
        QueryWrapper<UcenterMember> qw = new QueryWrapper<>();
        qw.eq("mobile",mobile);
        UcenterMember mobileMember = baseMapper.selectOne(qw);
        if(mobileMember == null){  //判断手机号
            throw new GuLiException(20001,"手机号不存在,请先注册");
        }
        //将密码进行加密然后和数据库进行比较
        if(!MD5.encrypt(password).equals(mobileMember.getPassword())){  //判断密码
            throw new GuLiException(20001,"密码不正确");
        }
        if(mobileMember.getIsDisabled() == 1){
            throw new GuLiException(20001,"该账户已禁用请联系管理员");
        }
        //用户登录成功，把token值返回
        String token = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        return token;
    }

    @Override
    public void register(RegisterVo registerVo) {
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();
        String nickname = registerVo.getNickname();
        //非空判断
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(nickname) || StringUtils.isEmpty(code)){
            throw new GuLiException(20001,"信息填写不完整，注册失败!");
        }
        //判断验证码
        String codeRedis = redisTemplate.opsForValue().get(mobile);
        if(StringUtils.isEmpty(codeRedis)){
            throw new GuLiException(20001,"验证码已失效,请重新发送");
        }
        if(!code.equals(codeRedis)){
            throw new GuLiException(20001,"验证码错误");
        }
        UcenterMember ucenterMember = new UcenterMember();
        BeanUtils.copyProperties(registerVo,ucenterMember);
        ucenterMember.setOpenid("手机号用户");
        ucenterMember.setPassword(MD5.encrypt(password));
        ucenterMember.setAvatar("https://grain-school.oss-cn-hangzhou.aliyuncs.com/headPhoto/OIP.jpg");
        ucenterMember.setSex(1);
        ucenterMember.setAge(19);
        int result = baseMapper.insert(ucenterMember);
        if(result == 0){
            throw new GuLiException(20001,"添加失败,后台出错!!!");
        }

    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> qw = new QueryWrapper<>();
        qw.eq("openid",openid);
        UcenterMember ucenterMember = baseMapper.selectOne(qw);

        return ucenterMember;
    }

    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
