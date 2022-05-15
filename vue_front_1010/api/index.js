import request from '@/utils/request'
export default {
  //查询课程和名师
  getIndexData() {
    return request({
      url: `/eduservice/indexfront/index`,
      method: 'get'
    })
  },

  //查询所有的课程
  getAllCourse() {
    return request({
      url: `/eduservice/indexfront/allCourse`,
      method: 'get'
    })
  }
}