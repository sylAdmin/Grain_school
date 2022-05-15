<template>
  <div>
    <!-- 阿里云视频播放器样式 -->
    <link rel="stylesheet" href="https://g.alicdn.com/de/prismplayer/2.8.1/skins/default/aliplayer-min.css" >
    <!-- 阿里云视频播放器脚本 -->
    <script charset="utf-8" type="text/javascript" src="https://g.alicdn.com/de/prismplayer/2.8.1/aliplayer-min.js" />
    <script type="text/javascript" charset="utf-8" src="/aliplayercomponents-1.0.8.min.js"></script>
    <!-- 定义播放器dom -->
    <div id="J_prismPlayer" class="prism-player" />
  </div>
</template>
<script>
import vod from '@/api/vod'
export default {
    
  layout: 'video',//应用video布局
  asyncData({ params, error }) {
    return vod.getPlayAuth(params.vid).then(response => {
      return {
        vid: params.vid,
        playURL: response.data.playURL
      }
    })
  },
  //在页面渲染完成之后在执行，防止数据取不到
  mounted() {
    new Aliplayer({
        id: 'J_prismPlayer',
        width: '100%',
        autoplay: false,
        height:'650px',
        source : this.playURL,    
        qualitySort: 'asc', // 清晰度排序
        mediaType: 'video', // 返回音频还是视频
        autoplay: false, // 自动播放
        isLive: false, // 直播
        rePlay: false, // 循环播放
        preload: true,
        controlBarVisibility: 'hover', // 控制条的显示方式：鼠标悬停
        useH5Prism: true, // 播放器类型：html5
    },function(player){
        console.log('播放器创建好了。')
    });
  },
}
</script>