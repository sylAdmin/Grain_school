//课程大纲（章节）
<template>
  <div class="app-container">
    <h2 style="text-align: center">发布新课程</h2>

    <el-steps
      :active="2"
      process-status="wait"
      align-center
      style="margin-bottom: 40px"
    >
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="最终发布" />
    </el-steps>

    <el-button type="text" @click="openChapterDialog()">添加章节</el-button>
    <!-- 章节 -->
    <ul class="chanpterList">
      <li v-for="chapter in chapterVideoList" :key="chapter.id">
        <p>
          {{ chapter.title }}

          <span class="acts">
            <el-button type="text" @click="openVideo(chapter.id)"
              >添加课时</el-button
            >
            <el-button style="" type="text" @click="openEditChapter(chapter.id)"
              >编辑</el-button
            >
            <el-button type="text" @click="deleteEditChapter(chapter.id)"
              >删除</el-button
            >
          </span>
        </p>

        <!-- 视频 -->
        <ul class="chanpterList videoList">
          <li v-for="video in chapter.children" :key="video.id">
            <p>
              {{ video.title }}
              <span class="acts">
                <el-button type="text" @click="openEdit(video.id)"
                  >编辑</el-button
                >
                <el-button type="text" @click="deleteVideo(video.id)"
                  >删除</el-button
                >
              </span>
            </p>
          </li>
        </ul>
      </li>
    </ul>
    <div>
      <el-button @click="previous">上一步</el-button>
      <el-button :disabled="saveBtnDisabled" type="primary" @click="next"
        >下一步</el-button
      >
    </div>
    <!-- 添加和修改章节表单 -->
    <el-dialog :visible.sync="dialogChapterFormVisible" title="添加章节">
      <el-form :model="chapter" label-width="120px">
        <el-form-item label="章节标题">
          <el-input v-model="chapter.title" />
        </el-form-item>
        <el-form-item label="章节排序">
          <el-input-number
            v-model="chapter.sort"
            :min="0"
            controls-position="right"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogChapterFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate" :disabled="disEdit"
          >确 定</el-button
        >
      </div>
    </el-dialog>
    <!-- 添加和修改课时表单 -->
    <el-dialog :visible.sync="dialogVideoFormVisible" title="添加课时">
      <el-form :model="video" label-width="120px">
        <el-form-item label="课时标题">
          <el-input v-model="video.title" />
        </el-form-item>
        <el-form-item label="课时排序">
          <el-input-number
            v-model="video.sort"
            :min="0"
            controls-position="right"
          />
        </el-form-item>
        <el-form-item label="是否免费">
          <el-radio-group v-model="video.free">
            <el-radio :label="true">免费</el-radio>
            <el-radio :label="false">默认</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上传视频">
          <el-upload
            :on-success="handleVodUploadSuccess"
            :on-remove="handleVodRemove"
            :before-remove="beforeVodRemove"
            :on-exceed="handleUploadExceed"
            :on-progress="progress"
            :file-list="fileList"
            :action="BASE_API + '/eduvod/video/uploadAlyVideo'"
            :limit="1"
            class="upload-demo"
          >
            <el-button size="small" type="primary">上传视频<i class="el-icon-upload el-icon--right"></i></el-button>
            <el-tooltip placement="right-end">
              <div slot="content">
                最大支持1G,<br />
                支持3GP、ASF、AVI、DAT、DV、FLV、F4V、<br />
                GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、<br />
                MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、<br />
                SWF、TS、VOB、WMV、WEBM 等视频格式上传
              </div>
              <i class="el-icon-question" />
            </el-tooltip>
          </el-upload>
          <el-label :hidden="hidden" style="color: red">请等待视频上传成功...</el-label>
        </el-form-item>
        
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVideoFormVisible = false">取 消</el-button>
        <el-button
          :disabled="saveVideoBtnDisabled"
          type="primary"
          @click="saveOrUpdateVideo"
          :loading="load">确定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import chapter from "@/api/edu/chapter";
import video from "@/api/edu/video";
import v from "@/api/edu/v";

