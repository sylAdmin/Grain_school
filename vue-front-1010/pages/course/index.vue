<template>
  <div id="aCoursesList" class="bg-fa of">
    <!-- /课程列表 开始 -->
    <section class="container">
      <header class="comm-title">
        <h2 class="fl tac">
          <span class="c-333">全部课程</span>
        </h2>
      </header>
      <section class="c-sort-box">
        <section class="c-s-dl">
          <dl>
            <dt>
              <span class="c-999 fsize14">课程类别</span>
            </dt>
            <dd class="c-s-dl-li">
              <ul class="clearfix">
                <li>
                  <a
                    title="全部"
                    href="#"
                    @click="gotoPage(100)"
                    :class="{ active: oneIndex == -2 }"
                    >全部</a
                  >
                </li>
                <!-- 一级分类 -->
                <li
                  v-for="(item, index) in subjectNestedList"
                  :key="index"
                  :class="{ active: oneIndex == index }"
                >
                  <a
                    :title="item.title"
                    @click="searchOne(item.id, index)"
                    href="#"
                    >{{ item.title }}</a
                  >
                </li>
              </ul>
            </dd>
          </dl>
          <dl>
            <dt>
              <span class="c-999 fsize14"></span>
            </dt>
            <dd class="c-s-dl-li">
              <ul class="clearfix">
                <!-- 二级分类 -->
                <li
                  v-for="(item, index) in subjectList"
                  :key="index"
                  :class="{ active: twoIndex == index }"
                >
                  <a
                    title="item.title"
                    href="#"
                    @click="searchTwo(item.id, index)"
                    >{{ item.title }}</a
                  >
                </li>
              </ul>
            </dd>
          </dl>
          <div class="clear"></div>
        </section>
        <div class="js-wrap">
          <section class="fr">
            <span class="c-ccc">
              <i class="c-master f-fM">1</i>/
              <i class="c-666 f-fM">1</i>
            </span>
          </section>
          <section class="fl">
            <ol class="js-tap clearfix">
              <li :class="{ 'current bg-orange': viewCount != '' }">
                <a
                  title="销量"
                  href="javascript:void(0);"
                  @click="searchBuyCount()"
                  >销量
                  <span :class="{ hide: viewCount == '' }">↓</span>
                </a>
              </li>
              <li :class="{ 'current bg-orange': gmtModified != '' }">
                <a
                  title="最新"
                  href="javascript:void(0);"
                  @click="searchGmtCreate()"
                  >最新
                  <span :class="{ hide: gmtModified == '' }">↓</span>
                </a>
              </li>
              <li :class="{ 'current bg-orange': priceSort != '' }">
                <a
                  title="价格"
                  href="javascript:void(0);"
                  @click="searchPrice()"
                  >价格&nbsp;
                  <span :class="{ hide: priceSort == '' }">↓</span>
                </a>
              </li>
            </ol>
          </section>
        </div>
        <div class="mt40">
          <!-- /无数据提示 开始-->
          <section class="no-data-wrap" v-if="data.total == 0">
            <em class="icon30 no-data-ico">&nbsp;</em>
            <span class="c-666 fsize14 ml10 vam"
              >没有相关数据，小编正在努力整理中...</span
            >
          </section>
          <!-- /无数据提示 结束-->
          <article class="comm-course-list" v-if="data.total > 0">
            <ul class="of" id="bna">
              <li v-for="item in data.items" :key="item.id">
                <div class="cc-l-wrap">
                  <section class="course-img">
                    <img
                      :src="item.cover"
                      class="img-responsive"
                      :alt="item.title"
                    />
                    <div class="cc-mask">
                      <a
                        :href="'/course/' + item.id"
                        title="开始学习"
                        class="comm-btn c-btn-1"
                        >开始学习</a
                      >
                    </div>
                  </section>
                  <h3 class="hLh30 txtOf mt10">
                    <a
                      :href="'/course/' + item.id"
                      :title="item.title"
                      class="course-title fsize18 c-333"
                      >{{ item.title }}</a
                    >
                  </h3>
                  <section class="mt10 hLh20 of">
                    <span class="fr jgTag bg-green" v-if="Number(item.price == 0)">
                      <i class="c-fff fsize12 f-fA">免费</i>
                    </span>
                    <span class="fr jgTag bg-green" v-else>
                      <i class="c-ggg fsize12 f-fA">{{item.price + "元" }}</i>
                    </span>
                    <span class="fl jgAttr c-ccc f-fA">
                      <i class="c-999 f-fA">{{ item.viewCount }}浏览数</i>
                      |
                      <i class="c-999 f-fA">9634评论</i>
                    </span>
                  </section>
                </div>
              </li>
            </ul>
            <div class="clear"></div>
          </article>
        </div>
        <div>
          <div class="paging">
            <!-- undisable这个class是否存在，取决于数据属性hasPrevious -->
            <a
              :class="{ undisable: !data.hasPrevious }"
              href="#"
              title="首页"
              @click.prevent="gotoPage(1)"
              >首</a
            >
            <a
              :class="{ undisable: !data.hasPrevious }"
              href="#"
              title="前一页"
              @click.prevent="gotoPage(data.current - 1)"
              >&lt;</a
            >
            <a
              v-for="page in data.pages"
              :key="page"
              :class="{
                current: data.current == page,
                undisable: data.current == page,
              }"
              :title="'第' + page + '页'"
              href="#"
              @click.prevent="gotoPage(page)"
              >{{ page }}</a
            >
            <a
              :class="{ undisable: !data.hasNext }"
              href="#"
              title="后一页"
              @click.prevent="gotoPage(data.current + 1)"
              >&gt;</a
            >
            <a
              :class="{ undisable: !data.hasNext }"
              href="#"
              title="末页"
              @click.prevent="gotoPage(data.pages)"
              >末</a
            >
            <div class="clear" />
          </div>
        </div>
      </section>
    </section>
    <!-- /课程列表 结束 -->
  </div>
