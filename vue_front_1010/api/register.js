import request from '@/utils/request'
export default {
  //发送短信验证码
  sendCode(phone) {
    return request({
      url: `/edumsm/msm/send/${phone}`,
      method: 'get'
    })
  },

  //用户注册
  registerMember(formItem){
    return request({
        url: `/educenter/member/register`,
        method: 'post',
        data: formItem
      })
  }
}