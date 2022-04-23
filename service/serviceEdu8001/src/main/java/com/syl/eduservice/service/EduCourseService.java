package com.syl.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syl.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.syl.eduservice.entity.frontvo.CourseFrontVo;
import com.syl.eduservice.entity.frontvo.CourseWebVo;
import com.syl.eduservice.entity.vo.CourseInfoVo;
import com.syl.eduservice.entity.vo.CoursePublishVo;
import com.syl.eduservice.entity.vo.CourseQuery;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-04-01
 */
public interface EduCourseService extends IService<EduCourse> {

    String saveCourseInfo(CourseInfoVo courseInfoVo);

    CourseInfoVo getCourseInfo(String courseId);

    void updateCourseInfo(CourseInfoVo courseInfoVo);

    CoursePublishVo publishCourseInfo(String id);

    void pageQuery(Page<EduCourse> eduCoursePage, CourseQuery courseQuery);

    void removeCourse(String courseId);

    Map<String, Object> getCourseFrontList(Page<EduCourse> eduCoursePage, CourseFrontVo courseFrontVo, HttpServletRequest request);

    CourseWebVo getBaseCourseInfo(String courseId);
}
