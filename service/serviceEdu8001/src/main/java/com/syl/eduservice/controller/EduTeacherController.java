package com.syl.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syl.commonutils.R;
import com.syl.eduservice.entity.EduTeacher;
import com.syl.eduservice.entity.vo.TeacherQuery;
import com.syl.eduservice.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-03-26
 */
@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/teacher")

public class EduTeacherController {

    //访问地址: http://localhost:8001/eduservice/teacher/findAll
    @Autowired
    private EduTeacherService eduTeacherService;

    //1、查询讲师表所有数据
    @ApiOperation(value = "所有讲师列表")
    @GetMapping("findAll")
    public R findAllTeacher(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items",list);
    }

    //2、逻辑删除讲师
    @ApiOperation(value = "逻辑删除讲师")
    @DeleteMapping("delete/{id}")
    public R removeTeacher(@ApiParam(name = "id",value = "讲师ID",required = true)
                                     @PathVariable("id") Long id){
        boolean flag = eduTeacherService.removeById(id);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //3、分页查询讲师
    @ApiOperation(value = "分页查询讲师")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable("current") Long current,
                             @PathVariable("limit") Long limit){
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        eduTeacherService.page(pageTeacher,null);
        long total = pageTeacher.getTotal();
        List<EduTeacher> records = pageTeacher.getRecords();
        HashMap<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",records);
        return R.ok().data(map);
    }

    //4、条件查询 + 分页方法
    @ApiOperation(value = "条件查询 + 分页方法")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable("current") Long current,
                                  @PathVariable("limit") Long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery){
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        //添加条件查询
        QueryWrapper<EduTeacher> qw = likeSelected(teacherQuery);
        qw.orderByDesc("gmt_modified");
        //进行条件分页
        eduTeacherService.page(pageTeacher,qw);
        long total = pageTeacher.getTotal(); //总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); //分页数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("total",total);
        map.put("rows",records);
        return R.ok().data(map);
    }

    //5、添加讲师接口方法
    @ApiOperation(value = "添加讲师接口方法")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        //设置默认头像
        eduTeacher.setAvatar("https://grain-school.oss-cn-hangzhou.aliyuncs.com/picture/2022/04/14/dd1c5d66b4ac4882869ba1294a3aca08.png");
        boolean save = eduTeacherService.save(eduTeacher);
        if(save){
            return R.ok();
        }else{
            return R.error();
        }
    }

    //6、根据讲师id进行查询
    @ApiOperation(value = "根据讲师id进行查询")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable("id") Long id){
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    //7、讲师修改功能
    @ApiOperation(value = "讲师修改功能")
    @PutMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean flag = eduTeacherService.updateById(eduTeacher);
        if(flag){
            return R.ok();
        }else {
            return R.error();
        }
    }

    //模糊查询条件封装
    public QueryWrapper<EduTeacher> likeSelected(TeacherQuery teacherQuery){
        QueryWrapper<EduTeacher> qw = new QueryWrapper<>();
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件是否为空,如果不为空拼接条件
        if(!StringUtils.isEmpty(name)){
            qw.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            qw.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            qw.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            qw.le("gmt_modified",end);
        }
        return qw;
    }

}

