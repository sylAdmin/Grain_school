<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- /课程详情 开始 -->
    <section class="container">
      <section class="path-wrap txtOf hLh30">
        <a href="/" title class="c-999 fsize14">首页</a>
        \
        <a :href="'/course'" title class="c-999 fsize14">课程列表</a>
        \
        <span class="c-333 fsize14">{{courseWebVo.title}}</span>
      </section>
      <div>
        <article class="c-v-pic-wrap" style="height: 357px;">
          <section class="p-h-video-box" id="videoPlay">
            <img :src="courseWebVo.cover" :alt="courseWebVo.title" class="dis c-v-pic">
          </section>
        </article>
        <aside class="c-attr-wrap">
          <section class="ml20 mr15">
            <h2 class="hLh30 txtOf mt15">
              <span class="c-fff fsize24">{{courseWebVo.title}}</span>
            </h2>
            <section class="c-attr-jg" v-if="isbuy">
              <span class="c-fff"></span>
              <b class="c-yellow" style="font-size:24px;">免费</b>
            </section>
            <section class="c-attr-jg" v-else>
              <span class="c-fff">价格：</span>
              <b class="c-yellow" style="font-size:24px;">￥{{courseWebVo.price}}</b>
            </section>
            <section class="c-attr-mt c-attr-undis">
              <span class="c-fff fsize14">主讲： {{courseWebVo.teacherName}}&nbsp;&nbsp;&nbsp;</span>
            </section>
            <section class="c-attr-mt of">
              <span class="ml10 vam">
                <em class="icon18 scIcon"></em>
                <a class="c-fff vam" title="收藏" href="#" >收藏</a>
              </span>
            </section>
            <section class="c-attr-mt" v-if="firstTvSourceId.length == 0">
              <button title="该课程无视频" class="comm-btn c-btn-3">该课程无视频</button>
            </section>
            <section class="c-attr-mt" v-else-if="Number(courseWebVo.price) === 0 || isbuy">
              <a :href="'/player/'+firstTvSourceId" title="立即观看" class="comm-btn c-btn-3" target="_blank">立即观看</a>
            </section>
            <section v-else class="c-attr-mt">
              <a @click.prevent="createOrders()" href="#" title="立即购买" class="comm-btn c-btn-3">立即购买</a>
            </section>
          </section>
        </aside>
        <aside class="thr-attr-box">
          <ol class="thr-attr-ol clearfix">
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">购买数</span>
                <br>
                <h6 class="c-fff f-fM mt10">{{courseWebVo.buyCount}}</h6>
              </aside>
            </li>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">课时数</span>
                <br>
                <h6 class="c-fff f-fM mt10">{{courseWebVo.lessonNum}}</h6>
              </aside>
            </li>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span class="c-fff f-fM">浏览数</span>
                <br>
                <h6 class="c-fff f-fM mt10">{{courseWebVo.viewCount}}</h6>
              </aside>
            </li>
          </ol>
        </aside>
        <div class="clear"></div>
      </div>
      <!-- /课程封面介绍 -->
      <div class="mt20 c-infor-box">
        <article class="fl col-7">
          <section class="mr30">
            <div class="i-box">
              <div>
                <section id="c-i-tabTitle" class="c-infor-tabTitle c-tab-title">
                  <a name="c-i" class="current" title="课程详情">课程详情</a>
                </section>
              </div>
              <article class="ml10 mr10 pt20">
                <div>
                  <h6 class="c-i-content c-infor-title">
                    <span>课程介绍</span>
                  </h6>
                  <div class="course-txt-body-wrap">
                    <section class="course-txt-body">
                      <p v-html="courseWebVo.description">
                      </p>
                    </section>
                  </div>
                </div>
                <!-- /课程介绍 -->
                <div class="mt50">
                  <h6 class="c-g-content c-infor-title">
                    <span>课程大纲</span>
                  </h6>
                  <section class="no-data-wrap" v-if="chapterVideoList.length == 0">
                    <em class="icon30 no-data-ico">&nbsp;</em>
                    <span class="c-666 fsize14 ml10 vam">没有相关数据，小编正在努力整理中...</span>
                  </section>
                  <section class="mt20">
                    <div class="lh-menu-wrap">
                      <menu id="lh-menu" class="lh-menu mt10 mr10">
                        <ul>
                          <!-- 文件目录 -->
                          <li class="lh-menu-stair" v-for="chapter in chapterVideoList" :key="chapter.id">
                            <a href="javascript: void(0)" :title="chapter.title" class="current-1">
                              <em class="lh-menu-i-1 icon18 mr10"></em>{{chapter.title}}
                            </a>
                            <ol class="lh-menu-ol" style="display: block;">
                              <li v-for="video in chapter.children" :key="video.id" class="lh-menu-second ml30">
                                  <a :href="'/player/'+video.videoSourceId" target="_blank" v-if="video.videoSourceId.length != 0">
                                      <span class="fr">
                                          <i class="free-icon vam mr10">免费试听</i>
                                      </span>
                                      <em class="lh-menu-i-2 icon16 mr5">&nbsp;</em>{{ video.title }}
                                  </a>
                              </li>
                            </ol>
                          </li>
                        </ul>
                      </menu>
                    </div>
                  </section>
                </div>
                <!-- /课程大纲 -->
              </article>
            </div>
          </section>
        </article>
        <aside class="fl col-3">
          <div class="i-box">
            <div>
              <section class="c-infor-tabTitle c-tab-title">
                <a title href="javascript:void(0)">主讲讲师</a>
              </section>
              <section class="stud-act-list">
                <ul style="height: auto;">
                  <li>
                    <div class="u-face">
                      <a :href="'/teacher/' + courseWebVo.teacherId">
                        <img :src="courseWebVo.avatar" width="50" height="50" alt>
                      </a>
                    </div>
                    <section class="hLh30 txtOf">
                      <a class="c-333 fsize16 fl" :href="'/teacher/' + courseWebVo.teacherId">{{courseWebVo.teacherName}}</a>
                    </section>
                    <section class="hLh20 txtOf">
                      <span class="c-999">{{courseWebVo.intro}}</span>
                    </section>
                  </li>
                </ul>
              </section>
            </div>
          </div>
        </aside>
        <div class="clear"></div>
      </div>
    </section>
    <!-- /课程详情 结束 -->
    <div class="mt50 commentHtml"><div>
      <h6 class="c-c-content c-infor-title" id="i-art-comment">
        <span class="commentTitle">课程评论</span>
      </h6>
      <section class="lh-bj-list pr mt20 replyhtml">
        <ul>
          <li class="unBr">
            <aside class="noter-pic">
              <img width="50" height="50" class="picImg" src="~/assets/img/avatar-boy.gif" v-if="this.loginInfo.avatar.length === 0">
              <img width="50" height="50" class="picImg" :src="this.loginInfo.avatar" v-if="this.loginInfo.avatar.length > 0">
              </aside>
            <div class="of">
              <section class="n-reply-wrap">
                <fieldset>
                  <textarea name="" v-model="comment.content" placeholder="输入您要评论的文字" id="commentContent"></textarea>
                </fieldset>
                <p class="of mt5 tar pl10 pr10">
                  <span class="fl "><tt class="c-red commentContentmeg" style="display: none;"></tt></span>
                  <input type="button" @click="addComment()" value="回复" class="lh-reply-btn">
                </p>
              </section>
            </div>
          </li>
        </ul>
      </section>
      <section class="">
          <section class="question-list lh-bj-list pr">
            <ul class="pr10">
              <li v-for="(comment) in data.items" :key="comment.id">
                  <aside class="noter-pic">
                    <img width="50" height="50" class="picImg" :src="comment.avatar">
                    </aside>
                  <div class="of">
                    <span class="fl"> 
                    <font class="fsize12 c-blue"> 
                      {{comment.nickname}}</font>
                    <font class="fsize12 c-999 ml5">评论：</font></span>
                  </div>
                  <div class="noter-txt mt5">
                    <p>{{comment.content}}</p>
                  </div>
                  <div class="of mt5">
                    <span class="fr"><font class="fsize12 c-999 ml5">
                      <a v-if="loginInfo.id == comment.ucenterId" href="#" @click.prevent="deleteComment(comment.id)" style="margin-right: 20px;color:red;">删除</a>
                      {{comment.gmtCreate}}</font></span>
                  </div>
                </li>
              
              </ul>
          </section>
        </section>
        
        <!-- 公共分页 开始 -->
        <div class="paging">
            <!-- undisable这个class是否存在，取决于数据属性hasPrevious -->
            <a
            :class="{undisable: !data.hasPrevious}"
            href="#"
            title="首页"
            @click.prevent="gotoPage(1)">首</a>
            <a
            :class="{undisable: !data.hasPrevious}"
            href="#"
            title="前一页"
            @click.prevent="gotoPage(data.current-1)">&lt;</a>
            <a
            v-for="page in data.pages"
            :key="page"
            :class="{current: data.current == page, undisable: data.current == page}"
            :title="'第'+page+'页'"
            href="#"
            @click.prevent="gotoPage(page)">{{ page }}</a>
            <a
            :class="{undisable: !data.hasNext}"
            href="#"
            title="后一页"
            @click.prevent="gotoPage(data.current+1)">&gt;</a>
            <a
            :class="{undisable: !data.hasNext}"
            href="#"
            title="末页"
            @click.prevent="gotoPage(data.pages)">末</a>
            <div class="clear"/>
        </div>
        <!-- 公共分页 结束 -->
      </div>
    </div>
  </div>
