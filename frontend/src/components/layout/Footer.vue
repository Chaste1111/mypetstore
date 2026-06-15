<template>
  <footer class="footer">
    <div class="container">
      <div class="footer-content">
        <div class="footer-section">
          <router-link to="/about" class="footer-title-link"><h3>关于我们</h3></router-link>
          <p>MyPetStore是您值得信赖的宠物商店，提供各类优质宠物及相关用品。</p>
        </div>

        <div class="footer-section">
          <router-link to="/help" class="footer-title-link"><h3>快速链接</h3></router-link>
          <ul>
            <li><router-link to="/">首页</router-link></li>
            <li><router-link to="/help">帮助中心</router-link></li>
            <li><router-link to="/account">个人中心</router-link></li>
          </ul>
        </div>

        <div class="footer-section">
          <router-link to="/about" class="footer-title-link"><h3>联系方式</h3></router-link>
          <ul>
            <li>电话: 400-123-4567</li>
            <li>邮箱: support@mypetstore.com</li>
            <li>地址: 中国某市某区某街道</li>
          </ul>
        </div>

        <div class="footer-section">
          <router-link to="/about" class="footer-title-link"><h3>关注我们</h3></router-link>
          <div class="social-links">
            <el-icon :size="24"><ChatDotRound /></el-icon>
            <el-icon :size="24"><Message /></el-icon>
            <el-icon :size="24"><Phone /></el-icon>
          </div>
        </div>
      </div>

      <div class="footer-bottom">
        <p>&copy; 2026 MyPetStore. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<script setup>
</script>

<style scoped>
.footer {
  background: var(--text-primary);
  color: #fff;
  padding: var(--spacing-xxl) 0 var(--spacing-lg);
  margin-top: auto;
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-xl);
}

.footer-title-link { text-decoration: none; color: inherit; }
.footer-section h3 {
  font-size: 16px;
  margin-bottom: var(--spacing-md);
  color: var(--primary-light);
}

.footer-section p {
  font-size: 14px;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.8);
}

.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: var(--spacing-sm);
}

.footer-section ul li a {
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  transition: color var(--transition-fast);
}

.footer-section ul li a:hover {
  color: var(--primary-light);
}

.social-links {
  display: flex;
  gap: var(--spacing-md);
}

.social-links .el-icon {
  cursor: pointer;
  transition: transform var(--transition-fast);
}

.social-links .el-icon:hover {
  transform: scale(1.2);
  color: var(--primary-light);
}

.footer-bottom {
  text-align: center;
  padding-top: var(--spacing-lg);
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
}
</style>
