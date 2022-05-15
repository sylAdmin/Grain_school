import request from '@/utils/request'

export default {

    //课程确认信息显示
    getPublishCourseInfo(id){
        return request({
            url: `/eduservice/course/getPublishCourseInfo/${id}`,
            method: 'get',    
        })
    },

    //课程最终发布
    publishCourse(id){
        return request({
            url: `/eduservice/course/publishCourse/${id}`,
            method: 'post',    
        })
    },
}