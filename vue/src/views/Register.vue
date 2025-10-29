<template>
  <div class="home-div">
    <div class="home-form">
      <h1 class="home-name">注册</h1>
      <el-form :model="form">
        <el-input class="input-bar" placeholder="名字" v-model="form.name" clearable prefix-icon="el-icon-user-solid"></el-input>
      <el-input class="input-bar" placeholder="用户名" v-model="form.username" clearable prefix-icon="el-icon-user"></el-input>
      <el-input class="input-bar" placeholder="密码" v-model="form.password" show-password prefix-icon="el-icon-lock"></el-input>
        <el-input class="input-bar" placeholder="确认密码" v-model="form.confirmPassword" show-password prefix-icon="el-icon-lock"></el-input>
<!--
//注册为多个角色
    <el-form-item prop="role" style="display: none">
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%" value="user">
            <el-option label="用户" value="user"></el-option>
          </el-select>
        </el-form-item>-->
      <el-button class="register-btn" type="primary" @click="register()">注册</el-button>
        <div class="home-jump">
         已有账号? <a  class="home-jump-a" href="/login"> 登录</a>
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
      account: "",
      password: "",
      confirmPassword:"",
    };
  },
  created() {
/*    Message.warning({
      message: "请登录"
    });*/
  },
  methods: {
    register() {
if (!this.form.username){
  this.$message.warning("用户名不能为空")
  return
}
      if (!this.form.password){
        this.$message.warning("密码不能为空")
        return
      }
      if (!this.form.confirmPassword){
        this.$message.warning("确认密码不能为空")
        return
      }
      if (this.form.confirmPassword!=this.form.password){
        this.$message.warning("密码不一致")
        return
      }
      request.post("account/register", this.form).then(res => {
        if (res.code === '0') {
          this.$message.success('注册成功')
          this.$router.push('/')

        }
else{
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
