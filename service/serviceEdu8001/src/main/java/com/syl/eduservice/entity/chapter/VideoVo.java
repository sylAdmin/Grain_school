package com.syl.eduservice.entity.chapter;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class VideoVo {

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
    private String title;
    private String chapterId;
    @ApiModelProperty(value = "云端视频资源")
    private String videoSourceId;
}
