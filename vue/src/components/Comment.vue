<template>
  <div class="comment-container">
    <div class="comment-header">
      <h3>评论区</h3>
      <span class="comment-count">共 {{ comments.length }} 条评论</span>
    </div>

    <!-- 评论输入区域 -->
    <div class="comment-input-area" v-if="isLoggedIn">
      <el-input
        v-model="newComment"
        type="textarea"
        :rows="3"
        placeholder="写下你的评论..."
        class="comment-input"
      />
      <div class="comment-input-actions">
        <el-button
          type="primary"
          @click="submitComment"
          :loading="submitting"
          :disabled="!newComment.trim()"
        >
          发表评论
        </el-button>
      </div>
    </div>

    <!-- 未登录提示 -->
    <div class="login-prompt" v-else>
      <el-alert
        title="请先登录后再发表评论"
        type="info"
        show-icon
        :closable="false"
      >
        <el-button type="text" @click="goToLogin">去登录</el-button>
      </el-alert>
    </div>

    <!-- 评论列表 -->
    <div class="comment-list">
      <div v-if="comments.length === 0" class="no-comments">
        <p>暂无评论，快来发表第一条评论吧！</p>
      </div>

      <div v-else class="comment-items">
        <div
          v-for="comment in comments"
          :key="comment.id"
          class="comment-item"
        >
          <div class="comment-avatar">
            <el-avatar
              :src="comment.userAvatar || defaultAvatar"
              :size="40"
            >
              {{ comment.username ? comment.username.charAt(0).toUpperCase() : 'U' }}
            </el-avatar>
          </div>

          <div class="comment-content">
            <div class="comment-meta">
              <span class="comment-author">{{ comment.username || '匿名用户' }}</span>
              <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
            </div>

            <div class="comment-text">
              {{ comment.content }}
            </div>

            <div class="comment-actions">
              <el-button
                type="text"
                size="small"
                @click="replyToComment(comment)"
              >
                回复
              </el-button>
            </div>

            <!-- 回复输入框 -->
            <div v-if="replyingTo === comment.id" class="reply-input-area">
              <el-input
                v-model="replyContent"
                type="textarea"
                :rows="2"
                :placeholder="`回复 ${comment.userName || '匿名用户'}：`"
                class="reply-input"
              />
              <div class="reply-actions">
                <el-button
                  type="primary"
                  size="small"
                  @click="submitReply(comment)"
                  :loading="submitting"
                  :disabled="!replyContent.trim()"
                >
                  回复
                </el-button>
                <el-button
                  type="text"
                  size="small"
                  @click="cancelReply"
                >
                  取消
                </el-button>
              </div>
            </div>

            <!-- 子评论回复列表 -->
            <div v-if="comment.replies && comment.replies.length > 0" class="replies-list">
              <div
                v-for="reply in comment.replies"
                :key="reply.id"
                class="reply-item"
              >
                <div class="reply-avatar">
                  <el-avatar
                    :src="reply.userAvatar || defaultAvatar"
                    :size="32"
                  >
                    {{ reply.userName ? reply.userName.charAt(0).toUpperCase() : 'U' }}
                  </el-avatar>
                </div>

                <div class="reply-content">
                  <div class="reply-meta">
                    <span class="reply-author">{{ reply.userName || '匿名用户' }}</span>
                    <span class="reply-time">{{ formatTime(reply.createTime) }}</span>
                  </div>

                  <div class="reply-text">
                    <span class="reply-to">回复 {{ reply.parentUserName || '匿名用户' }}：</span>
                    {{ reply.content }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'Comment',
  props: {
    bookId: {
      type: [Number, String],
      required: true
    }
  },
  data() {
    return {
      comments: [],
      newComment: '',
      replyContent: '',
      replyingTo: null,
      submitting: false,
      loading: false,
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      userInfo: null
    }
  },
  computed: {
    isLoggedIn() {
      return this.userInfo && this.userInfo.id
    }
  },
  created() {
    this.userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    this.loadComments()
  },
  methods: {
    loadComments() {
      request.get("/comment/select", {
        params:{
          BookId: this.bookId
        }
      }).then(res =>{
        if(res.code === '0'){
          this.comments = res.data;
          console.log(res.data);
          //遍历所有父评论，一个父评论一次循环，内循环遍历整个列表，遇到所属子评论将其包含入replies中
          //并将其从comments中除名
          for (let i = 0; i < res.data.length; i++) {
            if (this.comments[i].parentId != null)
              continue;
            let temp = 0;
            for (let j = 1; j < res.data.length; j++){
              if (this.comments[i].id == this.comments[j].parentId){
                if (!this.comments[i].replies) {
                  this.comments[i].replies = [];
                }
                this.comments[i].replies[temp++] = this.comments[j];
                this.comments.splice(j, 1);
                j--;
              }
            }//j
          }//i
        }
        else{
          this.$message.error(res.msg)
        }
      })
    },

    async submitComment() {
      if (!this.newComment.trim()) return

      try {
        this.submitting = true

        // 模拟API调用，实际项目中应该调用后端API
        const newCommentObj = {
          id: Date.now(),
          bookId: this.bookId,
          userId: this.userInfo.id,
          userName: this.userInfo.name || this.userInfo.username,
          userAvatar: this.userInfo.avatar,
          content: this.newComment.trim(),
          createTime: new Date(),
          parentId: null,
          replies: []
        }

        this.comments.unshift(newCommentObj)
        this.newComment = ''

        this.$message.success('评论发表成功')
      } catch (error) {
        console.error('发表评论失败:', error)
        this.$message.error('发表评论失败')
      } finally {
        this.submitting = false
      }
    },

    replyToComment(comment) {
      this.replyingTo = comment.id
      this.replyContent = ''
    },

    cancelReply() {
      this.replyingTo = null
      this.replyContent = ''
    },

    async submitReply(parentComment) {
      if (!this.replyContent.trim()) return

      try {
        this.submitting = true

        // 模拟API调用，实际项目中应该调用后端API
        const newReply = {
          id: Date.now(),
          bookId: this.bookId,
          userId: this.userInfo.id,
          userName: this.userInfo.name || this.userInfo.username,
          userAvatar: this.userInfo.avatar,
          content: this.replyContent.trim(),
          createTime: new Date(),
          parentId: parentComment.id,
          parentUserName: parentComment.userName || '匿名用户'
        }

        if (!parentComment.replies) {
          this.$set(parentComment, 'replies', [])
        }
        parentComment.replies.push(newReply)

        this.replyContent = ''
        this.replyingTo = null

        this.$message.success('回复成功')
      } catch (error) {
        console.error('回复失败:', error)
        this.$message.error('回复失败')
      } finally {
        this.submitting = false
      }
    },

    goToLogin() {
      this.$router.push('/login')
    },

    formatTime(time) {
      if (!time) return ''

      const date = new Date(time)
      const now = new Date()
      const diff = now - date

      if (diff < 60000) {
        return '刚刚'
      } else if (diff < 3600000) {
        return Math.floor(diff / 60000) + '分钟前'
      } else if (diff < 86400000) {
        return Math.floor(diff / 3600000) + '小时前'
      } else if (diff < 2592000000) {
        return Math.floor(diff / 86400000) + '天前'
      } else {
        return date.toLocaleDateString()
      }
    },
  }
}
</script>

