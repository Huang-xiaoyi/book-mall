
<template>
  <div>
    <div class="userInfo-card">
      <div class="search">
        <el-input  class="search-input"  placeholder="请输入书名" suffix-icon="el-icon-search"></el-input>
        <el-button type="primary" class="search-button">搜索</el-button>
        <el-button type="warning">重置</el-button>
      </div>

      <el-card>
        <el-table
            :data="tableData"
            :header-cell-style="{'text-align':'center'}"
            :cell-style="{'text-align':'center'}"
            style="width: 100%">
          <el-table-column prop="id" label="ID" width="50"></el-table-column>
          <el-table-column prop="pic" label="封面" >
            <template slot-scope="scope">
              <img :src="scope.row.pic"
                   style="width: 60px; height: 80px; object-fit: cover; border-radius: 4px;"
                   alt="封面图片">
            </template>
          </el-table-column>
          <el-table-column prop="name" label="书名" ></el-table-column>
          <el-table-column prop="author" label="作者"> </el-table-column>
          <el-table-column prop="num" label="数量">
            <el-input-number :min="1" :max="99"></el-input-number>
          </el-table-column>
        </el-table>
        <!--分页-->
        <div  class="pagination-div">
          <el-pagination
              background
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-sizes="[5, 10, 20]"
              :page-size="pageSize"
              layout="total, prev, pager, next"
              :total="total">
          </el-pagination>
        </div>
      </el-card>
    </div>
    <div class="out-table">
      <el-button type="warning">全部购买</el-button>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {

    return {
      user: JSON.parse(localStorage.getItem('userInfo') || '{}'),
      pageNum:1,
      pageSize:6,
      form: {},
      tableData: {},
      search:{}
    }
  },
  created() {

  },
  methods: {

    loadDate(){
      if (this.search.content == null){
        request.get('/cert/carts', {
          params:{
            pageNum:this.pageNum,
            pageSize: this.pageSize,
          }
        }).then(res =>{
          if (res.code === '0'){
            console.log(res.data);
            this.tableData = res.data.list;
            this.total = res.data.total
          }
        })
      }
    },

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

    del(id){
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        request.delete('/cert/delete/' + id).then(res => {
          if (res.code === '0') {   // 表示操作成功
            this.$message.success('操作成功')
            this.loadData()
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      })
          .catch(()=>{})
    },
  }
}
</script>

<style scoped>
.out-table{
  display: flex;
  justify-content: end;
}
</style>