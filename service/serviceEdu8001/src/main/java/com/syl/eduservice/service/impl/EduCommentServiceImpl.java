package com.syl.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syl.eduservice.entity.EduComment;
import com.syl.eduservice.mapper.EduCommentMapper;
import com.syl.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author sylup
 * @since 2022-04-17
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

    @Autowired
    private EduCommentService eduCommentService;

    @Override
    public Map<String, Object> selectPage(Long page, Long limit, String courseId) {
        Page<EduComment> pageParam = new Page<>(page, limit);
        QueryWrapper<EduComment> qw = new QueryWrapper<>();
        qw.orderByDesc("gmt_create");
        qw.eq("course_id", courseId);
        eduCommentService.page(pageParam, qw);
        List<EduComment> commentList = pageParam.getRecords();

        //递归查询分类
        Map<String, Object> map = new HashMap<>();
        commentList = getCommentList(commentList);
        for (EduComment eduComment : commentList) {
            map.put("items", eduComment.getChildren());
        }
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize() + 1);
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return map;
    }

    private List<EduComment> getCommentList(List<EduComment> commentList) {
        List<EduComment> eduCommentList = new ArrayList<>();
        EduComment eduComment = baseMapper.selectById(0);
        eduComment.setLevel(0);
        eduCommentList.add(recursive(eduComment, commentList));
        return eduCommentList;
    }

    private EduComment recursive(EduComment eduComment, List<EduComment> commentList) {
        eduComment.setChildren(new ArrayList<>());
        for (EduComment comment : commentList) {
            if (eduComment.getId().equals(comment.getParentId())) {
                comment.setLevel(eduComment.getLevel() + 1);
                eduComment.getChildren().add(recursive(comment, commentList));
            }
        }
        return eduComment;
    }

}
