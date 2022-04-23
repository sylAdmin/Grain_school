package com.syl.eduservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syl.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author sylup
 * @since 2022-04-17
 */
public interface EduCommentService extends IService<EduComment> {

    Map<String, Object> selectPage(Long page, Long limit, String courseId);
}
