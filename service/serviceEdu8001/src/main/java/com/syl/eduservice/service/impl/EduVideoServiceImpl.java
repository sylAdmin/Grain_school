package com.syl.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syl.eduservice.client.VodClient;
import com.syl.eduservice.entity.EduVideo;
import com.syl.eduservice.mapper.EduVideoMapper;
import com.syl.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.hssf.record.DVALRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClient vodClient;

    @ApiModelProperty(value = "删除课时和视频")
    @Override
    public void removeVideoByCourseId(String courseId) {
        //根据课程id查询所有的视频id
        QueryWrapper<EduVideo> qw = new QueryWrapper<>();
        qw.eq("course_id", courseId);
        qw.select("video_source_id"); //查询指定的列
        List<EduVideo> eduVideoList = baseMapper.selectList(qw);
        //把List<EduVideo> 变成 List<String>
        List<String> videoId = new ArrayList<String>();
        for (EduVideo eduVideo : eduVideoList) {
            String sourceId = eduVideo.getVideoSourceId();
            if (!StringUtils.isEmpty(sourceId)) {
                videoId.add(sourceId);
            }
        }
        if (videoId.size() > 0) {
            vodClient.deleteBatch(videoId);  //删除视频
        }
        qw = new QueryWrapper<>();
        qw.eq("course_id", courseId);
        baseMapper.delete(qw); //删除课时
    }
}
