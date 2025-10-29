<div class="comment-container">                                                                             
       <div class="comment-header">                                                                
│╎   4          <h3>评论区</h3>                                                                           
│╎   5 -        <span class="comment-count">共 {{ comments.length }} 条评论</span>                        
│╎   5 +        <span class="comment-count">共 {{ getTotalCommentCount() }} 条评论</span>                 
│╎   6        </div>                                                                                      
│╎   7                                                                                                    
│╎   8        <!-- 评论输入区域 -->                                                                       
│╎ ...                                                                                                    
│╎   46                                                                                                   
│╎   47          <div v-else class="comment-items">                                                       
│╎   48            <div                                                                                   
│╎   49 -            v-for="comment in comments"                                                          
│╎   49 +            v-for="comment in topLevelComments"                                                  
│╎   50              :key="comment.id"                                                                    
│╎   51              class="comment-item"                                                                 
│╎   52            >                                                                                      
│╎ ...                                                                                                    
│╎   75                    size="small"                                                                   
│╎   76                    @click="replyToComment(comment)"                                               
│╎   77                  >                                                                                
│╎   78 +                  <i class="el-icon-chat-dot-round"></i>                                         
│╎   79                    回复                                                                           
│╎   80                  </el-button>                                                                     
│╎   81                </div>                                                                             
│╎ ...                                                                                                    
│╎   85                    v-model="replyContent"                                                         
│╎   86                    type="textarea"                                                                
│╎   87                    :rows="2"                                                                      
│╎   88 -                  :placeholder="`回复 ${comment.userName || '匿名用户'}：`"                      
│╎   88 +                  :placeholder="`回复 @${comment.username || '匿名用户'}：`"                     
│╎   89                    class="reply-input"                                                            
│╎   90 +                  ref="replyInput"                                                               
│╎   91                  />                                                                               
│╎   92                  <div class="reply-actions">                                                                  ╎│
│╎   93                    <el-button                                                                                 ╎│
│╎ ...                                                                                                                ╎│
│╎   114                    v-for="reply in comment.replies"                                                          ╎│
│╎   115                    :key="reply.id"                                                                           ╎│
│╎   116                    class="reply-item"                                                                        ╎│
│╎   117 +                  :style="{ marginLeft: '0px' }"                                                            ╎│
│╎   118                  >                                                                                           ╎│
│╎   119                    <div class="reply-avatar">                                                                ╎│
│╎   120                      <el-avatar                                                                              ╎│
│╎   121                        :src="reply.userAvatar || defaultAvatar"                                              ╎│
│╎   122 -                      :size="30"                                                                            ╎│
│╎   122 +                      :size="32"                                                                            ╎│
│╎   123                      >                                                                                       ╎│
│╎   124 -                      {{ reply.userName ? reply.userName.charAt(0).toUpperCase() : 'U' }}                   ╎│
│╎   124 +                      {{ reply.username ? reply.username.charAt(0).toUpperCase() : 'U' }}                   ╎│
│╎   125                      </el-avatar>                                                                            ╎│
│╎   126                    </div>                                                                                    ╎│
│╎   127                                                                                                              ╎│
│╎   128                    <div class="reply-content">                                                               ╎│
│╎   129                      <div class="reply-meta">                                                                ╎│
│╎   130 -                      <span class="reply-author">{{ reply.userName || '匿名用户' }}</span>                  ╎│
│╎   130 +                      <span class="reply-author">{{ reply.username || '匿名用户' }}</span>                  ╎│
│╎   131                        <span class="reply-time">{{ formatTime(reply.createTime) }}</span>                    ╎│
│╎   132                      </div>                                                                                  ╎│
│╎   133                                                                                                              ╎│
│╎   134                      <div class="reply-text">                                                                ╎│
│╎   135 -                      <span class="reply-to">回复 {{ reply.parentUserName || '匿名用户' }}：</span>         ╎│
│╎   135 +                      <span v-if="reply.parentUsername" class="reply-to">                                   ╎│
│╎   136 +                        回复 <span class="reply-target">@{{ reply.parentUsername }}</span>：                ╎│
│╎   137 +                      </span>                                                                               ╎│
│╎   138                        {{ reply.content }}                                                                   ╎│
│╎   139                      </div>                                                                                  ╎│
│╎   140 +                                                                                                            ╎│
│╎   141 +                    <div class="reply-actions">                                                             ╎│
│╎   142 +                      <el-button                                                                            ╎│
│╎   143 +                        type="text"                                                                         ╎│
│╎   144 +                        size="small"                                                                        ╎│
│╎   145 +                        @click="replyToComment(reply)"                                                      ╎│
│╎   146 +                      >                                                                                     ╎│
│╎   147 +                        <i class="el-icon-chat-dot-round"></i>                                              ╎│
│╎   148 +                        回复                                                                                ╎│
│╎   149 +                      </el-button>                                                                          ╎│
│╎   150 +                    </div>                                                                                  ╎│
│╎   151 +                                                                                                            ╎│
│╎   152 +                    <!-- 对回复的回复输入框 -->                                                             ╎│
│╎   153 +                    <div v-if="replyingTo === reply.id" class="reply-input-area">                           ╎│
│╎   154 +                      <el-input                                                                             ╎│
│╎   155 +                        v-model="replyContent"                                                              ╎│
│╎   156 +                        type="textarea"                                                                     ╎│
│╎   157 +                        :rows="2"                                                                           ╎│
│╎   158 +                        :placeholder="`回复 @${reply.username || '匿名用户'}：`"                            ╎│
│╎   159 +                        class="reply-input"                                                                 ╎│
│╎   160 +                        ref="replyInput"                                                                    ╎│
│╎   161 +                      />                                                                                    ╎│
│╎   162 +                      <div class="reply-actions">                                                           ╎│
│╎   163 +                        <el-button                                                                          ╎│
│╎   164 +                          type="primary"                                                                    ╎│
│╎   165 +                          size="small"                                                                      ╎│
│╎   166 +                          @click="submitReply(reply)"                                                       ╎│
│╎   167 +                          :loading="submitting"                                                             ╎│
│╎   168 +                          :disabled="!replyContent.trim()"                                                  ╎│
│╎   169 +                        >                                                                                   ╎│
│╎   170 +                          回复                                                                              ╎│
│╎   171 +                        </el-button>                                                                        ╎│
│╎   172 +                        <el-button                                                                          ╎│
│╎   173 +                          type="text"                                                                       ╎│
│╎   174 +                          size="small"                                                                      ╎│
│╎   175 +                          @click="cancelReply"                                                              ╎│
│╎   176 +                        >                                                                                   ╎│
│╎   177 +                          取消                                                                              ╎│
│╎   178 +                        </el-button>                                                                        ╎│
│╎   179 +                      </div>                                                                                ╎│
│╎   180 +                    </div>                                                                                  ╎│
│╎   181                    </div>                                                                                    ╎│
│╎   182                  </div>                                                                                      ╎│
│╎   183                </div>                          