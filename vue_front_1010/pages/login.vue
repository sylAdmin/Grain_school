<template>
  <div class="main">
    <!-- 公共头引入 -->
    <div class="title">

      <a class="active" href="/login">登录</a>
      <span>·</span>
      <a href="/register">注册</a>
    </div>

    <div class="sign-up-container">
      <el-form ref="userForm" :model="user">
        <el-form-item
          class="input-prepend restyle"
          prop="mobile"
          :rules="[
            { required: true, message: '请输入手机号码', trigger: 'blur' },
            { validator: checkPhone, trigger: 'blur' },
          ]"
        >
          <div>
            <el-input type="text" placeholder="手机号" v-model="user.mobile" />
            <i class="iconfont icon-phone" />
          </div>
        </el-form-item>

        <el-form-item
          class="input-prepend"
          prop="password"
          :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]"
        >
          <div>
            <el-input
              type="password"
              placeholder="密码"
              v-model="user.password"
            />
            <i class="iconfont icon-password" />
          </div>
        </el-form-item>

        <div class="btn">
          <input
            type="button"
            class="sign-in-button"
            value="登录"
            @click="submitLogin()"
          />
        </div>
      </el-form>
      <!-- 更多登录方式 -->
      <div class="more-sign">
        <h6>社交帐号登录</h6>
        <ul>
          <li>
            <a
              id="weixin"
              class="weixin"
              href="http://localhost:8222/api/ucenter/wx/login"
              ><i class="iconfont icon-weixin"
            /></a>
          </li>
          <li>
            <a id="qq" class="qq" target="_blank" href="#"
              ><i class="iconfont icon-qq"
            /></a>
          </li>
        </ul>
        <h6 style="color: red">目前仅支持微信登录</h6>
      </div>
    </div>
  </div>
</template>

<script>
import "~/assets/css/sign.css";
import "~/assets/css/iconfont.css";
import cookie from "js-cookie";
import loginApi from "@/api/login";

export default {
  layout: "sign",
  data() {
    return {
      user: {
        mobile: "", //用户手机号
        password: "", //用户密码
      },
      loginInfo: {}, //用户信息
      token: "", //用户的token值
    };
  },

  methods: {
    //登录方法
    submitLogin() {
      //第一步 调用接口进行登录，返回token字符串
      loginApi.submitLoginUser(this.user).then((response) => {
        //第二步 获取token字符串放在cookie里面
        /*
              第一个参数cookie名称，第二个参数值，第三个参数值作用范围            
            */
        cookie.set("guli_token", response.data.token, { domain: "localhost" });

        //第四步 调用接口 根据token获取用户信息，为了首页面显示
        loginApi.getLoginUserInfo().then((response) => {
          this.loginInfo = JSON.stringify(response.data.userInfo);
          //获取返回的用户信息，放到cookie里面
          cookie.set("guli_ucenter", this.loginInfo, { domain: "localhost" });
          //跳转到首页面
          // window.location.href = '/'
          this.$router.push({ path: "/" });
        });
      });
    },
  },
};
</script>
<style>
.el-form-item__error {
  z-index: 9999999;
}
</style>