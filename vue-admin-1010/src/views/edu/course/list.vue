//课程列表
<template>
  <!-- 表格 -->
  <div>
    <br />
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <!-- 所属分类：级联下拉列表 -->
      <!-- 一级分类 -->&nbsp;&nbsp;&nbsp;&nbsp;
      <el-form-item label="课程类别">
        <el-select
          v-model="searchObj.subjectParentId"
          placeholder="请选择"
          @change="subjectLevelOneChanged"
        >
          <el-option
            v-for="subject in subjectNestedList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"
          />
        </el-select>

        <!-- 二级分类 -->
        <el-select v-model="searchObj.subjectId" placeholder="请选择">
          <el-option
            v-for="subject in subSubjectList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"
          />
        </el-select>
      </el-form-item>

      <!-- 标题 -->
      <el-form-item>
        <el-input v-model="searchObj.title" placeholder="课程标题" />
      </el-form-item>
      <!-- 讲师 -->
      <el-form-item>
        <el-select v-model="searchObj.teacherId" placeholder="请选择讲师">
          <el-option
            v-for="teacher in teacherList"
            :key="teacher.id"
            :label="teacher.name"
            :value="teacher.id"
          />
        </el-select>
      </el-form-item>
      <!-- 是否已经发布 -->
      <el-form-item>
        <el-select v-model="searchObj.status" placeholder="发布状态">
          <el-option key="Normal" label="已发布" value="Normal" />
          <el-option key="Draft" label="未发布" value="Draft" />
        </el-select>
      </el-form-item>
      <el-button
        type="primary"
        icon="el-icon-search"
        @click="fetchData()"
        style="margin-left: 26cm"
        size="small"
        >查询</el-button
      >&nbsp;&nbsp;
      <el-button type="default" @click="resetData()" size="small"
        >清空</el-button
      >
    </el-form>
    <br />
    <el-table
      v-loading="listLoading"
      :data="this.list"
      element-loading-text="数据加载中"
      border
      fit
      highlight-current-row
      row-class-name="myClassList"
    >
      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="课程信息" width="470" align="center">
        <template slot-scope="scope">
          <div class="info">
            <div class="pic">
              <img :src="scope.row.cover" alt="scope.row.title" width="150px" />
            </div>
            <div class="title">
              <a href="">{{ scope.row.title }}</a>
              <h4>{{ scope.row.lessonNum }}课时</h4>
              <p>{{ scope.row.status === "Normal" ? "已发布" : "未发布" }}</p>
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="价格" width="100" align="center">
        <template slot-scope="scope">
          {{
            Number(scope.row.price) === 0
              ? "免费"
              : "¥" + scope.row.price.toFixed(2)
          }}
        </template>
      </el-table-column>
      <el-table-column
        prop="buyCount"
        label="付费学员"
        width="100"
        align="center"
      >
        <template slot-scope="scope"> {{ scope.row.buyCount }}人 </template>
      </el-table-column>

      <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.gmtCreate.substr(0, 10) }}
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.gmtModified.substr(0, 10) }}
        </template>
      </el-table-column>

      <el-table-column
        prop="viewCount"
        label="浏览数量"
        width="80"
        align="center"
      >
        <template slot-scope="scope"> {{ scope.row.viewCount }}人 </template>
      </el-table-column>

      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <router-link :to="'/course/info/' + scope.row.id">
            <el-button type="text" size="mini" icon="el-icon-edit"
            >编辑课程信息</el-button>
          </router-link>
          <router-link :to="'/course/chapter/' + scope.row.id">
            <el-button type="text" size="mini" icon="el-icon-edit"
              >编辑课程大纲</el-button
            >
          </router-link>
          <el-button
            type="text"
            size="mini"
            icon="el-icon-delete"
            @click="deleteCourseById(scope.row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="page"
      :page-size="limit"
      :total="total"
      style="padding: 30px 0; text-align: center"
      layout="total, prev, pager, next, jumper"
      @current-change="fetchData"
    />
  </div>
</template>

<script>
import course from "@/api/edu/course";
import teacher from "@/api/edu/teacher";
import subject from "@/api/edu/subject";

export default {
  data() {
    return {
      listLoading: true, // 是否显示loading信息
      list: null, // 数据列表
      total: 0, // 总记录数
      page: 1, // 页码
      limit: 4, // 每页记录数
      searchObj: {
        subjectParentId: "",
        subjectId: "",
        title: "",
        teacherId: "",
      }, // 查询条件
      teacherList: [], // 讲师列表
      subjectNestedList: [], // 一级分类列表
      subSubjectList: [], // 二级分类列表,
      courseList: [] //课程信息列表
    };
  },

  created() {
    // 当页面加载时获取数据
    this.fetchData();
    // 初始化分类列表
    this.initSubjectList();
    // 获取讲师列表
    this.initTeacherList();
  },

  methods: {

    //删除课程
    deleteCourseById(id) {
      this.$confirm("此操作将永久删除课程及课程基本信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          //点击确定执行then方法
          course.deleteCourse(id).then((response) => {
            this.$message({
              type: "success",
              message: "删除成功!",
            });
            //刷新页面
            this.fetchData();
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },

    fetchData(page = 1) {
      // 调用api层获取数据库中的数据
      console.log("加载列表");
      // 当点击分页组件的切换按钮的时候，会传输一个当前页码的参数page
      // 解决分页无效问题
      this.page = page;
      this.listLoading = true;
      course
        .getPageList(this.page, this.limit, this.searchObj)
        .then((response) => {
          // debugger 设置断点调试
          if (response.success === true) {
            this.list = response.data.records;
            this.total = response.data.total;
          }
          this.listLoading = false;
        });
    },

    initTeacherList() {
      course.getListTeacher().then((response) => {
        this.teacherList = response.data.items;
      });
    },

    initSubjectList() {
      subject.getSubjectList().then((response) => {
        this.subjectNestedList = response.data.list;
      });
    },

    subjectLevelOneChanged(value) {
      for (let i = 0; i < this.subjectNestedList.length; i++) {
        if (this.subjectNestedList[i].id === value) {
          this.subSubjectList = this.subjectNestedList[i].children;
          this.searchObj.subjectId = "";
        }
      }
    },

    resetData() {
      this.searchObj = {};
      this.subSubjectList = []; // 二级分类列表
      this.fetchData();
    },
  },
};
</script>

<style scoped>
.myClassList .info {
  width: 450px;
  overflow: hidden;
}
.myClassList .info .pic {
  width: 150px;
  height: 90px;
  overflow: hidden;
  float: left;
}
.myClassList .info .pic a {
  display: block;
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
}
.myClassList .info .pic img {
  display: block;
  width: 100%;
}
.myClassList td .info .title {
  width: 280px;
  float: right;
  height: 90px;
}
.myClassList td .info .title a {
  display: block;
  height: 20px;
  line-height: 24px;
  overflow: hidden;
  color: #00baf2;
  margin-bottom: 12px;
}
.myClassList td .info .title p {
  line-height: 6px;
  margin-top: 1px;
  color: #565c5d;
}
.myClassList td .info .title h4 {
  color: #7dcfe6;
}
</style>