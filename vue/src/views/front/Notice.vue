<template>
  <div class="main-notice">
    <el-card>
      <div class="notice-theme">系统公告</div>
      <div  v-for="item in noticeData">
        <p class="notice-title">{{item.title}}</p>
        <div class="notice-div">
          <span class="notice-content">{{item.content}}</span>
          <span class="notice-time" >{{item.time}}</span>
        </div>
      </div>
      <div  class="pagination-div">
        <el-pagination
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
</template>

<script>

import request from "@/utils/request";
export default {
  data() {
    return {
      noticeData:[],
      pageNum:1,
      pageSize:6,
      total:0,
    }
  },
  created() {
    this.loadData()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadData() {
      request.get('/notice/selectPage',{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
        }
      }).then(res => {
        console.log(res)
        if (res.code == '0') {
          this.noticeData = res.data.list
          this.total=res.data.total
        }
        else
        {
          this.$message.error(res.msg)
        }
      })
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.loadData()
    },
  }
}
</script>
