<template>

  <div class="notice-card" >
    <el-card>
      <div class="notice-theme">系统公告</div>
      <div  v-for="item in tableData">
        <p class="title">{{item.title}}</p>
        <div class="notice-div">
          <el-tooltip :content="item.content" placement="top-start" effect="dark" popper-class="atooltip" >
            <span class="content">{{item.content}}</span>
          </el-tooltip>
          <span class="time" style="">{{item.time}}</span>
        </div>
      </div>
      <div  class="pagination-div">
        <el-pagination
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total"
        lang="zh-CN">
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
      tableData:[],
      pageNum:1,
      pageSize:5,
      total:0,
    }
  },
  created() {
    this.loadData()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    loadData() {
      request.get('/notice/selectPage').then(res => {
        console.log(res)
        if (res.code == '0') {
          console.log(res.data.list)
          this.tableData = res.data.list
          this.total=res.data.total
        }
        else
        {this.$message.error(res.msg)}
      })
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.loadData()
    },
  }
}
</script>
