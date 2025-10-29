<template>
  <div class="home-div">
    <div class="home-form">
      <h1 class="home-name">网上书城</h1>
      <el-form :model="form"  ref="formRef">
      <el-input class="input-bar" placeholder="用户名" v-model="form.username" clearable prefix-icon="el-icon-user" ></el-input>
      <el-input class="input-bar" placeholder="密码" v-model="form.password" show-password prefix-icon="el-icon-lock" ></el-input>
      <el-form-item prop="role" >
        <el-select v-model="form.role" placeholder="请选择角色"  class="input-bar">
          <el-option label="用户" value="user"></el-option>
          <el-option label="管理员" value="admin"></el-option>
          <el-option label="商家" value="merchant"></el-option>
        </el-select>
      </el-form-item>
      <el-button class="home-btn" type="primary" @click="login">登录</el-button>
        <div class="home-jump">
         没有账号？<a class="home-jump-a" href="/register">注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { Message } from "element-ui";
import request from "@/utils/request";
export default {
  data() {
    return {
      form: {},
      username: "",
      password: "",
      role: "",
    };
  },
  created() {

  },
  methods: {
    login() {
      //判断是否为空
      if (!this.form.username){

        this.$message.warning("用户名不能为空")
        return
      }
      if (!this.form.password){
        this.$message.warning("密码不能为空")
        return
      }
        request.post("account/login", this.form).then(res => {
          if(res.code === '0') {
            let user = res.data.user;
            let token = res.data.token;
            localStorage.setItem("token",token);
            localStorage.setItem("userInfo",JSON.stringify(user))
            this.$message.success('登录成功！')
            if (user.role=='user'){
              location.href='/front/index'
            }else {
              location.href='/manage/home'
            }
          } else {
            this.$message.error(res.msg)
          }
        })
      }
    }

};
</script>
<style scoped>
@import "@/assets/css/manage.css";
</style>
