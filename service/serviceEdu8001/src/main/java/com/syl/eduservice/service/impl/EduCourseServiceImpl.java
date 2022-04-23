package com.syl.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syl.commonutils.JwtUtils;
import com.syl.eduservice.client.OrderClient;
import com.syl.eduservice.entity.EduCourse;
import com.syl.eduservice.entity.EduCourseDescription;
import com.syl.eduservice.entity.EduTeacher;
import com.syl.eduservice.entity.frontvo.CourseFrontVo;
import com.syl.eduservice.entity.frontvo.CourseWebVo;
import com.syl.eduservice.entity.vo.CourseInfoVo;
import com.syl.eduservice.entity.vo.CoursePublishVo;
import com.syl.eduservice.entity.vo.CourseQuery;
import com.syl.eduservice.mapper.EduCourseMapper;
import com.syl.eduservice.service.EduChapterService;
import com.syl.eduservice.service.EduCourseDescriptionService;
import com.syl.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syl.eduservice.service.EduVideoService;
import com.syl.servicebase.exception.GuLiException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-04-01
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired  //课程描述
    private EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired  //课程章节
    private EduChapterService eduChapterService;

    @Autowired  //课程课时
    private EduVideoService eduVideoService;

    @Autowired  //订单模块
    private OrderClient orderClient;

    /**
     * 添加课程基本信息
     * @param courseInfoVo
     */
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //将课程简介去空格
        courseInfoVo.setDescription(courseInfoVo.getDescription().trim());
        //1、向课程表添加课程基本信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if(insert != 1){
            throw new GuLiException(20001,"添加课程信息失败");
        }
        //获取课程添加之后的id
        String eduCourseId = eduCourse.getId();

        //2、向课程简介表添加课程简介
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo, eduCourseDescription);
        eduCourseDescription.setId(eduCourseId);
        eduCourseDescriptionService.save(eduCourseDescription);
        if(insert != 1){
            throw new GuLiException(20001,"添加课程简介失败");
        }
        return eduCourseId;
    }

    @Override
    public CourseInfoVo getCourseInfo(String courseId) {

        CourseInfoVo courseInfoVo = new CourseInfoVo();
        //1、查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        //2、查询描述表
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(courseId);
        if(eduCourseDescription != null){
            courseInfoVo.setDescription(eduCourseDescription.getDescription());
            courseInfoVo.setId(courseId);
        }
        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        //更新课程信息
        int update = baseMapper.updateById(eduCourse);
        if(update == 0){
            throw new GuLiException(20001,"修改课程信息失败，请重试");
        }

        //更新简介信息
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo,eduCourseDescription);
        boolean update1 = eduCourseDescriptionService.updateById(eduCourseDescription);
        if(!update1){
            throw new GuLiException(20001,"修改课程信息成功！！修改课程简介失败，请重试");
        }
    }

    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        CoursePublishVo coursePublishInfo = baseMapper.getCoursePublishInfo(id);
        return coursePublishInfo;
    }

    //条件查询带分页
    @Override
    public void pageQuery(Page<EduCourse> eduCoursePage, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> qw = new QueryWrapper<>();
        qw.orderByDesc("gmt_modified");
        //如果没有查询条件就查询所有，并且按更新时间排序
        if(courseQuery == null){
            baseMapper.selectPage(eduCoursePage,qw);
            eduCoursePage.getRecords();
            return;
        }

        if(!StringUtils.isEmpty(courseQuery.getTitle())){
            qw.like("title",courseQuery.getTitle());
        }
        if(!StringUtils.isEmpty(courseQuery.getTeacherId())){
            qw.eq("teacher_id",courseQuery.getTeacherId());
        }
        //是否已经发布
        if(!StringUtils.isEmpty(courseQuery.getStatus())){
            qw.eq("status",courseQuery.getStatus());
        }
        //一级分类
        if(!StringUtils.isEmpty(courseQuery.getSubjectParentId())){
            qw.eq("subject_parent_id",courseQuery.getSubjectParentId());
        }
        //二级分类
        if(!StringUtils.isEmpty(courseQuery.getSubjectId())){
            qw.eq("subject_id",courseQuery.getSubjectId());
        }
        baseMapper.selectPage(eduCoursePage,qw);
    }

    @Transactional  //关联多张表建议加上事务
    @Override
    public void removeCourse(String courseId) {
        //1、根据课程id删除小节
        eduVideoService.removeVideoByCourseId(courseId);
        //2、根据课程id删除章节
        eduChapterService.removeChapterByCourseId(courseId);
        //3、根据课程id删除描述
        eduCourseDescriptionService.removeById(courseId);
        //4、删除课程本身
        int result = baseMapper.deleteById(courseId);
        if(result == 0){
            throw new GuLiException(20001,"删除失败");
        }
    }

    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo, HttpServletRequest request) {
        QueryWrapper<EduCourse> qw = new QueryWrapper<>();
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){  //一级分类
            qw.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId())){  //二级分类
            qw.eq("subject_id",courseFrontVo.getSubjectId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getViewCount())){  //关注度
            qw.orderByDesc("view_count");
        }
        if(!StringUtils.isEmpty(courseFrontVo.getGmtModified())){  //最新
            qw.orderByDesc("gmt_modified");
        }
        if(!StringUtils.isEmpty(courseFrontVo.getPriceSort())){  //价格
            qw.orderByDesc("price");
        }
        if(courseFrontVo.getTitle() == null){
            qw.orderByDesc("view_count");
        }
        baseMapper.selectPage(pageParam,qw);
        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();
        HashMap<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        //判断用户是否登录，登录之后在做操作
        if(!StringUtils.isEmpty(request.getHeader("token"))){
            String memberId = JwtUtils.getMemberIdByJwtToken(request);
            List<String> courseList = orderClient.isBuyCourses(memberId);
            for (EduCourse course : records) {
                for (String courseId : courseList) {
                    if(course.getId().equals(courseId)){ //将已经支付的课程价格设置成0,数据库不变
                        course.setPrice(BigDecimal.valueOf(Long.valueOf(0+"")));
                    }
                }
            }
            return map;
        }
        return map;
    }

    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}
