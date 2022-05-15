<template>
  <div class="app-container">
    <!--表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-select clearable placeholder="全部"> </el-select>
      </el-form-item>

      <el-form-item>
        <el-date-picker
          v-model="searchObj.begin"
          type="date"
          placeholder="选择开始日期"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="searchObj.end"
          type="date"
          placeholder="选择截止日期"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-button
        :disabled="btnDisabled"
        type="primary"
        icon="el-icon-search"
        @click="showChart()"
        >查询</el-button
      >
    </el-form>
    <h5 style="margin-top: -10px; color: #ff008b">
      暂时只有2022-04-13——————————2022-04-24有数据
    </h5>
    <div class="chart-container">
      <div id="chart" class="chart" style="height: 500px; width: 100%" />
    </div>
  </div>
</template>
<script>
import echarts from "echarts";
import staApi from "@/api/sta";
export default {
  data() {
    return {
      searchObj: {},
      btnDisabled: false,
      xData: [],
      yData1: [],
      yData2: [],
      yData3: [],
      yData4: [],
    };
  },
  methods: {
    //显示图表
    showChart() {
      staApi.getDataSta(this.searchObj).then((response) => {
        (this.xData = response.data.date_calculatedList),
          (this.yData1 = response.data.registerList);
        this.yData2 = response.data.loginList;
        this.yData3 = response.data.videoViewList;
        this.yData4 = response.data.courseList;
        //调用下面生成图表的方法，改变值
        this.setChart();
      });
    },

    //给图表设置参数
    setChart() {
      // 基于准备好的dom，初始化echarts实例
      this.chart = echarts.init(document.getElementById("chart"));
      // console.log(this.chart)

      // 指定图表的配置项和数据
      var option = {
        title: {
          text: "统计数据",
        },
        tooltip: {
          trigger: "axis"
        },
        legend: {
          data: ["注册人数", "登陆人数", "每日播放视频数", "每日新增课程数"],
        },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
            saveAsImage: {}
            }
        },
        dataZoom: [{
            show: true,
            height: 30,
            xAxisIndex: [
                0
            ],
            bottom: 30,
            start: 10,
            end: 80,
            handleIcon: 'path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z',
            handleSize: '110%',
            handleStyle: {
                color: '#d3dee5'

            },
            textStyle: {
                color: '#fff'
            },
            borderColor: '#90979c'
            },
            {
            type: 'inside',
            show: true,
            height: 15,
            start: 1,
            end: 35
            }],
        xAxis: {
          type: "category",
          boundaryGap: false,
          data: this.xData,
        },
        yAxis: {
          type: "value",
        },
        series: [
          {
            name: "注册人数",
            type: "line",
            stack: "Total",
            data: this.yData1,
          },
          {
            name: "登陆人数",
            type: "line",
            stack: "Total",
            data: this.yData2,
          },
          {
            name: "每日播放视频数",
            type: "line",
            stack: "Total",
            data: this.yData3,
          },
          {
            name: "每日新增课程数",
            type: "line",
            stack: "Total",
            data: this.yData4,
          },
        ],
      };

      this.chart.setOption(option);
    },
  },
};
</script>