/**
 * 产品分类图标映射
 */
export const categoryIcons = {
  'FISH': '/images/fish_icon.gif',
  'DOGS': '/images/dogs_icon.gif',
  'CATS': '/images/cats_icon.gif',
  'BIRDS': '/images/birds_icon.gif',
  'REPTILES': '/images/reptiles_icon.gif'
}

/**
 * 产品图片映射表
 * 根据产品ID返回对应的图片路径
 * 优先使用 PNG 格式（更清晰）
 */
export const productImages = {
  // 鱼类 (FISH)
  'FI-SW-01': '/images/jinli.png',        // 锦鲤
  'FI-SW-02': '/images/jinyu.png',        // 金鱼
  'FI-FW-01': '/images/fish3.gif',        // 其他鱼类（备用）
  'FI-FW-02': '/images/fish4.gif',        // 其他鱼类（备用）
  
  // 狗类 (DOGS)
  'K9-BD-01': '/images/jinmao.png',       // 金毛犬
  'K9-BD-02': '/images/labuladuo.png',    // 拉布拉多
  'K9-RT-01': '/images/keji.png',         // 柯基犬
  'K9-RT-02': '/images/hashiqi.png',      // 哈士奇
  'K9-DL-01': '/images/dog5.gif',         // 其他犬类（备用）
  'K9-DL-02': '/images/dog6.gif',         // 其他犬类（备用）
  
  // 猫类 (CATS)
  'AV-CB-01': '/images/yinduanlanmao.png', // 英短蓝猫
  'AV-CB-02': '/images/buoumao.png',      // 布偶猫
  'AV-CB-03': '/images/jumao.png',        // 橘猫
  'FL-DSH-01': '/images/cat1.gif',        // 其他猫类（备用）
  'FL-DSH-02': '/images/cat2.gif',        // 其他猫类（备用）
  'FL-DLH-01': '/images/cat1.gif',        // 其他猫类（备用）
  'FL-DLH-02': '/images/cat2.gif',        // 其他猫类（备用）
  
  // 鸟类 (BIRDS)
  'AV-SB-01': '/images/yingwu.png',       // 鹦鹉
  'AV-SB-02': '/images/jinsique.png',     // 金丝雀
  'AV-SB-03': '/images/wenniao.png',      // 文鸟
  
  // 爬行类 (REPTILES)
  'RP-LI-01': '/images/lizard1.gif',      // 蜥蜴（暂无PNG）
  'RP-SN-01': '/images/snake1.gif'        // 蛇（暂无PNG）
}

/**
 * Banner 图片映射
 */
export const bannerImages = {
  'FISH': '/images/banner_fish.gif',
  'DOGS': '/images/banner_dogs.gif',
  'CATS': '/images/banner_cats.gif',
  'BIRDS': '/images/banner_birds.gif',
  'REPTILES': '/images/banner_reptiles.gif'
}

/**
 * 获取产品图片路径
 * @param {string} productId - 产品ID
 * @returns {string} 图片路径
 */
export function getProductImage(productId) {
  return productImages[productId] || '/images/splash.gif'
}

/**
 * 获取分类图标路径
 * @param {string} categoryId - 分类ID
 * @returns {string} 图标路径
 */
export function getCategoryIcon(categoryId) {
  return categoryIcons[categoryId] || '/images/fish_icon.gif'
}

/**
 * 获取分类Banner路径
 * @param {string} categoryId - 分类ID
 * @returns {string} Banner路径
 */
export function getCategoryBanner(categoryId) {
  return bannerImages[categoryId] || '/images/splash.gif'
}
