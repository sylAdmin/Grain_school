package com.syl.eduservice.service;

import com.syl.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.syl.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-03-31
 */
public interface EduSubjectService extends IService<EduSubject> {

    /**
     * 添加课程分类
     * @param file
     */
    void saveSubject(MultipartFile file,EduSubjectService eduSubjectService);

    List<OneSubject> getAllOneTwoSubject();

    List<EduSubject> recursiveSelectSubject();
}
