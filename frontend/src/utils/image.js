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
 */
export const productImages = {
  // 鱼类 (FISH)
  'FI-SW-01': '/images/fish3.gif',
  'FI-SW-02': '/images/fish4.gif',
  'FI-FW-01': '/images/jinli.png',
  'FI-FW-02': '/images/jinyu.png',

  // 狗类 (DOGS)
  'K9-BD-01': '/images/labuladuo.png',
  'K9-CW-01': '/images/jinmao.png',
  'K9-PO-01': '/images/dog5.gif',
  'K9-RT-01': '/images/keji.png',

  // 猫类 (CATS)
  'FL-DSH-01': '/images/buoumao.png',
  'FL-DLH-01': '/images/cat1.gif',
  'FL-MC-01': '/images/yinduanlanmao.png',

  // 鸟类 (BIRDS)
  'AV-CB-01': '/images/yingwu.png',
  'AV-SB-01': '/images/jinsique.png',

  // 爬行类 (REPTILES)
  'RP-LI-01': '/images/lizard1.gif',
  'RP-SN-01': '/images/snake1.gif'
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
