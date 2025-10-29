import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

//创建路由规则
const routes = [
  {
    path: '/manage',
    name: 'home',
    component: () => import('../views/Manage.vue'),
    redirect: '/home',  // 重定向到主页
    children:[
      { path:'user',name:'User',meta: { name: '用户信息',title: '用户信息'  },component:() => import('../views/manage/User') },
      { path:'admin',name:'Admin',meta: { name: '管理员信息',title: '管理员信息'},component:() => import('../views/manage/Admin') },
      { path: 'type', name: 'Type', meta: { name: '分类信息' ,title: '分类信息'}, component: () => import('../views/manage/Type')},
      { path:'merchant',name:'Merchant',meta:{name:'商家信息',title:'商家信息'},component:() => import('../views/manage/Merchant') },
      { path: 'book', name: 'Book', meta: { name: '图书信息' ,title: '图书信息'}, component: () => import('../views/manage/Book') },
      { path: 'notice', name: 'Notice', meta: { name: '公告信息' ,title: '公告信息'}, component: () => import('../views/manage/Notice') },
      { path:'home',name:'Home',meta: { name: '首页' ,title: '首页'},component: () => import('../views/manage/Home')  },
      { path: 'personInfo', name: 'PersonInfo', meta: { name: '个人信息',title: '个人信息' }, component: () => import('../views/manage/PersonInfo')  },
      { path: 'password', name: 'Password', meta: { name: '修改密码',title: '修改密码' }, component:() => import('../views/manage/Password')  },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'index', name: 'Index', meta: { name: '系统首页',title: '系统首页' }, component:() => import('../views/front/Index') },
      { path: 'notice', name: 'Notice', meta: { name: '修改密码' ,title: '修改密码'}, component:() => import('../views/front/Notice')  },
      { path: 'book', name: 'Book', meta: { name: '图书信息', title: '图书信息' }, component: () => import('../views/front/Book') },
      { path: 'bookDetail', name: 'BookDetail', meta: { name: '图书详情', title: '图书详情' }, component: () => import('../views/front/BookDetail') },
      { path: 'center',
        name: 'Center',
        component:() => import('../views/front/Center.vue'),
        redirect: '/front/center/userInfo',
        children: [
          { path: 'password', name: 'Password', meta: { name: '修改密码' ,title: '修改密码'}, component:() => import('../views/front/center/Password.vue')  },
          { path: 'userInfo', name: 'UserInfo', meta: { name: '个人信息' ,title: '个人信息'}, component:() => import('../views/front/center/UserInfo.vue')  },

        ]
      },
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录'  },
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { title: '注册'  },
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

// 路由守卫
router.beforeEach((to ,from, next) => {
  document.title = '网上书城';
  if (to.path === '/') {
    return next("/login");
  }
  let user = JSON.parse(localStorage.getItem("userInfo") || '{}');
  //front有页面 打开下方的
  if (to.path.includes("manage")&&user.role==='user'){
    next('/front/index')
  }
  if (to.path.includes("front")&&user.role!='user'){
    next('/manage/home')
  }
  if (  to.path !== '/login' && to.path !== '/register' && to.path !== '/front/index') {
    if (user==null||user==""){
      next("/login");}
  }
  next();
})
export default router
