<template>

  <div>
    <el-card  class="top-card">
    <div class="search">
      <el-input  class="search-input"  v-model="search.content" placeholder="请输入标题" suffix-icon="el-icon-search"></el-input>
      <el-button type="primary" class="search-button"  plain @click="loadData()">搜索</el-button>
      <el-button type="warning"  plain @click="reset()">重置</el-button>
    </div>
      <el-button  type="primary" @click="add()">新增</el-button>
    </el-card>
    <el-card>
    <el-table
        :data="tableData"
        :header-cell-style="{'text-align':'center'}"
        :cell-style="{'text-align':'center'}"
        style="width: 100%">
      <el-table-column prop="id" label="ID" width="50"></el-table-column>
      <el-table-column prop="name" label="分类名称" > </el-table-column>
            <el-table-column prop="" label="操作" width="160">
        <template slot-scope="scope">
          <el-button type="primary"  icon="" @click="edit(scope.row)">编辑</el-button>
          <el-button type="danger"  @click="del(scope.row.id)">删除</el-button>
        </template>
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
    <!--模态框-->
    <el-dialog title="分类信息" :visible.sync="dialogVisible" width="40%">
      <el-form :model="form" label-position="center" label-width="100px"  class="dialog-form" >
        <el-form-item label="分类名称">
          <el-input  v-model="form.name" placeholder="请输入名称"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button  @click="dialogVisible = false">取 消</el-button>
        <el-button  type="primary" @click="save">保 存</el-button>
      </div>
    </el-dialog>


  </div>


</template>

<script>

import request from "@/utils/request";
export default {
  name: 'User',

  data() {
    return {
      form:{},
      search:{},
      dialogVisible:false,
      pageNum:1,
      pageSize:6,
      total:0,
      tableData: []
    }
  },
  created() {
    this.loadData()
  },
  methods:{
    //  当点击页码时，当前页码数赋值给data变量pageNum
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.loadData()
    },
    // 分页加载表格数据
    loadData() {
      request.get("/type/selectPage",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          title:this.search.content
        }
      }).then(res=>{
        if (res.code=='0'){
          console.log(res.data)
          this.tableData = res.data.list
          this.total=res.data.total
        }
        else {
          this.$message.error(res.msg)
        }
      })
    },
    reset(){
      this.search.content = null
      this.loadData()
    },
    add(){
      this.form = {}
      this.dialogVisible=true
    },

    save() {
      if (!this.form.id) {
        //新增 为空
        request.post("/type/save", this.form).then(res => {
          if (res.code === '0') {
            this.$message.success('保存成功');
            this.loadData()
            this.dialogVisible = false
          }
          else
          {
            this.$message.error(res.msg)
          }
        })
      } else {
        request.post("/type/update", this.form).then(res => {
          if (res.code === '0') {
            this.$message.success('修改成功');
            this.loadData()
            this.dialogVisible = false
          }
          else
          {
            this.$message.error(res.msg)
          }
        })
      }
    },

    del(id){
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        request.delete('/type/delete/' + id).then(res => {
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
    edit(row){
      this.form=row
      this.dialogVisible=true
    },

  }
}
</script>

