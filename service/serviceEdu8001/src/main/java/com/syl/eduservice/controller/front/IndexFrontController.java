package com.syl.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syl.commonutils.R;
import com.syl.eduservice.entity.EduCourse;
import com.syl.eduservice.entity.EduTeacher;
import com.syl.eduservice.service.EduCourseService;
import com.syl.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "前端首页数据显示")
@RequestMapping("/eduservice/indexfront")
@RestController

public class IndexFrontController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduTeacherService eduTeacherService;

    @ApiOperation(value = "查询前8条热门课程，查询前4名讲师")
    @Cacheable(key = "'selectCourseAndTeacher'",value = "courseAndTeacher")
    @GetMapping("index")
    public R index(){
        QueryWrapper<EduCourse> qwEduCourse = new QueryWrapper<>();
        qwEduCourse.orderByDesc("view_count");
        qwEduCourse.last("limit 8");
        List<EduCourse> eduCourseList = eduCourseService.list(qwEduCourse);
        QueryWrapper<EduTeacher> qwEduTeacher = new QueryWrapper<>();
        qwEduTeacher.orderByDesc("sort");
        qwEduTeacher.last("limit 4");
        List<EduTeacher> eduTeacherList = eduTeacherService.list(qwEduTeacher);
        return R.ok().data("eduCourseList",eduCourseList).data("eduTeacherList",eduTeacherList);
    }

    @ApiOperation(value = "查询全部课程")
    @GetMapping("allCourse")
    public R allCourse(){
        QueryWrapper<EduCourse> qwEduCourse = new QueryWrapper<>();
        qwEduCourse.orderByDesc("view_count");
        List<EduCourse> courseList = eduCourseService.list(qwEduCourse);
        return R.ok().data("courseList",courseList);
    }
}
