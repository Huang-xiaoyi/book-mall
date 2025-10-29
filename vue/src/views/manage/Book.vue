<template>

  <div>
    <el-card  class="top-card">
    <div class="search">
      <el-input  class="search-input"  v-model="search.content" placeholder="请输入书名" suffix-icon="el-icon-search"></el-input>
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
      <el-table-column prop="pic" label="封面" >
        <template slot-scope="scope">
          <img :src="scope.row.pic"
               style="width: 60px; height: 80px; object-fit: cover; border-radius: 4px;"
               alt="封面图片">
        </template>
      </el-table-column>
      <el-table-column prop="name" label="书名" ></el-table-column>
      <el-table-column prop="author" label="作者"> </el-table-column>
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
    <el-dialog title="书本信息" :visible.sync="dialogVisible" width="40%">
      <el-form :model="form" label-position="center" label-width="100px"  class="dialog-form" >
        <el-form-item label="书名">
          <el-input  v-model="form.name" placeholder="请输入书名"></el-input>
        </el-form-item>
        <el-form-item label="封面">

          <el-upload
              class="avatar-uploader"
              action="http://localhost:9090/files/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess">
            <img v-if="form.pic" :src="form.pic" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>

        </el-form-item>
        <el-form-item label="价格">
          <el-input  type="number" v-model="form.price" placeholder="请输入价格"></el-input>
        </el-form-item>
        <el-form-item label="概述">
          <el-input type="textarea" v-model="form.detail" placeholder="请输入概述" ></el-input>
        </el-form-item>
        <el-form-item label="库存">
          <el-input type="number" v-model="form.stock" placeholder="请输入库存"></el-input>
        </el-form-item>
        <el-form-item label="作者">
          <el-input  v-model="form.author" placeholder="请输入作者姓名"></el-input>
        </el-form-item>
        <el-form-item label="出版单位">
          <el-input  v-model="form.publisher"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="form.typeId" placeholder="请选择类型">
            <el-option :label="item.name" :value="item.id" v-for="item in typeData"></el-option>
          </el-select>
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
      tableData: [],
      typeData: []
    }
  },
  created() {
    this.loadData()
    this.loadTypeData()
  },
  methods:{
    //  当点击页码时，当前页码数赋值给data变量pageNum
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.loadData()
    },
    // 分页加载表格数据
    loadData() {
      if (this.search.content == null){

        request.get("/book/selectPage",{
          params:{
            pageNum:this.pageNum,
            pageSize:this.pageSize,
            title:this.search.content
          }
        }).then(res=>{
          if (res.code==='0'){
            console.log(res.data)
            this.tableData = res.data.list
            this.total=res.data.total
          }
          else {
            this.$message.error(res.msg)
          }
        })
      }else{
        request.get("/book/search", {
          params:{
            pageNum:this.pageNum,
            pageSize:this.pageSize,
            name:this.search.content
          }
        }).then(res =>{
          if (res.code==='0'){
            console.log(res.data)
            this.tableData = res.data.list
            this.total=res.data.total
          }
          else {
            this.$message.error(res.msg)
          }
        })
      }
    },
    loadTypeData(){
      request.get("/type/selectAll").then(res=>{
        if(res.code === '0'){
          this.typeData = res.data
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
      // console.log(this.form.typeId)
      // this.form.typeId = Number(this.form.typeId)
      if (!this.form.id) {
        //检查书的id，没有就是新增 为空
        request.post("/book/save", this.form).then(res => {
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
        request.post("/book/update", this.form).then(res => {
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
        request.delete('/book/delete/' + id).then(res => {
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

    handleAvatarSuccess(response) {
      // 获取后台传来的图片路径
      this.form.pic = response.data
      this.$forceUpdate()
    },

  }
}
</script>

