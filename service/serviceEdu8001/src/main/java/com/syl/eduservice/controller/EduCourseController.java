package com.syl.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syl.commonutils.R;
import com.syl.eduservice.entity.EduCourse;
import com.syl.eduservice.entity.vo.CourseInfoVo;
import com.syl.eduservice.entity.vo.CoursePublishVo;
import com.syl.eduservice.entity.vo.CourseQuery;
import com.syl.eduservice.service.EduCourseService;
import com.syl.servicebase.exception.GuLiException;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-04-01
 */
@RestController
@RequestMapping("/eduservice/course")

public class EduCourseController {

    @Autowired
    private EduCourseService eduCourseService;

    @ApiModelProperty(value = "条件查询带封装")
    @GetMapping("{page}/{limit}")
    public R pageQuery(@PathVariable Long page,
                       @PathVariable Long limit,
                        CourseQuery courseQuery){
        Page<EduCourse> eduCoursePage = new Page<>(page,limit);
        eduCourseService.pageQuery(eduCoursePage,courseQuery);
        List<EduCourse> records = eduCoursePage.getRecords();
        long total = eduCoursePage.getTotal();
        return R.ok().data("total",total).data("records",records);
    }

    @ApiModelProperty(value = "添加课程基本信息的方法")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        if (courseInfoVo.getTitle() == "" || courseInfoVo.getDescription() == "" ||
                courseInfoVo.getPrice() == null || courseInfoVo.getDescription() == null) {
            throw new GuLiException(20001, "课程信息不能为空");
        }
        //返回添加之后的课程id，为了添加大纲使用
        String id = eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId", id);
    }

    @ApiModelProperty(value = "查询课程基本信息")
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
        if(!StringUtils.isEmpty(courseId)){
            CourseInfoVo courseInfoVo = eduCourseService.getCourseInfo(courseId);
            return R.ok().data("courseInfoVo",courseInfoVo);
        }
        return R.error().data("20001","id不能为空");
    }

    @ApiModelProperty(value = "修改课程基本信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        eduCourseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    @ApiModelProperty(value = "根据id查询课程确认信息")
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
        CoursePublishVo coursePublishVo = eduCourseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }

    @ApiModelProperty(value="修改课程状态")
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        eduCourseService.updateById(eduCourse);
        return R.ok();
    }

    @ApiModelProperty(value = "课程分类列表-删除课程")
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId){
        eduCourseService.removeCourse(courseId);
        return R.ok();
    }
}

