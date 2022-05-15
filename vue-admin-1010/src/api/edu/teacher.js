import request from '@/utils/request'

export default {

    //1、讲师列表（条件查询+分页）
    //current当前页 limit每页记录数 teacherQuery条件对象
    getTeacherListPage(current,limit,teacherQuery) {
        return request({
            //url: '/table/list',
            url: `/eduservice/teacher/pageTeacherCondition/${current}/${limit}` ,
            method: 'post',
            //teacherQuery条件对象，后端使用@RequestBody获取数据，前端必须这样写（固定写法）
            //data表示把对象转换成json进行传递到接口里面
            data: teacherQuery
          })
    },

    //2、删除讲师
    deleteTeacherId(id) {
        return request({
            //url: '/table/list',
            url: `/eduservice/teacher/delete/${id}` ,
            method: 'delete',
          })
    },

    //3、添加讲师
    addTeacher(teacher) {
        return request({
            //url: '/table/list',
            url: `/eduservice/teacher/addTeacher` ,
            method: 'post',
            data: teacher
          })
    },

    //4、修改数据回显讲师
    addTeacherInfo(id) {
        return request({
            //url: '/table/list',
            url: `/eduservice/teacher/getTeacher/${id}` ,
            method: 'get'
          })
    },

    //5、修改讲师
    updateTeacherInfo(teacher){
        return request({
            //url: '/table/list',
            url: `/eduservice/teacher/updateTeacher` ,
            method: 'put',
            data: teacher
          })
    }
}