</template>
<script>
import courseApi from "@/api/course";
export default {
  data() {
    return {
      page: 1, //当前页
      data: {}, //课程列表
      subjectNestedList: [], // 一级分类列表
      subjectList: [], // 二级分类列表
      searchObj: {}, // 查询表单对象

      oneIndex: -1,
      twoIndex: -1,
      viewCount: "",
      gmtModified: "",
      priceSort: "",
    };
  },
  created() {
    this.oneIndex = -2;
    this.viewCount = 1
    //课程第一次查询
    this.initCourseFirst();
    //查询所有一级分类和二级分类
    this.initSubject();
  },
  methods: {

    //查询第一页数据
    initCourseFirst() {
      courseApi.getCourseList(1, 8, this.searchObj).then((response) => {
        this.data = response.data;
      });
    },

    //查询所有一级分类
    initSubject() {
      courseApi.getAllSubject().then((response) => {
        this.subjectNestedList = response.data.list;
      });
    },

    //分页切换的方法
    gotoPage(page) {
      //查询全部的方法
      if (page == 100) {
        this.gmtModified = "";
        this.priceSort = "",
        this.viewCount = 1
        this.twoIndex= -1
        this.oneIndex = -2;
        page = 1;
        this.searchObj = {};
        this.subjectList = {}
      }

      if (page > this.data.pages) {
        page = this.data.pages;
      }
      courseApi.getCourseList(page, 8, this.searchObj).then((response) => {
        this.data = response.data;
      });
    },

    //点击一级分类查询二级分类并分页显示
    searchOne(subjectParentId, index) {
      //让样式生效
      this.oneIndex = index;
      //将二级分类id和集合清空
      this.searchObj.subjectId = "";
      this.subjectList = [];
      this.searchObj.subjectParentId = subjectParentId;
      this.gotoPage(1);
      this.subjectList = this.subjectNestedList[index].children;
      this.twoIndex = -1;
    },

    //点击某个二级分类实现查询
    searchTwo(subjectId, index) {
      //让样式生效
      this.twoIndex = index;
      this.searchObj.subjectId = subjectId;
      this.gotoPage(1);
      this.searchObj.subjectId = "";
    },

    //根据销量排序
    searchBuyCount(){
      this.oneIndex = -1
      this.twoIndex = -1
      this.viewCount = "1",
      this.gmtModified = "",
      this.priceSort = ""
      this.searchObj.viewCount = this.viewCount
      this.searchObj.gmtModified = this.gmtModified;
      this.searchObj.priceSort = this.priceSort;
      this.gotoPage(1)
    },

    //更新时间查询
    searchGmtCreate() {
      this.oneIndex = -1
      this.twoIndex = -1
      this.viewCount = "";
      this.gmtModified = "1";
      this.priceSort = "";
      this.searchObj.viewCount = this.viewCount;
      this.searchObj.gmtModified = this.gmtModified;
      this.searchObj.priceSort = this.priceSort;
      this.gotoPage(1)
    },

    //价格查询
    searchPrice() {
      this.oneIndex = -1
      this.twoIndex = -1
      this.viewCount = "";
      this.gmtModified = "";
      this.priceSort = "1";
      this.searchObj.viewCount = this.viewCount;
      this.searchObj.gmtModified = this.gmtModified;
      this.searchObj.priceSort = this.priceSort;
      this.gotoPage(1)
    },
  },
};
</script>
<style scoped>
.active {
  background: #68cb9b;
}
.hide {
  display: none;
}
.show {
  display: block;
}
</style>
