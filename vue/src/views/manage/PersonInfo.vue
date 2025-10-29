<template>
  <div>
    <el-card class="personInfo-card ">
      <el-form :model="user" label-width="100px" >
        <div >
          <el-upload
              action="http://localhost:9090/files/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess">
            <img v-if="user.avatar" :src="user.avatar" class="personInfo-avatar" />
          </el-upload>
        </div>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" placeholder="用户名" disabled></el-input>
        </el-form-item>
        <el-form-item label="名字" prop="username">
          <el-input v-model="user.name" placeholder="名字"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address" v-if="user.role == 'merchant'">
          <el-input v-model="user.address" placeholder="地址"></el-input>
        </el-form-item>
        <el-form-item label="标签" prop="tag" v-if="user.role == 'merchant'">
          <el-input v-model="user.tag"  placeholder="标签"></el-input>
        </el-form-item>
        <div class="personInfo-btn">
          <el-button type="primary"  @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "PersonInfo",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('userInfo') || '{}')
    }
  },
  created() {

  },
  methods: {
    update() {
      // 保存当前的用户信息到数据库
      if(this.user.role == 'admin') {
        request.post('/admin/update', this.user).then(res => {
          if (res.code === '0') {
            // 更新
            this.$message.success('修改成功')
            // 更新浏览器缓存的用户信息
            localStorage.setItem('userInfo', JSON.stringify(this.user))
            // 触发数据更新
            this.$emit('update:user')
          } else {
            this.$message.error(res.msg)
          }
        })
      }
      else{
        request.post('/merchant/update', this.user).then(res => {
          if (res.code === '0') {
            // 更新
            this.$message.success('修改成功')
            // 更新浏览器缓存的用户信息
            localStorage.setItem('userInfo', JSON.stringify(this.user))
            // 触发数据更新
            this.$emit('update:user')
          } else {
            this.$message.error(res.msg)
          }
        })
      }
    },
    handleAvatarSuccess(response, file, fileList) {
      // 把user的头像属性换成上传的图片的链接
      this.$set(this.user, 'avatar', response.data)
    },
  }
}
</script>

