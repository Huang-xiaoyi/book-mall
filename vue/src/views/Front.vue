<template>
  <div>
    <!--头部-->
    <div class="header">
      <div class="header-title">
        <img src="@/assets/img/logo.png" class="logo">
        <a class="name" href="/front/index">网上书城</a>
      </div>

        <div class="header-nav">
          <el-menu :default-active="$route.path" mode="horizontal" router>
						<el-menu-item index="/front/index" class="el-menu-nav" >首页</el-menu-item>
						<el-menu-item index="/front/notice" class="el-menu-nav">系统公告</el-menu-item>
						<el-menu-item index="/front/book" class="el-menu-nav">全部图书</el-menu-item>
          </el-menu>
        </div>
      <div class="header-info">
        <div v-if="!user.username">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown size="medium">
            <div class="header-dropdown">
              <img :src="user.avatar" alt="">
              <div class="header-div">
                <span>{{ user.name }}</span><i class="el-icon-arrow-down" ></i>
              </div>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="toCenter">个人中心</el-dropdown-item>
              <el-dropdown-item>
                <div @click="logout">退出</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!--主体-->
    <div class="main-body">
      <router-view ref="child" @update:user="updateUser" />
    </div>
    <div class="footer">

      <div class="footer-info">
        <div class="footer-contact">
          <el-row  :gutter="20" class="row">
            <el-col :span="8"  class="col" style=" ">
              <p class="title">网上书城</p>
              <p class="text">浙ICP备0123456号-1</p>
              <p class="text">copyright © All rights reserved </p>
            </el-col>
            <el-col :span="8" >
              <p><i class="el-icon-user"></i> <span class="text">周一至周五9:00-18:00</span></p>
              <p><i class="el-icon-phone"></i> <span class="text">01111111</span></p>
              <p><i class="el-icon-location-outline"></i> <span class="text">广州</span></p>
            </el-col>
            <el-col :span="8" >
              <p class="add">加入我们提供</p>
              <p class="text">店面设计</p>
              <p class="text">开业指导</p>
              <p class="text">日常运营指导</p>
            </el-col>
          </el-row>
        </div>

      </div>
    </div>
  </div>

</template>

<script>

export default {
  name: "FrontLayout",

  data () {
    return {
      top: '',

      user: JSON.parse(localStorage.getItem("userInfo") || '{}'),
    }
  },

  mounted() {

  },
  methods: {

    updateUser() {
      this.user = JSON.parse(localStorage.getItem('userInfo') || '{}')   // 重新获取下用户的最新信息
    },
    toCenter() {
        this.$router.push('/front/center')
    },

    // 退出登录
    logout() {
      localStorage.removeItem("userInfo");
      this.$router.push("/login");
    },
  }

}
</script>

<style scoped>
@import "@/assets/css/front.css";
</style>