import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

import './assets/css/global.css';
import App from './App.vue';
import router from './router';
import { Message } from 'element-ui';

ElementUI.Dialog.props.lockScroll.default = false;
Vue.use(ElementUI, { size: 'small' });

new Vue({
  router,
  el: '#app',
  render: h => h(App)
});
//title动态设置
/*router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})*/
Vue.prototype.$message = function(msg) {
  return Message({
    message: msg,
    duration: 1500
  });
};
// 分别对success、warning和error等样式进行设置
Vue.prototype.$message.success = function(msg) {
  return Message.success({
    message: msg,
    duration: 1500
  });
};
Vue.prototype.$message.warning = function(msg) {
  return Message.warning({
    message: msg,
    duration: 1500
  });
};
Vue.prototype.$message.error = function(msg) {
  return Message.error({
    message: msg,
    duration: 1500
  });
};
