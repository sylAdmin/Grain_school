package com.syl.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syl.eduservice.entity.EduSubject;
import com.syl.eduservice.entity.excel.SubjectData;
import com.syl.eduservice.entity.subject.OneSubject;
import com.syl.eduservice.entity.subject.TwoSubject;
import com.syl.eduservice.listener.SubjectEasyListener;
import com.syl.eduservice.mapper.EduSubjectMapper;
import com.syl.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-03-31
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {
        try {
            InputStream inputStream = file.getInputStream();
            EasyExcel.read(inputStream, SubjectData.class, new SubjectEasyListener(eduSubjectService)).sheet().doRead();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //查询一级分类名称
        QueryWrapper<EduSubject> qw = new QueryWrapper<>();
        qw.eq("parent_id", 1L);
        List<EduSubject> oneSubjectList = baseMapper.selectList(qw);
        //查询二级分类名称
        qw = new QueryWrapper<>();
        qw.ne("parent_id", 1L);
        List<EduSubject> twoSubjectList = baseMapper.selectList(qw);
        //一级分类和二级分类树化
        return branch(oneSubjectList,twoSubjectList);
    }

    /**
     * 一级分类和二级分类树杈化
     */
    public List<OneSubject> branch(List<EduSubject> oneSubjectList,List<EduSubject> twoSubjectList){
        List<OneSubject> osl = new ArrayList();
        for (EduSubject oneList : oneSubjectList) {
            //每次需要new一个OneSubject，用来在同一级树杈中存储不同的二级树杈
            OneSubject oneSubject = new OneSubject();
            oneSubject.setId(oneList.getId());
            oneSubject.setTitle(oneList.getTitle());

            for (EduSubject twoList : twoSubjectList) {
                //判断如果一级分类的id等于二级分类的parent_id就视为是同一级树叉
                if ((oneList.getId()).equals(twoList.getParentId())) {
                    TwoSubject twoSubject = new TwoSubject();
                    twoSubject.setId(twoList.getId());
                    twoSubject.setTitle(twoList.getTitle());
                    twoSubject.setParentId(twoList.getParentId());
                    //每个一级树杈中有多个二级树杈，用集合存储
                    oneSubject.add(twoSubject);
                }
            }
            //有几个一级的课程就有几个树杈，
            osl.add(oneSubject);
        }
        return osl;
    }

    /**
     * 递归查询所有分类
     * @return
     */
    @Override
    public List<EduSubject> recursiveSelectSubject() {
        List<EduSubject> eduSubjectList = baseMapper.selectList(null);
        //把查询所有分类进行封装
        List<EduSubject> resultList = buildSubject(eduSubjectList);
        return resultList;
    }

    private List<EduSubject> buildSubject(List<EduSubject> eduSubjectList) {
        ArrayList<EduSubject> oneSubjects = new ArrayList<>();
        EduSubject eduSubject = baseMapper.selectById(1);
        eduSubject.setLevel(1);
        oneSubjects.add(recursive(eduSubject,eduSubjectList));
        return oneSubjects;
    }

    private EduSubject recursive(EduSubject eduSubject, List<EduSubject> eduSubjectList) {
        eduSubject.setChildren(new ArrayList<EduSubject>());
        for (EduSubject subject : eduSubjectList) {
            if(eduSubject.getId().equals(subject.getParentId())){
                subject.setLevel(eduSubject.getLevel() + 1);
                eduSubject.getChildren().add(recursive(subject,eduSubjectList));
            }
        }
        return eduSubject;
    }


}
