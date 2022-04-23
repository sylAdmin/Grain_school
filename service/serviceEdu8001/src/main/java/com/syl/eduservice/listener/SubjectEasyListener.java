package com.syl.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syl.eduservice.entity.EduSubject;
import com.syl.eduservice.entity.excel.SubjectData;
import com.syl.eduservice.service.EduSubjectService;
import com.syl.servicebase.exception.GuLiException;

/**
 * 监听器负责读取excel表格中的内容，并写入数据库
 */
//不能交给spring管理,因为spring注入在监听器之后 Listener --> Filter --> Servlet --> Spring注入
public class SubjectEasyListener extends AnalysisEventListener<SubjectData> {

    //因为SubjectEasyListener不能交给Spring进行管理，需要自己new，不能注入其他对象
    //不能实现数据库操作
    public EduSubjectService eduSubjectService;
    public SubjectEasyListener() {}
    public SubjectEasyListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    //读取excel内容，一行一行进行读取
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if(subjectData == null){
            throw new GuLiException(20001,"文件数据为空");
        }

        //一行一行读取，每次读取两个值，第一个值一级分类，第二个值二级分类
        //判断一级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(eduSubjectService, subjectData.getOneSubjectName());
        if(existOneSubject == null){ //没有相同一级分类，进行添加
            existOneSubject = new EduSubject();
            existOneSubject.setTitle(subjectData.getOneSubjectName());
            existOneSubject.setParentId("0");
            eduSubjectService.save(existOneSubject);
        }

        //获取一级分类id值
        String id = existOneSubject.getId();

        //添加二级分类
        //判断二级分类是否重复
        EduSubject existTwoSubject = this.existTwoSubject(eduSubjectService, subjectData.getTwoSubjectName(), id);
        if(existTwoSubject == null){ //没有相同一级分类，进行添加
            existTwoSubject = new EduSubject();
            existTwoSubject.setTitle(subjectData.getOneSubjectName());
            existTwoSubject.setParentId(id);
            eduSubjectService.save(existTwoSubject);
        }
    }

    //读取完之后
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {}

    //判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService eduSubjectService,String name){
        QueryWrapper<EduSubject> qw = new QueryWrapper<>();
        qw.eq("title",name);
        qw.eq("parent_id",0);
        EduSubject oneSubject = eduSubjectService.getOne(qw);
        return oneSubject;
    }

    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService eduSubjectService,String name,String id){
        QueryWrapper<EduSubject> qw = new QueryWrapper<>();
        qw.eq("title",name);
        qw.eq("parent_id",id);
        EduSubject twoSubject = eduSubjectService.getOne(qw);
        return twoSubject;
    }
}
