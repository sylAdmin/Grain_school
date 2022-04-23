package com.syl.eduservice.controller;


import com.syl.commonutils.R;
import com.syl.eduservice.entity.EduChapter;
import com.syl.eduservice.entity.chapter.ChapterVo;
import com.syl.eduservice.service.EduChapterService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 * @since 2022-04-01
 */
@RestController
@RequestMapping("/eduservice/chapter")

public class    EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    @ApiModelProperty(value = "课程大纲列表")
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){
        List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("list",list);
    }

    @ApiModelProperty(value = "添加章节")
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return R.ok();
    }

    @ApiModelProperty(value = "根据章节id查询")
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId){
        EduChapter eduChapter = chapterService.getById(chapterId);
        return R.ok().data("chapter",eduChapter);
    }

    @ApiModelProperty(value = "修改章节")
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    @ApiModelProperty(value = "删除章节")
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){
        boolean flag = chapterService.deleteChapter(chapterId);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

}

