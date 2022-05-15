import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/eduservice/user/login',
    method: 'post',
    data: {
      username,
      password
    }
  })
}

export function getInfo(token) {
  return request({
    url: '/eduservice/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export default {
  //用户登录
  submitLoginUser(userInfo){
    return request({
      url: '/educenter/member/login',
      method: 'post',
      data:userInfo
    })
  },

  //根据token获取用户信息
  getLoginUserInfo(){
    return request({
      url: '/educenter/member/getMemberInfo',
      method: 'get',
    })
  },
  
}
