import request from '@/utils/request'

export default {

     //3、修改小节
    updateval(video){
        return request({
            url: `/eduservice/video/updateVideo`,
            method: 'post',
            data: video
        })
    }

}