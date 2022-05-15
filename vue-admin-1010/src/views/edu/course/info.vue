//课程信息
<template>
  <div class="app-container">
    <h2 style="text-align: center">发布新课程</h2>

    <el-steps
      :active="1"
      process-status="wait"
      align-center
      style="margin-bottom: 40px"
    >
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="最终发布" />
    </el-steps>

    <el-form label-width="120px">
      <el-form-item label="课程标题">
        <el-input
          v-model="courseInfo.title"
          placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写"
        />
      </el-form-item>

      <!-- 所属分类 -->
      <el-form-item label="课程分类">
        <el-select
          v-model="courseInfo.subjectParentId"
          placeholder="一级分类"
          @change="subjectLevelOneChanged"
        >
          <el-option
            v-for="subject in subjectOneList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"
          />
        </el-select>

        <!-- 二级分类 -->
        <el-select
          v-model="courseInfo.subjectId"
          :placeholder="pla"
          :disabled="dis"
        >
          <el-option
            v-for="subject in subjectTwoList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"
          />
        </el-select>
      </el-form-item>

      <!-- 课程讲师 -->
      <el-form-item label="课程讲师">
        <el-select v-model="courseInfo.teacherId" placeholder="请选择">
          <el-option
            v-for="teacher in teacherList"
            :key="teacher.id"
            :label="teacher.name"
            :value="teacher.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="总课时">
        <el-input-number
          :min="0"
          v-model="courseInfo.lessonNum"
          controls-position="right"
          placeholder="请填写课程的总课时数"
        />
      </el-form-item>

      <!-- 课程简介 TODO -->
      <!-- 课程简介-->
      <el-form-item label="课程简介">
        <tinymce :height="300" v-model="courseInfo.description" />
      </el-form-item>

      <!-- 课程封面-->
      <el-form-item label="课程封面">
        <el-upload
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :action="BASE_API + '/eduoss/fileoss'"
          class="avatar-uploader"
        >
          <img :src="courseInfo.cover"
           width="300px" height="200px"/>
        </el-upload>
      </el-form-item>

      <el-form-item label="课程价格">
        <el-input-number
          :min="0"
          v-model="courseInfo.price"
          controls-position="right"
          placeholder="免费课程请设置为0元"
        />
        元
      </el-form-item>

      <el-form-item>
        <el-button
          :disabled="saveBtnDisabled"
          type="primary"
          @click="saveOrUpdate()"
          >保存并下一步</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import course from "@/api/edu/course";
import subject from "@/api/edu/subject";
import Tinymce from "@/components/Tinymce";
export default {
  components: { Tinymce },
  data() {
    return {
      dis: false,
      saveBtnDisabled: false, // 保存按钮是否禁用
      courseInfo: {
        title: "",
        subjectId: "", //二级分类id
        subjectParentId: "", //一级分类id
        teacherId: "",
        lessonNum: 0,
        description: "",
        cover: "https://grain-school.oss-cn-hangzhou.aliyuncs.com/picture/2022/04/03/3827b07a37624dfd8a3dece0c8f4276c.jpg",
        price: 0,
      },
      courseId: "",
      BASE_API: process.env.BASE_API, // 接口API地址
      pla: "二级分类",
      teacherList: [], //封装所有讲师数据
      subjectOneList: [],
      subjectTwoList: [],
    };
  },

  created() {
    this.init();
  },

  watch: {
    //监听
    $route(to, from) {
      //路由发生变化，方法就会执行，解决上面created第二次路由不执行问题
      this.init();
    },
  },

  methods: {
    //判断路由中是否有有id
    init() {
      if (this.$route.params && this.$route.params.id) {
        this.courseId = this.$route.params.id;
        //调用根据id查询课程的方法
        this.getInfo();
      } else {
        //当路径中没有id时，也就是用户从修改页面点击到添加页面时，清空数据
        this.courseInfo = {};
        //初始化所有讲师
        this.getListTeacher();
        //初始化一级分类
        this.getOneSubject();
      }
    },
    //根据课程id查询
    getInfo() {
      course.getCourseInfoId(this.courseId).then((response) => {
        this.courseInfo = response.data.courseInfoVo;
        //查询所有的一级和二级分类
        subject.getSubjectList().then((response) => {
          //获取所有的一级和二级分类
          this.subjectOneList = response.data.list;
          //遍历一级分类
          for (var i = 0; i < this.subjectOneList.length; i++) {
            //获取每个一级分类
            var oneSubject = this.subjectOneList[i];
            //判断当前courseInfo的一级分类id和所有的一级分类id作比较
            if (this.courseInfo.subjectParentId == oneSubject.id) {
              //获取一级分类所有的二级分类
              this.subjectTwoList = oneSubject.children;
            }
          }
        });
        //初始化所有讲师
        this.getListTeacher();
      });
    },

    //点击某个一级分类，触发change，显示对应二级分类
    //框架给我们做了封装，选中列表框的id会自动传过来给value
    subjectLevelOneChanged(value) {
      //遍历所有的分类，包含一级和二级
      for (let i = 0; i < this.subjectOneList.length; i++) {
        if (this.subjectOneList[i].id === value) {
          if (this.subjectOneList[i].children.length) {
            this.dis = false;
            this.pla = "二级分类";
            this.subjectTwoList = this.subjectOneList[i].children;
            this.courseInfo.subjectId = "";
          } else {
            this.courseInfo.subjectId = "";
            this.dis = true;
            this.pla = "无";
          }
        }
      }
    },
    //查询所有的一级分类
    getOneSubject() {
      subject.getSubjectList().then((response) => {
        this.subjectOneList = response.data.list;
      });
    },

    //查询所有讲师
    getListTeacher() {
      course.getListTeacher().then((response) => {
        this.teacherList = response.data.items;
      });
    },

    addCourse() {
      course.getCourseInfo(this.courseInfo).then((response) => {
        this.$message({
          type: "success",
          message: "添加课程信息成功",
        });
        //跳转到第二步
        this.$router.push({
          path: "/course/chapter/" + response.data.courseId,
        });
      });
    },

    //修改课程
    updateCourse() {
      course.updateCourseInfoId(this.courseInfo).then((response) => {
        
        //修改成功提示
        this.$message({
          type: "success",
          message: "修改课程信息成功",
        });
        //跳转到第二步
        this.$router.push({
          path: "/course/chapter/" + this.courseId,
        });
      });
    },
    //添加课程信息
    saveOrUpdate() {
      //判断是否有id，有就是修改，没有就是添加
      if (!this.courseId) {
        this.addCourse();
      } else {
        this.updateCourse();
      }
    },

    //上传之前调用的方法
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      return isJPG;
    },
    //上传封面成功调用的方法
    handleAvatarSuccess(res, file) {
      this.courseInfo.cover = res.data.url;
    },
  },
};
</script>
<style scoped>
.tinymce-container {
  line-height: 29px;
}
</style>