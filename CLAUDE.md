# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

这是一个全栈在线书店 Web 应用程序，采用前后端分离架构：
- **后端**: Spring Boot 2.5.1 + Java 8
- **前端**: Vue.js 2 + Element UI
- **数据库**: MySQL 8.0.13 + MyBatis ORM
- **构建工具**: Maven (后端) + npm (前端)
- **核心依赖**: JWT认证、PageHelper分页、Hutool工具库、tk.mybatis通用Mapper

### 技术栈详情
- **认证授权**: JWT (Java JWT 3.10.3) + 基于角色的访问控制
- **分页插件**: PageHelper Spring Boot Starter 1.4.6
- **工具库**: Hutool 5.3.7, Apache POI 4.1.2 (Excel处理)
- **HTTP客户端**: Axios 1.8.1
- **UI框架**: Element UI 2.15.14

## 代码库结构

```
book-mall/
├── springboot/           # Spring Boot 后端
│   ├── src/main/java/    # Java 源代码
│   │   └── com.example.springboot/
│   │       ├── controller/   # REST API 接口
│   │       ├── service/      # 业务逻辑
│   │       ├── dao/          # 数据访问接口
│   │       ├── entity/       # 数据模型
│   │       ├── common/       # 工具类、配置、异常处理
│   │       │   ├── config/   # 配置类 (JWT, CORS等)
│   │       │   ├── Result.java    # 统一响应格式
│   │       │   └── ResultCode.java # 响应码枚举
│   │       └── SpringbootApplication.java  # 主应用类
│   ├── src/main/resources/
│   │   ├── application.yml   # 应用配置文件
│   │   └── mapper/           # MyBatis XML 映射文件
│   └── pom.xml               # Maven 依赖和构建配置
├── vue/                      # Vue.js 前端
│   ├── src/
│   │   ├── views/            # 页面组件 (manage/ 为管理端，front/ 为用户端)
│   │   ├── router/           # Vue Router 配置
│   │   ├── components/       # 可复用 UI 组件
│   │   ├── utils/            # 工具函数 (如 HTTP 客户端)
│   │   │   └── request.js    # Axios 配置和拦截器
│   │   └── assets/           # 静态资源
│   └── package.json          # npm 依赖和脚本
└── book_mall.sql             # 数据库 schema 和初始数据
```

## 开发命令

### 后端开发 (Spring Boot)

1. **安装后端依赖**:
   ```bash
   cd springboot
   mvn clean install
   ```

2. **运行后端应用**:
   ```bash
   cd springboot
   mvn spring-boot:run
   ```

3. **构建后端**:
   ```bash
   cd springboot
   mvn clean package
   ```

4. **运行后端测试**:
   ```bash
   cd springboot
   mvn test
   ```

5. **跳过测试构建**:
   ```bash
   cd springboot
   mvn clean package -DskipTests
   ```

### 前端开发 (Vue.js)

1. **安装前端依赖**:
   ```bash
   cd vue
   npm install
   ```

2. **运行前端应用**:
   ```bash
   cd vue
   npm run serve
   ```

3. **构建前端生产版本**:
   ```bash
   cd vue
   npm run build
   ```

4. **清理 node_modules**:
   ```bash
   cd vue
   rm -rf node_modules package-lock.json
   npm install
   ```

### 数据库设置

1. 导入数据库 schema:
   ```sql
   mysql -u [用户名] -p [数据库名] < book_mall.sql
   ```

2. **数据库配置**: 修改 `springboot/src/main/resources/application.yml` 中的数据库连接信息

## 架构模式

### 后端架构

- **分层架构**: Controller → Service → DAO
- **RESTful API**: 标准 HTTP 方法实现 CRUD 操作
- **JWT 认证**: 基于 Token 的认证拦截器 (`JwtInterceptor.java`)
- **MyBatis ORM**: 基于 XML 的 SQL 映射
- **PageHelper**: 分页支持
- **Hutool**: 常用工具库

### 前端架构

- **组件化**: Vue 单文件组件
- **Vue Router**: 客户端路由与路由守卫
- **Element UI**: UI 组件库
- **Axios**: HTTP 客户端与请求/响应拦截器 (`request.js`)
- **响应式设计**: 适配不同屏幕尺寸

## 已实现关键功能

1. **用户管理**: 注册、登录、个人资料管理
2. **管理面板**: 用户、管理员、图书、分类管理
3. **图书目录**: 浏览和搜索图书
4. **文件上传**: 头像和资源管理
5. **JWT 认证**: 安全的 API 访问
6. **分页功能**: 高效数据加载
7. **基于角色的访问控制**: 用户与管理员权限区分

## API 规范

- 基础 URL: `http://localhost:9090`
- JSON 响应格式: `{ "code": "0", "msg": "success", "data": {} }`
- 认证方式: 请求头中携带 Bearer Token
- 错误处理: 集中式异常处理，使用自定义错误码

