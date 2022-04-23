package com.syl.eduservice.controller;


import com.syl.commonutils.R;
import com.syl.eduservice.entity.EduSubject;
import com.syl.eduservice.entity.subject.OneSubject;
import com.syl.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-03-31
 */
@RestController
@RequestMapping("/eduservice/subject")
@Api(description = "课程列表")
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    //获取上传过来的文件，把文件内容取出来
    @ApiOperation(value = "添加课程分类")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        //上传过来excel文件
        eduSubjectService.saveSubject(file,this.eduSubjectService);
        return R.ok();
    }

    @ApiOperation(value = "课程分类列表(树形)")
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        //list集合泛型是一级分类
        List<OneSubject> list = eduSubjectService.getAllOneTwoSubject();
        return R.ok().data("list",list);
    }

    @ApiOperation(value = "递归实现课程分类列表(树形)")
    @GetMapping("recursive/getAllSubject")
    public R recursive(){
        //list集合泛型是一级分类
        List<EduSubject> list = eduSubjectService.recursiveSelectSubject();
        return R.ok().data("list",list);
    }

}