</template>

<script>
import courseApi from '@/api/course'
import cookie from "js-cookie";
import order from "@/api/order";
export default {
  asyncData({params, error}) {
    return{
      courseId: params.id
    }
  },
  data(){
    return{
      data:{},
      page:1,
      limit:100,
      total:'',
      comment:{
        content:'',
        courseId:''
      },
      firstTvSourceId:'',
      courseWebVo:{},
      isbuy:false,
      courseId:'',
      courseInfo:{},  
      chapterVideoList:[],
      isbuyCourse:false,
      loginInfo: {
        nickname:'',
        avatar:'',
      }
    }
  },

  created() {
    var userStr = cookie.get("guli_ucenter");
      //将cookie字符串转换成json对象(js对象) 例var str = "{'name':'lucy':'age':20}",去调""号
      if (userStr) {
        this.loginInfo = JSON.parse(userStr);
      }
    this.comment.nickname = this.loginInfo.nickname
    this.comment.avatar = this.loginInfo.avatar
    this.initCourseInfo()
    this.gotoPage(this.page)
  },

  methods: {
    //查询课程详情信息
    initCourseInfo(){ 
      courseApi.getCourseInfo(this.courseId)
      .then(response => {
          this.courseWebVo = response.data.courseWebVo
          this.chapterVideoList = response.data.chapterVideoList
          var chapterList = this.chapterVideoList[0]
          if(chapterList){
            var tvList = chapterList.children[0]
            if(tvList){
              this.firstTvSourceId = tvList.videoSourceId
            }
          }
          this.isbuy = response.data.isBuy
      })
    },

    //生成订单
    createOrders(){
      order.createOrder(this.courseId)
        .then(response => {
          response.data.orderId  //获取返回的订单号
          //生成订单之后，跳转到订单显示页面
          this.$router.push({path:'/orders/' + response.data.orderId})
        })
    },

    //添加评论
    addComment(){
      this.comment.courseId = this.courseWebVo.id
      this.comment.teacherId = this.courseWebVo.teacherId
      courseApi.addComment(this.comment)
        .then(response => {
          this.$message({
            type: "success",
            message: "发表成功!",
          });
          this.comment.content = '',
          this.gotoPage(1)
        })
    },

    //分页查询评论
    gotoPage(page){
      if(page > this.data.pages){
        page = this.data.pages
      } 
      courseApi.getAllComment(this.courseId,page,this.limit)
        .then(response => {
          this.data = response.data.items
        })
      
    },

    //删除评论
    deleteComment(id){
      this.$confirm("此操作将永久删除该评论, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
      .then(() => {
        courseApi.deleteComment(id)
          .then(response => {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            this.gotoPage(1)
          })
      })
    }
  }
  
};
</script>
