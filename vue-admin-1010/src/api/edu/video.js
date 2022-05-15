import request from '@/utils/request'

export default {

    //1、添加小节
    addVideo(video){
        return request({
            url: `/eduservice/video/addVideo`,
            method: 'post',
            data: video
        })
    },

    //2、根据id查询小节
    getVideo(id){
        return request({
            url: `/eduservice/video/getOneVideo/${id}`,
            method: 'get',
        })
    },

    //4、删除章节
    deleteVideo(id){
        return request({
            url: `/eduservice/video/${id}`,
            method: 'delete',
        })
    },

    //5、删除视频
    deleteAlyVideo(id){
        return request({
            url: `/eduvod/video/removeVideo/${id}`,
            method: 'delete',
        })
    },

}