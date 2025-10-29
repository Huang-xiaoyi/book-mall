<template>
  <div class="div-password">

      <el-form :model="user" label-width="80px" class="form-input" :rules="rules" ref="formRef">
        <el-form-item label="原密码" prop="password">
          <el-input  v-model="user.password" placeholder="原密码" ></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input  v-model="user.newPassword" placeholder="新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input  v-model="user.confirmPassword" placeholder="确认密码"></el-input>
        </el-form-item>
        <div  class="password-btn">
          <el-button  type="primary" @click="updatePassword">修改密码</el-button>
        </div>
      </el-form>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.user.newPassword) {
        callback(new Error('确认密码错误'))
      } else {
        callback()
      }
    }
    return {
      user: JSON.parse(localStorage.getItem('userInfo') || '{}'),
      dialogVisible: false,

      rules: {
        password: [
          { required: true, message: '请输入原始密码', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        confirmPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
      }
    }
  },
  created() {

  },
  methods: {

    updatePassword() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
        request.post('/account/updatePassword', this.user).then(res => {
            if (res.code === '0') {
              // 成功更新
              this.$message.success('修改密码成功,请重新登录')
              this.$router.push('/login')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>
