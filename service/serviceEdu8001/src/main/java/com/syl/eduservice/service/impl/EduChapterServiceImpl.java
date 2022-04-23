package com.syl.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syl.eduservice.entity.EduChapter;
import com.syl.eduservice.entity.EduVideo;
import com.syl.eduservice.entity.chapter.ChapterVo;
import com.syl.eduservice.entity.chapter.VideoVo;
import com.syl.eduservice.mapper.EduChapterMapper;
import com.syl.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syl.eduservice.service.EduVideoService;
import com.syl.servicebase.exception.GuLiException;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-04-01
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;

    /**
     * 课程大纲列表
     * @param courseId
     * @return
     */
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //最终要的到的数据列表
        ArrayList<ChapterVo> chapterVoArrayList = new ArrayList<>();

        //获取章节信息
        QueryWrapper<EduChapter> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("course_id", courseId);
        queryWrapper1.orderByAsc("sort", "id");
        List<EduChapter> chapters = baseMapper.selectList(queryWrapper1);

        //获取课时信息
        QueryWrapper<EduVideo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("course_id", courseId);
        queryWrapper2.orderByAsc("sort", "id");
        List<EduVideo> videos = videoService.list(queryWrapper2);

        //封装章节和小节信息
        ArrayList<ChapterVo> chapterVoList = new ArrayList<>();
        for (EduChapter chapter : chapters) { //遍历章节信息
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);
                for (EduVideo video : videos) { //遍历小节信息
                    if (chapter.getId().equals(video.getChapterId())) { //小节中有章节信息
                        VideoVo videoVo = new VideoVo();
                        BeanUtils.copyProperties(video, videoVo);
                        chapterVo.getChildren().add(videoVo);
                    }
                }
            chapterVoList.add(chapterVo);
        }
        return chapterVoList;
    }

    //删除章节情况
    @Override
    public boolean deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> qw = new QueryWrapper<>();
        qw.eq("chapter_id",chapterId);
        int count = videoService.count(qw);
        if(count > 0){
            videoService.remove(qw);
        }
        int result = baseMapper.deleteById(chapterId);
        return result > 0;
    }

    @ApiModelProperty(value = "根据课程id删除章节")
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> qw = new QueryWrapper<>();
        qw.eq("course_id",courseId);
        baseMapper.delete(qw);
    }
}
