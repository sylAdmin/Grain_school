package com.syl.educenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.syl.commonutils.JwtUtils;
import com.syl.educenter.config.WxConfigProperties;
import com.syl.educenter.entity.UcenterMember;
import com.syl.educenter.service.UcenterMemberService;
import com.syl.educenter.utils.HttpClientUtils;
import com.syl.servicebase.exception.GuLiException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private WxConfigProperties wxConfigProperties;

    @Autowired
    private UcenterMemberService ucenterMemberService;

    /**
     * 获取扫描人信息，添加数据，扫码并实现登录
     */
    @GetMapping("callback")
    public String callback(String code, String state) {
        //1、获取code值，临时票据，类似于验证码

        //2、拿着code请求，微信固定地址，得到两个值，access_token 和 openid
        Map<String, String> tokenAndOpenId = getTokenAndOpenId(code, state);

        String access_token = tokenAndOpenId.get("access_token");
        String openid = tokenAndOpenId.get("openid");
        //判断数据库表里是否存在相同微信信息，根据opedid判断
        UcenterMember ucenterMember = ucenterMemberService.getOpenIdMember(openid);

        //3、拿着得到的access_token和openid，再去请求微信提供的地址，获取到扫描人信息,并添加到数据库
        //如果有用户信息并且用户的头像和昵称没有变化就不做操作，如果用户的信息发生了变化，就更新数据库中的信息
        ucenterMember = getUserInfo(access_token, openid, ucenterMember);

        //使用jwt根据member对象生成token字符串
        String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(),ucenterMember.getNickname());
        //最后返回首页面,通过路径传递token字符串
        return "redirect:http://localhost:3000?token=" + jwtToken;
    }

    /**
     * 生成微信扫描二维码
     */
    @GetMapping("login")
    public String getWxCode() {
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //对redirect_url进行编码
        String redirectUrl = wxConfigProperties.getRedirect_url();
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(
                baseUrl,
                wxConfigProperties.getApp_id(),
                redirectUrl,
                "syl"
        );
        return "redirect:" + url;
    }


    //2、拿着code请求，微信固定地址，得到两个值，access_token 和 openid
    //向认证服务器发送请求换取access_token
    public Map<String, String> getTokenAndOpenId(String code, String state) {
        try {
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //拼接三个地址 id密钥 和 code值
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    wxConfigProperties.getApp_id(),
                    wxConfigProperties.getApp_secret(),
                    code
            );
            //请求这个拼接好的地址，得到返回两个值 access_token 和 openid
            //使用httpClient发送请求，得到返回结果（相当于在浏览器中写请求地址）
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            //从accessTokenInfo字符串取出两个值access_token 和 openid
            //把accessTokenInfo字符串转换成map集合，根据map里面key获取对应值
            //使用json转换工具  Gson
            Gson gson = new Gson();
            HashMap map = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) map.get("access_token");
            String openid = (String) map.get("openid");
            HashMap<String, String> mapInfo = new HashMap<>();
            mapInfo.put("access_token", access_token);
            mapInfo.put("openid", openid);
            return mapInfo;
        } catch (Exception e) {
            throw new GuLiException(20001, "获取 access_token 和 openid 失败");
        }
    }

    //3、拿着得到的access_token和openid，再去请求微信提供的地址，获取到扫描人信息
    public UcenterMember getUserInfo(String access_token, String openid, UcenterMember ucenterMember) {
        try {
            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" + "?access_token=%s" + "&openid=%s";
            //拼接两个参数
            String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
            //发送请求
            String userInfo = HttpClientUtils.get(userInfoUrl);
            //获取返回userInfo字符串扫描人信息
            Gson gson = new Gson();
            HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
            String nickname = (String) userInfoMap.get("nickname"); //昵称
            String avatar = (String) userInfoMap.get("headimgurl"); //头像
            if (ucenterMember == null) {  //用户没有注册过，就进行数据库添加
                ucenterMember = new UcenterMember();
                ucenterMember.setOpenid(openid);
                ucenterMember.setAvatar(avatar);
                ucenterMember.setNickname(nickname);
                ucenterMemberService.save(ucenterMember);
                return ucenterMember;
            } else if (!avatar.equals(ucenterMember.getAvatar()) || !nickname.equals(ucenterMember.getNickname())) {
                //如果数据库中有用户，但是用户头像或昵称信息变了，进行更新
                QueryWrapper<UcenterMember> qw = new QueryWrapper<>();
                qw.eq("openid", openid);
                ucenterMember.setAvatar(avatar);
                ucenterMember.setNickname(nickname);
                ucenterMemberService.update(ucenterMember, qw);
                return ucenterMember;
            } else {
                //用户信息没有改变，不用做设置
                return ucenterMember;
            }
        } catch (Exception e) {
            throw new GuLiException(20001, "获取 微信扫描人信息 失败");
        }
    }
}
