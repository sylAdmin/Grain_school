import request from '@/utils/request'
export default {
  //条件分页查询课程
  getCourseList(page,limit,searchObj) {
    return request({
      url: `/eduservice/coursefront/getFrontCourseList/${page}/${limit}`,
      method: 'post',
      data:searchObj
    })
  },

  //查询所有分类的方法
  getAllSubject() {
    return request({
      url: `/eduservice/subject/getAllSubject`,
      method: 'get',
    })
  },

  //课程详情的方法
  getCourseInfo(id){
    return request({
        url: `/eduservice/coursefront/getFrontCourseInfo/${id}`,
        method: 'get',
    })
  },

  //分页查询所有评论
  getAllComment(courseId,page,limit){
    return request({
      url: `/eduservice/comment/getAllComments/${courseId}/${page}/${limit}`,
      method: 'get',
    })
  },

  //添加评论的方法
  addComment(comment){
    return request({
      url: `/eduservice/comment/addComments`,
      method: 'post',
      data: comment
    })
  },

  //删除评论的方法
  deleteComment(id){
    return request({
      url: `/eduservice/comment/deleteComments/${id}`,
      method: 'delete',
    })
  },

  //添加多级评论的方法
  addComments(comment,id){
    return request({
      url: `/eduservice/comment/addComments/${id}`,
      method: 'post',
      data: comment
    })
  },

}