<template>
<div class="book-container">
  <div class="book-information">
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="book-image-container">
          <img :src="book.pic" class="book-image">
<!--          固定高度，使用flex布局使得图片在区域内水平居中
object-fit: contain保持图片比例不拉伸-->
        </div>
      </el-col>
      <el-col :span="8"><div class="book-name">{{book.name}}</div></el-col>
      <el-col :span="6" :offset="4"><div class="book-price">￥{{book.price}}</div></el-col>
      <el-col :span="6"><div class="book-author">{{book.author}}</div></el-col>
      <el-col :span="18"></el-col>
<!--      为了让组件能够打破行24列排列的限制，选择制作空组件填充空隙-->
      <el-col :span="6"><div class="description-container">
        <div :class="['description-content', {expanded : isDescriptionExpanded}]"
             ref="descriptionText">
          {{book.detail}}
        </div>
        <div v-if="showReadMore" class="read-more-gradient"></div>
        <div class="read-more-container">
          <span class="read-more-btn" @click="toggleDescription">
            {{ isDescriptionExpanded ? '收起' : '查看更多' }}
          </span>
        </div>
      </div></el-col>
      <el-col :span="18"></el-col>
      <el-col :span="6"><div class="purchase-number">
        <el-input-number v-model="booknum" :min="1" :max="book.stock" label="描述文字"></el-input-number>
        库存{{book.stock}}
      </div></el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="6" :offset="6"><el-button type="warning">收藏</el-button>
        <el-button type="danger">加入购物车</el-button>
      </el-col>
    </el-row>
  </div>

  <!-- 评论区 -->
<!--  <Comment :book-id="book ? book.id : null" />-->
</div>
</template>

<script>
import Comment from '@/components/Comment.vue'
import request from '@/utils/request'

export default {
  name: "BookDetail",
  components: {
    Comment
  },

  data() {
    return {
      booknum: 0,
      bookStr: "",
      book: null,
      loading: false,
      isDescriptionExpanded: false,
      showReadMore: false,
      error: null
    }
  },
  created() {
    this.bookStr = this.$route.query.book;
    if(this.bookStr){
      try{
        this.book = JSON.parse(this.bookStr);
        return;
      }catch (e) {
        console.error("无效的图书数据", e);
      }
    }
    // 字符串丢失时发出请求重新加载数据，当前后端业务层还没写
    this.loadBookDetail();
  },
  // 当DOM加载完毕后会立刻执行这里的代码
  mounted(){
    this.checkDescriptionOverflow();
  },
  methods: {
    loadBookDetail() {
      const bookId = this.$route.query.id;
      if (!bookId) {
        this.error = '图书ID不存在';
        return;
      }

      this.loading = true;
      this.error = null;

      request.get(`/book/selectById?id=${bookId}`)
        .then(res => {
          if (res.code === '0') {
            this.book = res.data;
          } else {
            this.error = res.msg || '获取图书信息失败';
          }
        })
        .catch(err => {
          this.error = '请求出错，请稍后重试';
          console.error('Error fetching book detail:', err);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    checkDescriptionOverflow() {
      const textEl = this.$refs.descriptionText;
      if (textEl) {
        this.showReadMore = textEl.scrollHeight > textEl.clientHeight && !this.isDescriptionExpanded;
      }
    },
    toggleDescription() {
      this.isDescriptionExpanded = !this.isDescriptionExpanded;
      this.$nextTick(() => {
        this.checkDescriptionOverflow();
      });
    }
  }
}
</script>

<style scoped>
.book-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}
.book-information {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,.1);
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.book-image-container {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: #f5f5f5;
}

.book-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}
.book-name{
  font-family: 幼圆;
  font-size: 19px;
}
.book-price{
  display: flex;
  justify-content: center;
  font-size: 20px;
  font-weight: 600;
  color: red;
}
.book-author{
  font-style: italic;
}
.purchase-number{
  display: flex;
  justify-content: center;
  color: #8c939d;
  font-size: 12px;
}

.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}

.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
.description-container {
  position: relative;
  margin: 15px 0;
}

.description-content {
  font-size: 14px;
  color: #8c939d;
  line-height: 1.5;
  overflow: hidden;
  max-height: 120px;
  transition: max-height 0.3s ease;
}

.description-content.expanded {
  max-height: none;
}

.read-more-gradient {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 50px;
  background: linear-gradient(to bottom, rgba(255,255,255,0), rgba(255,255,255,1));
  pointer-events: none;
}

.read-more-container {
  text-align: center;
  margin-top: 10px;
}

.read-more-btn {
  color: #409EFF;
  cursor: pointer;
  font-size: 14px;
}

.read-more-btn:hover {
  text-decoration: underline;
}
</style>