<template>
  <div class="book-container">
    <!-- 搜索区域 -->
    <div class="search-section">
      <div class="search-box">
        <el-input
          class="search-input"
          v-model="searchQuery"
          placeholder="搜索图书..."
          suffix-icon="el-icon-search"
          @keyup.enter.native="searchBooks">
        </el-input>
<!--        suffix-icon 框尾部图标，keyup.enter.native回车键触发，当组件是html原生组件时不需要.native-->
        <el-button type="primary" class="search-button" @click="searchBooks">搜索</el-button>
      </div>

      <!-- 分类筛选 -->
      <div class="filter-section">
        <div class="filter-title">分类筛选:</div>
        <div class="filter-tags">
          <el-tag
            v-for="type in typeData"
            :key="type.id"
            :type="selectedType === type.id ? 'primary' : ''"
            @click="filterByType(type.id)"
            class="filter-tag">
            {{ type.name }}
          </el-tag>
          <el-tag
            @click="filterByType(null)"
            :type="selectedType === null ? 'primary' : ''"
            class="filter-tag">
            全部
          </el-tag>
        </div>
      </div>
    </div>

    <!-- 图书列表 -->
    <div class="book-list">
      <div v-if="bookData.length === 0" class="no-data">
        暂无图书数据
      </div>

      <div v-else class="book-grid">
        <div
          v-for="book in bookData"
          :key="book.id"
          class="book-card"
          @click="viewBookDetail(book)">
          <div class="book-cover">
            <img :src="book.pic || defaultCover" :alt="book.name" class="cover-img">
          </div>
          <div class="book-info">
            <div class="book-title">{{ book.name }}</div>
            <div class="book-author">{{ book.author }}</div>
            <div class="book-publisher">{{ book.publisher }}</div>
            <div class="book-price">¥{{ book.price }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-section" v-if="total > 0">
      <el-pagination
        background
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-size="pageSize"
        layout="prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  data() {
    return {
      bookData: [],
      typeData: [],
      searchQuery: '',
      selectedType: null,
      pageNum: 1,
      pageSize: 20,
      total: 0,
      defaultCover: require('@/assets/img/carousel_2.png')
    }
  },
  created() {
    this.loadData();
    this.loadTypeData();
  },
  methods: {
    loadData() {
      request.get('/book/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.searchQuery,
          typeId: this.selectedType
        }
      }).then(res => {
        if (res.code === '0') {
          this.bookData = res.data.list;
          this.total = res.data.total;
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    loadTypeData() {
      request.get('/type/selectAll').then(res => {
        if (res.code === '0') {
          this.typeData = res.data;
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    searchBooks() {
      request.get('/book/search', {
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.searchQuery,
        }
      }).then(res =>{
        if (res.code === '0') {
          this.bookData = res.data.list;
          this.total = res.data.total;
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    filterByType(typeId) {
      this.selectedType = typeId;
      this.pageNum = 1;
      request.get('/book/filterPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.searchQuery,
          typeId: this.selectedType
        }
      }).then(res => {
        if (res.code === '0') {
          this.bookData = res.data.list;
          this.total = res.data.total;
        } else {
          this.$message.error(res.msg);
        }
      });
    },

    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.loadData();
    },

    viewBookDetail(book) {
      // 这里可以跳转到图书详情页
      console.log('查看图书详情:', book);
      // 以对象形式传过去时，这段数据会因为route.query参数只支持字符串序列化导致对象数据丢失，即使初始化调用对象ID访问数据库也来不及
      // 必须使用这种方法保留数据
      this.$router.push({path:"BookDetail", query:{book:JSON.stringify(book)}});
    },

    resetSearch() {
      this.searchQuery = '';
      this.selectedType = null;
      this.pageNum = 1;
      this.loadData();
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

.search-section {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,.1);
}

.search-box {
  display: flex;
  margin-bottom: 20px;
}

.search-input {
  flex: 1;
  margin-right: 10px;
}

.search-button {
  width: 100px;
}

.filter-section {
  display: flex;
  align-items: center;
}

.filter-title {
  font-size: 14px;
  color: #666;
  margin-right: 15px;
  white-space: nowrap;
}

.filter-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.filter-tag {
  cursor: pointer;
  transition: all 0.3s;
}

.filter-tag:hover {
  opacity: 0.8;
}

.book-list {
  margin-bottom: 30px;
}

.no-data {
  text-align: center;
  padding: 50px 0;
  color: #999;
  font-size: 16px;
}

.book-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 20px;
}

.book-card {
  background: #fff;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,.1);
  transition: all 0.3s;
  cursor: pointer;
}

.book-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,.15);
}

.book-cover {
  height: 240px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
}

.cover-img {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.book-info {
  padding: 12px;
}

.book-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 6px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 36px;
}

.book-author {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.book-publisher {
  font-size: 12px;
  color: #999;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.book-price {
  font-size: 16px;
  font-weight: 600;
  color: #e64545;
}

.pagination-section {
  display: flex;
  justify-content: center;
  padding: 20px 0;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .book-container {
    padding: 10px;
  }

  .book-grid {
    grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
    gap: 15px;
  }

  .book-cover {
    height: 180px;
  }

  .search-box {
    flex-direction: column;
  }

  .search-input {
    margin-right: 0;
    margin-bottom: 10px;
  }
}
</style>
