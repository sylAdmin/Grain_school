package com.syl.eduservice.mapper;

import com.syl.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 讲师 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2022-03-26
 */
@Repository
@Mapper
public interface EduTeacherMapper extends BaseMapper<EduTeacher> {

}