## 数据库结构

主要表:
- `user`: 用户信息 (id, username, password, avatar, name, sex, age, phone, role, email)
- `admin`: 管理员信息 (id, username, password, phone, role, avatar, email, name)
- `notice`: 系统公告 (id, title, content, time, user_id)
- 数据库使用 MyISAM 和 InnoDB 引擎混合
- 默认管理员账户: admin/123456
- 默认用户账户: 123/123

## 后端结构详情

### Controller 层
- REST 控制器实现标准 CRUD 操作
- 使用 @GetMapping、@PostMapping、@PutMapping、@DeleteMapping 进行请求映射
- 返回统一格式的 Result 对象响应

### Service 层
- 业务逻辑实现
- 数据验证和错误处理
- 与 DAO 层集成

### DAO 层
- MyBatis 映射接口
- mapper 文件中的 XML SQL 查询
- 条件语句实现动态 SQL

### Entity 层
- 与数据库表映射的 Java 类
- JPA 注解用于 ORM 映射
- 继承模式 (例如 User 继承自 Account)

### 认证与安全
- JWT 拦截器 (`JwtInterceptor.java`) 验证请求
- JWT 工具类 (`JwtTokenUtils.java`) 处理 Token
- CORS 配置 (`CorsConfig.java`) 处理跨域请求

## 前端结构详情

### 视图层
- `manage/`: 管理面板组件
- `front/`: 用户界面组件
- 基于路由的懒加载与动态导入

### 路由
- Vue Router 嵌套路由
- 路由元信息用于导航
- 路由守卫实现认证

### 工具类
- Axios HTTP 客户端与拦截器 (`request.js`)
- 请求/响应处理与统一格式
- 基于 Token 的认证

## 配置文件

### 后端 (application.yml)
- 服务端口配置 (9090)
- 数据库连接设置
- MyBatis 映射文件位置
- PageHelper 分页配置
- 文件上传大小限制

### 前端 (vue.config.js, babel.config.js)
- Vue CLI 配置
- Babel 转译设置

## 开发注意事项

1. **后端开发**:
   - 所有 API 响应应使用 `Result` 类统一封装，成功返回 code "0"，失败返回 code "-1"
   - 控制器中使用 `@RequestBody` 接收 JSON 数据
   - 服务层处理业务逻辑，避免在控制器中写复杂逻辑
   - 使用 JWT 拦截器保护需要认证的接口，Token 通过请求头 "token" 传递
   - JWT Token 使用用户密码作为签名密钥，Token 格式为 "userId-role" 的 audience

2. **前端开发**:
   - 使用 Element UI 组件保持界面一致性
   - 通过 `request.js` 中的 Axios 拦截器处理认证，自动添加 token 到请求头
   - 路由守卫实现基于角色的访问控制：用户只能访问 front 路径，管理员只能访问 manage 路径
   - 组件命名遵循 PascalCase 规范
   - 用户信息存储在 localStorage 的 "userInfo" 和 "token" 中

3. **数据库操作**:
   - 使用 MyBatis XML 映射文件编写 SQL
   - 分页查询使用 PageHelper 插件
   - 注意 SQL 注入风险，使用参数化查询
   - 数据库表使用混合引擎：admin 和 user 表使用 MyISAM，notice 和 comment 表使用 InnoDB

## 关键架构细节

### 多角色认证系统
项目实现了基于角色的访问控制系统，支持三种用户角色：
- **admin**: 管理员，拥有所有管理权限
- **user**: 普通用户，只能访问用户端功能
- **merchant**: 商家角色（代码中已预留，但前端界面未完全实现）

JWT Token 的 audience 字段格式为 "userId-role"，拦截器会根据角色信息查询对应的用户表。

### 统一响应格式
所有 API 响应都使用 `Result` 类统一封装：
```java
{
  "code": "0",    // "0" 表示成功，"-1" 表示失败
  "msg": "success", // 响应消息
  "data": {}      // 响应数据
}
```

### 前端路由守卫机制
前端路由守卫实现了严格的权限控制：
- 用户角色为 "user" 时，访问 manage 路径会自动重定向到 front/index
- 非用户角色访问 front 路径会自动重定向到 manage/home
- 未登录用户访问除登录、注册、首页外的页面会被重定向到登录页

### 文件上传处理
项目支持文件上传功能，主要用于用户头像管理：
- 上传大小限制：单个文件 10MB，总请求 10MB
- 文件存储在服务器本地，通过 `http://localhost:9090/files/` 访问
- 文件路径存储在数据库的 avatar 字段中

### 数据库设计特点
- **用户表分离**: admin 和 user 表分别存储管理员和普通用户信息
- **评论系统**: comment 表支持嵌套评论（通过 parent_id 字段）
- **软删除**: comment 表包含 is_deleted 字段实现软删除功能
- **索引优化**: comment 表在 book_id、user_id、parent_id 字段上建立索引