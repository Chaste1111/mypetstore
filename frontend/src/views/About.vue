<template>
  <div class="about-page">
    <div class="about-hero">
      <h1>关于我们</h1>
      <p>MyPetStore — 您值得信赖的宠物商店</p>
    </div>
    <div class="about-content">
      <section>
        <h2>我们的故事</h2>
        <p>MyPetStore 成立于 2020 年，致力于为中国家庭提供健康、优质的宠物及相关用品。从鱼类到犬类，从猫咪到鸟类，我们精选每一只宠物，确保它们健康快乐地来到您的家庭。</p>
      </section>
      <section>
        <h2>我们的承诺</h2>
        <ul>
          <li>所有宠物均经过专业兽医检查</li>
          <li>提供30天健康保障</li>
          <li>专业售后指导服务</li>
          <li>7x24 小时在线客服</li>
        </ul>
      </section>
      <section>
        <h2>联系方式</h2>
        <p>电话: 400-123-4567</p>
        <p>邮箱: support@mypetstore.com</p>
      </section>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.about-page {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px 20px;
}
.about-hero {
  text-align: center;
  padding: 60px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 12px;
  margin-bottom: 40px;
}
.about-hero h1 { font-size: 36px; margin-bottom: 12px; }
.about-content section {
  margin-bottom: 40px;
}
.about-content h2 {
  font-size: 24px;
  margin-bottom: 16px;
  color: #333;
  border-bottom: 2px solid #667eea;
  padding-bottom: 8px;
}
.about-content p {
  font-size: 16px;
  line-height: 1.8;
  color: #666;
}
.about-content ul {
  list-style: none;
  padding: 0;
}
.about-content li {
  padding: 8px 0;
  font-size: 16px;
  color: #666;
}
.about-content li::before {
  content: "✓ ";
  color: #67c23a;
  font-weight: bold;
}
</style>
