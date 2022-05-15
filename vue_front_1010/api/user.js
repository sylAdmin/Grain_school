import request from '@/utils/request'
export default {
  //更新用户头像
  uploadUser(userInfo) {
    return request({
      url: `/educenter/member/updateUserInfo`,
      method: 'post',
      data:userInfo
    })
  },

}