<template>
  <div >
    <div class="userInfo-card">

      <el-form :model="user" label-width="80px" class="form-input">
        <div class="userInfo-div">
          <el-upload
              action="http://localhost:9090/files/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess">
            <img v-if="user.avatar" :src="user.avatar" class="userInfo-avatar"/>
          </el-upload>
        </div>
        <el-form-item label="用户名" prop="username">
          <el-input  v-model="user.username" placeholder="用户名" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input  v-model="user.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input  v-model="user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input  v-model="user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">

          <el-select v-model="user.sex" placeholder="性别"  style="width: 100% " class="input-bar">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input  v-model="user.age" placeholder="年龄"></el-input>
        </el-form-item>
        <div class="userInfo-btn">
          <el-button  type="primary" @click="update">修改</el-button>

        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {

    return {
      user: JSON.parse(localStorage.getItem('userInfo') || '{}'),
      dialogVisible: false,
    }
  },
  created() {

  },
  methods: {
    update() {
      // 保存当前的用户信息到数据库
      request.post('/user/update', this.user).then(res => {
        if (res.code === '0') {
          // 成功更新
          this.$message.success('保存成功')
          // 更新浏览器缓存里的用户信息
          localStorage.setItem('userInfo', JSON.stringify(this.user))

          // 触发父级的数据更新
          this.$emit('update:user')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAvatarSuccess(response) {
      // 把user的头像属性换成上传的图片的链接
      this.$set(this.user, 'avatar', response.data)
    },

  }
}
</script>
