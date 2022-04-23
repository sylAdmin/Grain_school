package com.syl.eduservice.entity.subject;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 一级分类
 */
@Data
public class OneSubject {

    private String id;
    private String title;
    @TableField(exist = false)
    private Integer level;

    private List<OneSubject> childrens;

    //一个一级分类有多个二级分类
    private List<TwoSubject> children = new ArrayList<>();

    public void add(TwoSubject twoSubject){
        children.add(twoSubject);
    }
}
