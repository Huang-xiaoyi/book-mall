<template>
  <div class="main-content" >
    <div>
      <el-carousel :interval="5000" arrow="always" height="45vh">
        <el-carousel-item v-for="item in imgData" :key="item" >
          <img :src="item" class="index-carousel">
        </el-carousel-item>
      </el-carousel>
    </div>
    <div>
      <div class="recommend-title">
        <span class="text" >推荐</span>
        <a class="more" href="">查看更多信息</a>
      </div>
      <div class="recommend">
        <el-row >
          <el-col :span="6">
            <div class="info">
            <img src="@/assets/img/picture1.png" class="img">
            <p>666</p>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info">
              <img src="@/assets/img/picture1.png" class="img">
              <p>666</p>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info">
              <img src="@/assets/img/picture1.png" class="img">
              <p>666</p>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info">
              <img src="@/assets/img/picture1.png" class="img">
              <p>666</p>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

  </div>
</template>

<script>
import carousel_1  from "@/assets/img/carousel_1.png"
import carousel_2  from "@/assets/img/carousel_2.png"
export default {
  data() {
    return {
      autoHeight:'',
      imgData:[
        picture1,carousel_2
      ]

    }
  },
  created() {
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
  }
}
</script>
