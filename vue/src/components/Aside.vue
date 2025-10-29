<template>

  <el-menu default-active="2"  class="el-menu-vertical-demo"
           background-color="#545c64"
           text-color="#fff"
           active-text-color="#409EFF"
           router=""
           :default-active="$route.path">
           <div class="aside-div">
             <img src="@/assets/img/logo.png" class="logo">
      <span >网上书城</span>
    </div>


    <el-menu-item index="/manage/home">
      <template slot="title">
        <i class="el-icon-house"></i>
        <span>主页</span>
      </template>
    </el-menu-item>
    <el-menu-item index="/manage/notice" v-if="user.role == 'admin' ">
      <template slot="title">
        <i class="el-icon-message"></i>
        <span>公告信息</span>
      </template>
    </el-menu-item>
    <el-menu-item index="/manage/book" v-if="user.role == 'merchant' ">
      <template slot="title">
        <i class="el-icon-message"></i>
        <span>书本信息</span>
      </template>
    </el-menu-item>
    <el-menu-item index="/manage/type" v-if="user.role == 'admin'">
      <template slot="title">
        <i class="el-icon-message"></i>
        <span>分类信息</span>
      </template>
    </el-menu-item>
    <el-submenu index="1" v-if="user.role == 'admin'">
      <template slot="title">
        <i class="el-icon-user"></i>
        <span>用户信息</span>
      </template>
      <el-menu-item-group>
        <el-menu-item index="/manage/user">用户信息</el-menu-item>
        <el-menu-item index="/manage/admin">管理员信息</el-menu-item>
        <el-menu-item index="/manage/merchant">商家信息</el-menu-item>
      </el-menu-item-group>
    </el-submenu>

  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('userInfo') || '{}')
    }
  },
}
</script>


