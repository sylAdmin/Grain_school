package com.syl.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.print.DocFlavor;

@Data
public class TeacherQuery {

    @ApiModelProperty(value = "教师名称，模糊查询")
    private String name;

    @ApiModelProperty(value = "头衔 1高级讲师 2首席讲师")
    private Integer level;

    @ApiModelProperty(value = "查询开始时间",example = "2022-01-01 13:16:28")
    private String begin;

    @ApiModelProperty(value = "查询结束时间",example = "2022-12-01 13:16:28")
    private String end;
}
