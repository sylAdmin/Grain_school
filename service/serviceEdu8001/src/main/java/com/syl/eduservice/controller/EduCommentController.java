package com.syl.eduservice.controller;


import com.syl.commonutils.JwtUtils;
import com.syl.commonutils.R;
import com.syl.eduservice.entity.EduComment;
import com.syl.eduservice.service.EduCommentService;
import com.syl.servicebase.exception.GuLiException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author sylup
 * @since 2022-04-17
 */
@RestController
@RequestMapping("/eduservice/comment")

public class EduCommentController {

    @Autowired
    private EduCommentService eduCommentService;

    @ApiOperation(value = "分页查询所有评论")
    @GetMapping("/getAllComments/{courseId}/{page}/{limit}")
    public R getAllComments(@PathVariable Long page,
                            @PathVariable Long limit,
                            @PathVariable String courseId) {
        Map<String, Object> map = eduCommentService.selectPage(page, limit, courseId);
        return R.ok().data("items", map);
    }

    @ApiOperation(value = "添加评论")
    @PostMapping("/addComments")
    public R addComments(@RequestBody EduComment eduComment, HttpServletRequest request) {
        isLogin(request); //判断用户是否登录
        String loginId = JwtUtils.getMemberIdByJwtToken(request);  //获取当前登录用户的id
        eduComment.setUcenterId(loginId);
        if (StringUtils.isEmpty(eduComment.getContent())) {
            throw new GuLiException(20001, "评论内容不能为空");
        }
        if (eduCommentService.save(eduComment)) {
            return R.ok();
        }
        return R.error().message("添加评论失败,请重试");
    }

    @ApiOperation(value = "删除评论")
    @DeleteMapping("/deleteComments/{id}")
    public R deleteComments(@PathVariable String id,HttpServletRequest request) {
        eduCommentService.removeById(id);
        return R.ok();
    }

    /**
     * 判断用户是否已经登录
     * @param request 如果已经登陆，请求头中应该有token信息
     * @return 登录了返回true 没登陆抛出异常
     */
    public Boolean isLogin(HttpServletRequest request){
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new GuLiException(20004, "本站不支持匿名评论,请先进行登录");
        }
        return true;
    }

}

