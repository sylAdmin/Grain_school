package com.syl.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syl.commonutils.JwtUtils;
import com.syl.commonutils.R;
import com.syl.commonutils.ordervo.CourseWebVoOrder;
import com.syl.eduservice.client.OrderClient;
import com.syl.eduservice.entity.EduCourse;
import com.syl.eduservice.entity.chapter.ChapterVo;
import com.syl.eduservice.entity.frontvo.CourseFrontVo;
import com.syl.eduservice.entity.frontvo.CourseWebVo;
import com.syl.eduservice.service.EduChapterService;
import com.syl.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(value = "前端课程")
@RestController
@RequestMapping("/eduservice/coursefront")

public class CourseFrontController {

    @Autowired
    private EduCourseService eduCourseService;

    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private OrderClient orderClient;

    @ApiOperation(value = "条件查询带分页查询课程")
    @PostMapping("/getFrontCourseList/{page}/{limit}")
    public R getFrontCourseList(@PathVariable Long page,
                                @PathVariable Long limit,
                                @RequestBody CourseFrontVo courseFrontVo,
                                HttpServletRequest request){
        Page<EduCourse> eduCoursePage = new Page<>(page,limit);
        Map<String, Object> map = eduCourseService.getCourseFrontList(eduCoursePage,courseFrontVo,request);
        return R.ok().data(map);
    }

    @ApiOperation(value = "课程详情的方法")
    @GetMapping("/getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request){
        //根据课程id，编写sql语句查询课程信息
        CourseWebVo courseWebVo = eduCourseService.getBaseCourseInfo(courseId);
        //根据课程id查询章节和小节
        List<ChapterVo> chapterVideoList = eduChapterService.getChapterVideoByCourseId(courseId);
        if(!StringUtils.isEmpty(request.getHeader("token"))) {
            //根据课程id和用户id查询当前课程是否已经支付过了
            boolean buyCourse = orderClient.isBuyCourse(courseId, JwtUtils.getMemberIdByJwtToken(request));
            return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",buyCourse);
        }
        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList);
    }

    /**
     * 根据课程id查询课程信息
     */
    @ApiOperation(value = "根据课程id查询课程信息")
    @GetMapping("/getCourseInfoOrder/{courseId}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String courseId){
        CourseWebVo courseWebVo = eduCourseService.getBaseCourseInfo(courseId);
        CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
        BeanUtils.copyProperties(courseWebVo,courseWebVoOrder);
        return courseWebVoOrder;
    }
}
