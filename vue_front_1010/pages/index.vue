<template>
  <div>
    <title>谷粒学院</title>
    <!-- 幻灯片 开始 -->
    <div v-swiper:mySwiper="swiperOption">
      <div class="swiper-wrapper">
        <div
          v-for="banner in bannerList"
          :key="banner.id"
          class="swiper-slide"
          style="background: #040b1b"
        >
          <a target="_blank" :href="banner.linkUrl">
            <img :src="banner.imageUrl" :alt="banner.title" />
          </a>
        </div>
      </div>
      <div class="swiper-pagination swiper-pagination-white"></div>
      <div
        class="swiper-button-prev swiper-button-white"
        slot="button-prev"
      ></div>
      <div
        class="swiper-button-next swiper-button-white"
        slot="button-next"
      ></div>
    </div>
    <hr
      style="height:5px;border:none;border-top:5px groove #68cb9b;skyblue #68cb9b"
    />
    <!-- 幻灯片 结束 -->
    <div id="aCoursesList">
      <!-- 网校课程 开始 -->
      <div>
        <section class="container">
          <header class="comm-title">
            <h2 class="tac">
              <span class="c-333">热门课程</span>
            </h2>
          </header>
          <div>
            <article class="comm-course-list">
              <ul class="of" id="bna">
                <li v-for="course in eduList" :key="course.id">
                  <div style="background-color: #eeee" class="cc-l-wrap">
                    <section class="course-img">
                      <img
                        :src="course.cover"
                        class="img-responsive"
                        :alt="course.title"
                      />
                      <div class="cc-mask">
                        <a :href="'/course/' + course.id" title="开始学习" class="comm-btn c-btn-1"
                          >开始学习</a
                        >
                      </div>
                    </section>
                    <h3 class="hLh30 txtOf mt10">
                      <a
                        href="#"
                        :title="course.title"
                        class="course-title fsize18 c-333"
                        >{{ course.title }}</a
                      >
                    </h3>
                    <section class="mt10 hLh20 of">
                      <span class="fl jgAttr c-ccc f-fA">
                        <i class="c-999 f-fA"
                          >浏览数量:{{ course.viewCount }}</i
                        >
                        |
                        <i class="c-999 f-fA">9634评论</i>
                      </span>
                    </section>
                  </div>
                </li>
              </ul>
              <div class="clear"></div>
            </article>
            <section class="tac pt20">
              <a href="#" title="全部课程" @click="getAllCourse" class="comm-btn c-btn-2">全部课程</a>
            </section>
          </div>
        </section>
      </div>
      <!-- /网校课程 结束 -->
      <!-- 网校名师 开始 -->
      <div>
        <section class="container">
          <header class="comm-title">
            <h2 class="tac">
              <span class="c-333">名师大咖</span>
            </h2>
          </header>
          <div>
            <article class="i-teacher-list">
              <ul class="of">
                <li v-for="teacher in teacherList" :key="teacher.id">
                  <section class="i-teach-wrap">
                    <div class="i-teach-pic">
                      <a :href="'/teacher/' + teacher.id" :title="teacher.name">
                        <img :alt="teacher.name" :src="teacher.avatar" />
                      </a>
                    </div>
                    <div class="mt10 hLh30 txtOf tac">
                      <a
                        :href="'/teacher/' + teacher.id"
                        :title="teacher.name"
                        class="fsize18 c-666"
                        >{{ teacher.name }}</a
                      >
                    </div>
                    <div class="hLh30 txtOf tac">
                      <span class="fsize14 c-999">{{ teacher.career }}</span>
                    </div>
                    <div class="mt15 i-q-txt">
                      <p class="c-999 f-fA">
                        {{ teacher.intro }}
                      </p>
                    </div>
                  </section>
                </li>
              </ul>
              <div class="clear"></div>
            </article>
            <section class="tac pt20">
              <a href="#" title="全部讲师" class="comm-btn c-btn-2">全部讲师</a>
            </section>
          </div>
        </section>
      </div>
      <!-- /网校名师 结束 -->
    </div>
    <link
      href="https://cdn.jsdelivr.net/npm/aplayer/dist/APlayer.min.css"
      rel="stylesheet"
    />
    <script src="https://cdn.jsdelivr.net/npm/aplayer/dist/APlayer.min.js"></script>
    <!-- require MetingJS -->
    <script src="https://cdn.jsdelivr.net/npm/meting@2/dist/Meting.min.js"></script>
    <!--     樱花特效-->
    <script src="https://cdn.jsdelivr.net/gh/yremp/yremp-js@1.5/sakura.js"></script>

  </div>
</template>

<script>
import banner from "@/api/banner";
import index from "@/api/index";
export default {
  data() {
    return {
      swiperOption: {
        //配置分页
        pagination: {
          el: ".swiper-pagination", //分页的dom节点
        },
        //配置导航
        navigation: {
          nextEl: ".swiper-button-next", //下一页dom节点
          prevEl: ".swiper-button-prev", //前一页dom节点
        },
      },
      eduList: [], //热门课程
      teacherList: [], //热门讲师
      bannerList: [], //幻灯片
    };
  },
  created() {
    
  },
  mounted() {
    //调用查询banner的方法
    this.getBannerList();
    //调用热门课程和热门讲师
    this.getHotCourseTeacher();
  },
  methods: {
    //查询热门课程和热门讲师
    getHotCourseTeacher() {
      index.getIndexData().then((response) => {
        this.eduList = response.data.eduCourseList;
        this.teacherList = response.data.eduTeacherList;
      });
    },
    //查询banner数据
    getBannerList() {
      banner.getListBanner().then((response) => {
        this.bannerList = response.data.list;
      });
    },

    getAllCourse(){
      index.getAllCourse()
        .then(response => {
          this.eduList = response.data.courseList
        })
    }
  },
};
</script>
