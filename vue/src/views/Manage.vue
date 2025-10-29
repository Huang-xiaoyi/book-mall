<template>
  <div class="home">

    <el-container class="container" >
      <el-aside  class="aside" width="200px" >
        <Aside ></Aside>
      </el-aside>
      <el-container>



        <el-header>
          <div class="header" >
            <div style="flex: 1">
              <el-breadcrumb separator-class="el-icon-arrow-right">
                <el-breadcrumb-item :to="{ path: '/manage/home' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
              </el-breadcrumb>
            </div>
            <img :src="user.avatar" alt="" class="header-avatar" >
            <el-dropdown style="cursor: pointer"  size="medium">
              <span class="header-name">{{user.name}}<i class="el-icon-arrow-down" ></i></span>

              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="toPersonInfo" >个人信息</el-dropdown-item>
                <el-dropdown-item @click.native="toUpdatePassword" >修改密码</el-dropdown-item>
                <el-dropdown-item ><span @click="logout()">退出</span></el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-header>

        <el-main>
          <!-- main中的按钮-->
          <router-view @update:user="updateUser" />



        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>

import Aside from "@/components/Aside"
export default {
  components: { Aside},
  name: "Manager",
  data() {

    return {
      user: JSON.parse(localStorage.getItem('userInfo') || '{}'),
    }
  },
  created() {
    if (!this.user.id) {
      this.$router.push('/login')
    }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('userInfo') || '{}')   // 重新获取下用户的最新信息
    },
    toPersonInfo() {
      this.$router.push('/manage/personInfo')
    },
    toUpdatePassword() {
      this.$router.push('/manage/password')
    },
    logout() {
      localStorage.removeItem('userInfo')
      this.$router.push('/login')
    }
  }
}

</script>
<style scoped>

@import "@/assets/css/manage.css";
.el-menu{
  border-right: none !important;
}

</style>

