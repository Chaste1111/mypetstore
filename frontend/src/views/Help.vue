<template>
  <div class="help-container">
      <div class="container">
        <h1>帮助中心</h1>
        
        <div class="help-sections">
          <section class="help-section">
            <h2>常见问题</h2>
            <div class="faq-item">
              <h3>如何注册账户？</h3>
              <p>点击页面右上角的"注册"按钮，填写相关信息即可完成注册。</p>
            </div>
            
            <div class="faq-item">
              <h3>如何购买商品？</h3>
              <p>浏览商品页面，选择您喜欢的商品加入购物车，然后结算完成购买。</p>
            </div>
            
            <div class="faq-item">
              <h3>如何查看订单状态？</h3>
              <p>登录后，进入"我的订单"页面即可查看所有订单的状态和详情。</p>
            </div>
            
            <div class="faq-item">
              <h3>如何修改个人信息？</h3>
              <p>登录后，进入"个人中心"页面，点击"编辑资料"即可修改个人信息。</p>
            </div>
          </section>
          
          <section class="help-section">
            <h2>联系我们</h2>
            <div class="contact-info">
              <p><strong>客服电话：</strong>400-123-4567</p>
              <p><strong>客服邮箱：</strong>support@mypetstore.com</p>
              <p><strong>工作时间：</strong>周一至周五 9:00-18:00</p>
            </div>
          </section>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.help-container {
  min-height: calc(100vh - 60px);
  padding: var(--spacing-xl) 0;
}

.help-container h1 {
  text-align: center;
  font-size: 32px;
  margin-bottom: var(--spacing-xl);
  color: var(--text-primary);
}

.help-sections {
  max-width: 800px;
  margin: 0 auto;
}

.help-section {
  background: var(--bg-primary);
  border-radius: var(--radius-md);
  padding: var(--spacing-xl);
  margin-bottom: var(--spacing-lg);
  box-shadow: var(--shadow-sm);
}

.help-section h2 {
  font-size: 24px;
  margin-bottom: var(--spacing-lg);
  color: var(--primary-color);
}

.faq-item {
  margin-bottom: var(--spacing-lg);
}

.faq-item h3 {
  font-size: 18px;
  margin-bottom: var(--spacing-sm);
  color: var(--text-primary);
}

.faq-item p {
  color: var(--text-secondary);
  line-height: 1.6;
}

.contact-info p {
  margin-bottom: var(--spacing-sm);
  color: var(--text-secondary);
}

.contact-info strong {
  color: var(--text-primary);
}
</style>
