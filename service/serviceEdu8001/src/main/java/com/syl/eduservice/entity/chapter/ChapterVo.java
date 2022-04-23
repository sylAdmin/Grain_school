package com.syl.eduservice.entity.chapter;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 章节
 */
@Data
@ToString
public class ChapterVo {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
    private String title;

    //表示小节
    private List<VideoVo> children = new ArrayList<>();
}
