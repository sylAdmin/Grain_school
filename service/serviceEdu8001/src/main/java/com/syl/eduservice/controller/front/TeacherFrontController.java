package com.syl.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syl.commonutils.R;
import com.syl.eduservice.entity.EduCourse;
import com.syl.eduservice.entity.EduTeacher;
import com.syl.eduservice.service.EduCourseService;
import com.syl.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description = "讲师前端管理")
@RestController
@RequestMapping("/eduservice/teacherfront")

public class TeacherFrontController {

    @Autowired
    private EduTeacherService eduTeacherService;

    @Autowired
    private EduCourseService eduCourseService;

    @ApiOperation(value = "分页查询讲师数据")
    @PostMapping("getTeacherFrontList/{page}/{limit}")
    public R getTeacherFrontList(@PathVariable Long page,
                                 @PathVariable Long limit){
        Page<EduTeacher> teacherPage = new Page<>(page,limit);
        Map<String,Object> map = eduTeacherService.getTeacherFrontList(teacherPage);
        return R.ok().data(map);
    }

    @ApiOperation(value = "讲师详情的功能")
    @GetMapping("getTeacherInfo/{teacherId}")
    public R getTeacherInfo(@PathVariable String teacherId){
        //1、根据讲师id查询讲师基本信息
        EduTeacher eduTeacher = eduTeacherService.getById(teacherId);
        //2、根据讲师id查询所讲课程
        QueryWrapper<EduCourse> qw = new QueryWrapper<>();
        qw.eq("teacher_id",teacherId);
        List<EduCourse> courseList = eduCourseService.list(qw);
        return R.ok().data("teacher",eduTeacher).data("courseList",courseList);
    }
}
