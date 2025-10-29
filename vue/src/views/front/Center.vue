<template>
  <div class="main-content" style="background: none;width: 75%">
    <el-row :gutter="20">
      <el-col :span="5">
        <el-card>
          <el-menu
              background-color="#fff"
              class="el-menu-vertical-demo"
              active-text-color="#409eff"
              :default-active="$route.path" router>
            <el-submenu index="2">
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span>个人中心</span>
              </template>
              <el-menu-item-group>
                <el-menu-item index="/front/center/userInfo">个人信息</el-menu-item>
                <el-menu-item index="/front/center/password">修改密码</el-menu-item>

              </el-menu-item-group>
            </el-submenu>
<!--            <el-menu-item index="2">
              <i class="el-icon-menu"></i>
              <span slot="title">导航二</span>
            </el-menu-item>-->

          </el-menu>
        </el-card>
      </el-col>
      <el-col :span="19">

        <el-card>
          <router-view />
        </el-card>
      </el-col>
    </el-row>


  </div>
</template>
<script>

import request from "@/utils/request";
export default {
  name: 'User',

  data() {
    return {
      activeIndex: '/front/personal/userInfo' // 默认激活首页
    }
  },


}
</script>