export default {
  data() {
    return {
      hidden: true,
      saveBtnDisabled: false, //保存按钮是否禁用
      saveVideoBtnDisabled: false,
      disEdit: false,
      chapterVideoList: [],
      courseId: "", //课程id
      chapter: {
        //封装章节数据
        title: "",
        sort: 0,
      },
      load: false,
      video: {
        id: "",
        title: "",
        sort: 0,
        free: 0,
        videoSourceId: "", //视频id
        videoOriginalName: "", //视频名称
      },
      dialogChapterFormVisible: false, //章节弹框
      dialogVideoFormVisible: false, //小节弹框
      fileList: [], //上传文件列表
      fileLists: [],
      BASE_API: process.env.BASE_API, // 接口API地址
    };
  },

  created() {
    if (this.$route.params && this.$route.params.id) {
      this.courseId = this.$route.params.id;
      this.getChapterVideo();
    }
  },

  methods: {
    //==========================================视频操作============================================

    //点击x调用的方法
    beforeVodRemove(file, fielList) {
      return this.$confirm(`确定移除 ${file.name}?`);
    },

    //点击确定调用的方法
    handleVodRemove() {
      video.deleteAlyVideo(this.video.videoSourceId).then((response) => {
        //把文件列表清空
        this.fileList = [];
        this.video.videoOriginalName = "";
        this.video.videoSourceId = "";
        //提示信息
        this.$message({
          type: "success",
          message: "删除成功",
        });
      });
    },

    //文件上传时
    progress(){
      //文件上传没完成之前，先禁用按钮，防止数据没有传到数据库
      this.load = true;
      this.hidden = false;
    },

    //file表示当前上传的文件
    //fielList代表文件列表
    handleVodUploadSuccess(response, file, fielList) {
      this.hidden = true;
      //上传成功之后调用的方法
      //上传视频id
      this.video.videoSourceId = response.data.videoId;
      //上传视频名称
      this.video.videoOriginalName = file.name;
      this.load = false;
    },

    handleUploadExceed() {
      this.$message.warning("想要重新上传视频，请先删除已上传视频!");
    },
    //==========================================课时操作============================================
    openVideo(chapterId) {
      this.dialogVideoFormVisible = true;
      //设置章节id
      this.video.chapterId = chapterId;
      this.saveVideoBtnDisabled = false;
      this.title = "";
      this.video.title = "";
    },

    //添加小节
    addVideo() {
      //设置课程id
      this.video.courseId = this.courseId;
      video.addVideo(this.video).then((response) => {
        this.video.id = response.data.id;
        //关闭弹框
        this.dialogVideoFormVisible = false;
        //提示信息
        this.$message({
          type: "success",
          message: "添加课时成功",
        });
        //this.chapter.id = ''
        //刷新页面
        this.getChapterVideo();
      });
    },

    //课时回显
    openEdit(id) {
      video.getVideo(id).then((response) => {
        this.video = response.data.eduVideo;
      });
      this.dialogVideoFormVisible = true;
      this.saveVideoBtnDisabled = false;
    },

    //删除课时
    deleteVideo(id) {
      video.deleteVideo(id).then((response) => {
        this.$message({
          type: "success",
          message: "删除成功!",
        });
        //刷新页面
        this.getChapterVideo();
      });
    },

    //编辑课时
    updateVideo(video) {
      v.updateval(video).then((response) => {
        this.$message({
          type: "success",
          message: "修改成功!",
        });
        this.dialogVideoFormVisible = false;
        //刷新页面
        this.getChapterVideo();
      });
    },

    //保存小节
    saveOrUpdateVideo() {
      if (this.video.id) {
        this.saveVideoBtnDisabled = true;
        this.updateVideo(this.video);
      } else {
        this.fileList = []
        this.saveVideoBtnDisabled = true;
        this.addVideo();
      }
    },

    //==========================================章节操作============================================
    //删除章节
    deleteEditChapter(chapterId) {
      this.$confirm("此操作将永久删除章节和小节, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        //点击确定执行then方法
        chapter.deleteChapter(chapterId).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          //刷新页面
          this.getChapterVideo();
        });
      });
    },

    //修改章节弹框数据回显
    openEditChapter(chapterId) {
      chapter.getChapter(chapterId).then((response) => {
        this.chapter = response.data.chapter;
        this.dialogChapterFormVisible = true;
      });
    },

    //弹出添加章节页面
    openChapterDialog() {
      //弹框
      this.dialogChapterFormVisible = true;
      //表单数据清空
      this.chapter = {};
      this.chapter.sort = 0;
    },

    //添加章节
    addChapter() {
      //设置课程id到chapter对象里面
      this.chapter.courseId = this.courseId;
      chapter.addChapter(this.chapter).then((response) => {
        this.disEdit = false;
        //关闭弹框
        this.dialogChapterFormVisible = false;
        //提示信息
        this.$message({
          type: "success",
          message: "添加章节成功",
        });
        //this.chapter.id = ''
        //刷新页面
        this.getChapterVideo();
      });
    },

    //更新章节
    updateChapter() {
      chapter.updateChapter(this.chapter).then((response) => {
        //关闭弹框
        this.dialogChapterFormVisible = false;
        //提示信息
        this.$message({
          type: "success",
          message: "修改章节成功",
        });
        //刷新页面
        this.getChapterVideo();
      });
    },

    saveOrUpdate() {
      if (this.chapter.id) {
        this.updateChapter();
      } else {
        this.disEdit = true;
        this.addChapter();
      }
    },

    //根据课程id查询章节和小节
    getChapterVideo() {
      chapter.getAllChapterVideo(this.courseId).then((response) => {
        this.chapterVideoList = response.data.list;
      });
    },

    previous() {
      this.$router.push({ path: "/course/info/" + this.courseId });
    },

    next() {
      this.$router.push({ path: "/course/publish/" + this.courseId });
    },
  },
};
</script>
<style scoped>
.chanpterList {
  position: relative;
  list-style: none;
  margin: 0;
  padding: 0;
}
.chanpterList li {
  position: relative;
}
.chanpterList p {
  float: left;
  font-size: 20px;
  margin: 10px 0;
  padding: 10px;
  height: 70px;
  line-height: 50px;
  width: 100%;
  border: 1px solid #ddd;
}
.chanpterList .acts {
  float: right;
  font-size: 14px;
}

.videoList {
  padding-left: 50px;
}
.videoList p {
  float: left;
  font-size: 14px;
  margin: 10px 0;
  padding: 10px;
  height: 50px;
  line-height: 30px;
  width: 100%;
  border: 1px dotted #ddd;
}
</style>
