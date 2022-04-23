package com.syl.eduservice.mapper;

import com.syl.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syl.eduservice.entity.frontvo.CourseWebVo;
import com.syl.eduservice.entity.vo.CoursePublishVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2022-04-01
 */
@Mapper
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    CoursePublishVo getCoursePublishInfo(@Param("id") String courseId);

    CourseWebVo getBaseCourseInfo(@Param("id") String courseId);
}