<style scoped>
.comment-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.comment-header h3 {
  margin: 0;
  color: #303133;
  font-size: 18px;
}

.comment-count {
  color: #909399;
  font-size: 14px;
}

.comment-input-area {
  margin-bottom: 30px;
}

.comment-input {
  margin-bottom: 15px;
}

.comment-input-actions {
  text-align: right;
}

.login-prompt {
  margin-bottom: 30px;
}

.login-prompt .el-button {
  margin-left: 10px;
}

.comment-list {
  margin-top: 20px;
}

.no-comments {
  text-align: center;
  padding: 40px;
  color: #909399;
  background: #fafafa;
  border-radius: 4px;
}

.comment-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  display: flex;
  gap: 15px;
  padding: 15px;
  background: #fafafa;
  border-radius: 8px;
  border: 1px solid #ebeef5;
}

.comment-avatar {
  flex-shrink: 0;
}

.comment-content {
  flex: 1;
}

.comment-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.comment-author {
  font-weight: 600;
  color: #303133;
}

.comment-time {
  color: #909399;
  font-size: 12px;
}

.comment-text {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 10px;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.comment-actions {
  display: flex;
  gap: 10px;
}

.reply-input-area {
  margin-top: 15px;
  padding: 15px;
  background: #fff;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}

.reply-input {
  margin-bottom: 10px;
}

.reply-actions {
  display: flex;
  gap: 10px;
}

.replies-list {
  margin-top: 15px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.reply-item {
  display: flex;
  gap: 10px;
  padding: 10px;
  background: #fff;
  border-radius: 4px;
  border: 1px solid #ebeef5;
}

.reply-avatar {
  flex-shrink: 0;
}

.reply-content {
  flex: 1;
}

.reply-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 5px;
}

.reply-author {
  font-weight: 500;
  color: #606266;
  font-size: 14px;
}

.reply-time {
  color: #c0c4cc;
  font-size: 11px;
}

.reply-text {
  color: #606266;
  line-height: 1.5;
  font-size: 14px;
}

.reply-to {
  color: #409eff;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .comment-container {
    padding: 15px;
  }

  .comment-item {
    flex-direction: column;
  }

  .comment-avatar {
    align-self: flex-start;
  }

  .reply-item {
    flex-direction: column;
  }

  .reply-avatar {
    align-self: flex-start;
  }
}
</style>