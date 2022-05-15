import request from '@/utils/request'

export default {

    //条件查询带分页
    getPageList(page, limit, searchObj) {
        return request({
          url: `/eduservice/course/${page}/${limit}`,
          method: 'get',
          params: searchObj
        })
    },

    //1、添加课程信息
    getCourseInfo(courseInfo) {
        return request({
            url: `/eduservice/course/addCourseInfo` ,
            method: 'post',
            data: courseInfo
          })
    },

    //2、查询所有讲师
    getListTeacher() {
        return request({
            url: `/eduservice/teacher/findAll` ,
            method: 'get',
          })
    },

    //3、根据课程id查询课程基本信息
    getCourseInfoId(id) {
        return request({
            url: `/eduservice/course/getCourseInfo/${id}` ,
            method: 'get',
          })
    },

    //3、修改课程信息
    updateCourseInfoId(courseInfoVo) {
        return request({
            url: `/eduservice/course/updateCourseInfo` ,
            method: 'post',
            data: courseInfoVo
          })
    },

    //4、删除课程
    deleteCourse(id){
        return request({
            url: `/eduservice/course/${id}` ,
            method: 'delete',
        }) 
    }

    
}

