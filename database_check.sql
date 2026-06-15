-- ============================================================
-- MyPetStore 数据库完整检查和初始化脚本
-- 数据库：mypetstore
-- 字符集：utf8mb4
-- 引擎：InnoDB
-- 共计：12 张表
-- ============================================================

-- ============================================================
-- 1. 创建数据库（如果不存在）
-- ============================================================
CREATE DATABASE IF NOT EXISTS mypetstore
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE mypetstore;

-- ============================================================
-- 2. 分类表 (CATEGORY)
--    存储商品分类信息（如鱼类、狗狗、猫咪、鸟类）
-- ============================================================
CREATE TABLE IF NOT EXISTS CATEGORY (
    CATEGORYID VARCHAR(80) PRIMARY KEY COMMENT '分类ID',
    NAME       VARCHAR(80)  NOT NULL COMMENT '分类名称',
    `DESC`     VARCHAR(255) COMMENT '分类描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 3. 产品表 (PRODUCT)
--    存储产品信息，每个产品属于一个分类
-- ============================================================
CREATE TABLE IF NOT EXISTS PRODUCT (
    PRODUCTID VARCHAR(80) PRIMARY KEY COMMENT '产品ID',
    CATEGORY  VARCHAR(80) NOT NULL COMMENT '所属分类ID',
    NAME      VARCHAR(80) NOT NULL COMMENT '产品名称',
    `DESC`    VARCHAR(255) COMMENT '产品描述',
    FOREIGN KEY (CATEGORY) REFERENCES CATEGORY(CATEGORYID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 4. 商品表 (ITEM)
--    存储具体商品SKU信息（一个产品可有多个商品变体）
--    STATUS: P=有货, D=下架
-- ============================================================
CREATE TABLE IF NOT EXISTS ITEM (
    ITEMID    VARCHAR(80) PRIMARY KEY COMMENT '商品ID',
    PRODUCTID VARCHAR(80) NOT NULL COMMENT '所属产品ID',
    LISTPRICE DECIMAL(10,2) NOT NULL COMMENT '标价',
    UNITCOST  DECIMAL(10,2) NOT NULL COMMENT '单位成本',
    SUPPLIER  VARCHAR(80) COMMENT '供应商',
    STATUS    VARCHAR(2) DEFAULT 'P' COMMENT '状态(P=在售/D=下架)',
    ATTR1     VARCHAR(80) COMMENT '属性1',
    ATTR2     VARCHAR(80) COMMENT '属性2',
    ATTR3     VARCHAR(80) COMMENT '属性3',
    ATTR4     VARCHAR(80) COMMENT '属性4',
    ATTR5     VARCHAR(80) COMMENT '属性5',
    FOREIGN KEY (PRODUCTID) REFERENCES PRODUCT(PRODUCTID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 5. 库存表 (INVENTORY)
--    存储每个商品的库存数量
-- ============================================================
CREATE TABLE IF NOT EXISTS INVENTORY (
    ITEMID VARCHAR(80) PRIMARY KEY COMMENT '商品ID',
    QTY    INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    FOREIGN KEY (ITEMID) REFERENCES ITEM(ITEMID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 6. 用户账户表 (ACCOUNT)
--    存储用户账户及个人信息（合并了 SIGNON 和 PROFILE）
--    STATUS: P=正常, L=锁定
-- ============================================================
CREATE TABLE IF NOT EXISTS ACCOUNT (
    USERID       VARCHAR(80) PRIMARY KEY COMMENT '用户名',
    PASSWORD     VARCHAR(80) NOT NULL COMMENT '密码',
    EMAIL        VARCHAR(80) NOT NULL COMMENT '邮箱',
    FIRSTNAME    VARCHAR(80) COMMENT '名字',
    LASTNAME     VARCHAR(80) COMMENT '姓氏',
    STATUS       VARCHAR(2) DEFAULT 'P' COMMENT '账户状态(P=正常/L=锁定)',
    ADDR1        VARCHAR(255) COMMENT '地址1',
    ADDR2        VARCHAR(255) COMMENT '地址2',
    CITY         VARCHAR(80) COMMENT '城市',
    STATE        VARCHAR(80) COMMENT '州/省份',
    ZIP          VARCHAR(20) COMMENT '邮政编码',
    COUNTRY      VARCHAR(80) COMMENT '国家',
    PHONE        VARCHAR(20) COMMENT '电话号码',
    FAVCATEGORY  VARCHAR(80) COMMENT '收藏的分类ID',
    LANGPREF     VARCHAR(80) DEFAULT 'CN' COMMENT '语言偏好',
    MYLISTOPT    INT DEFAULT 1 COMMENT '是否显示我的列表(1=是/0=否)',
    BANNEROPT    INT DEFAULT 1 COMMENT '是否显示横幅(1=是/0=否)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 7. 横幅数据表 (BANNERDATA)
--    存储各分类对应的横幅图片名称
-- ============================================================
CREATE TABLE IF NOT EXISTS BANNERDATA (
    FAVCATEGORY VARCHAR(80) PRIMARY KEY COMMENT '收藏分类ID',
    BANNERNAME  VARCHAR(255) COMMENT '横幅图片名称',
    FOREIGN KEY (FAVCATEGORY) REFERENCES CATEGORY(CATEGORYID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 8. 购物车表 (CART)
--    存储用户购物车，每个用户一个购物车
-- ============================================================
CREATE TABLE IF NOT EXISTS CART (
    CARTID VARCHAR(80) PRIMARY KEY COMMENT '购物车ID（即用户名）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 9. 购物车项表 (CARTITEM)
--    存储购物车中的商品项
-- ============================================================
CREATE TABLE IF NOT EXISTS CARTITEM (
    CARTID   VARCHAR(80) NOT NULL COMMENT '购物车ID',
    ITEMID   VARCHAR(80) NOT NULL COMMENT '商品ID',
    QUANTITY INT NOT NULL DEFAULT 1 COMMENT '数量',
    PRIMARY KEY (CARTID, ITEMID),
    FOREIGN KEY (CARTID) REFERENCES CART(CARTID),
    FOREIGN KEY (ITEMID) REFERENCES ITEM(ITEMID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 10. 订单表 (ORDERS)
--    存储用户订单信息
--    STATUS: P=待处理, C=已确认, S=已发货, D=已完成, X=已取消
-- ============================================================
CREATE TABLE IF NOT EXISTS ORDERS (
    ORDERID         VARCHAR(80) PRIMARY KEY COMMENT '订单ID',
    USERID          VARCHAR(80) NOT NULL COMMENT '用户名',
    ORDERDATE       DATETIME NOT NULL COMMENT '下单时间',
    SHIPADDR1       VARCHAR(255) COMMENT '配送地址1',
    SHIPADDR2       VARCHAR(255) COMMENT '配送地址2',
    SHIPCITY        VARCHAR(80) COMMENT '配送城市',
    SHIPSTATE       VARCHAR(80) COMMENT '配送州/省',
    SHIPZIP         VARCHAR(20) COMMENT '配送邮编',
    SHIPCOUNTRY     VARCHAR(80) COMMENT '配送国家',
    BILLADDR1       VARCHAR(255) COMMENT '账单地址1',
    BILLADDR2       VARCHAR(255) COMMENT '账单地址2',
    BILLCITY        VARCHAR(80) COMMENT '账单城市',
    BILLSTATE       VARCHAR(80) COMMENT '账单州/省',
    BILLZIP         VARCHAR(20) COMMENT '账单邮编',
    BILLCOUNTRY     VARCHAR(80) COMMENT '账单国家',
    TOTALPRICE      DECIMAL(10,2) COMMENT '订单总价',
    COURIER         VARCHAR(80) COMMENT '快递公司',
    BILLTOFIRSTNAME VARCHAR(80) COMMENT '账单收件人名',
    BILLTOLASTNAME  VARCHAR(80) COMMENT '账单收件人姓',
    SHIPTOFIRSTNAME VARCHAR(80) COMMENT '配送收件人名',
    SHIPTOLASTNAME  VARCHAR(80) COMMENT '配送收件人姓',
    CREDITCARD      VARCHAR(80) COMMENT '信用卡号',
    EXPRDATE        VARCHAR(20) COMMENT '信用卡有效期',
    CARDTYPE        VARCHAR(20) COMMENT '信用卡类型',
    LOCALE          VARCHAR(20) COMMENT '语言环境',
    STATUS          VARCHAR(2) DEFAULT 'P' COMMENT '订单状态(P/C/S/D/X)',
    FOREIGN KEY (USERID) REFERENCES ACCOUNT(USERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 11. 订单状态表 (ORDERSTATUS)
--    记录订单状态变更历史
-- ============================================================
CREATE TABLE IF NOT EXISTS ORDERSTATUS (
    ORDERID   VARCHAR(80) NOT NULL COMMENT '订单ID',
    LINENUM   INT NOT NULL COMMENT '状态行号',
    TIMESTAMP DATETIME NOT NULL COMMENT '状态变更时间',
    STATUS    VARCHAR(2) NOT NULL COMMENT '订单状态(P/C/S/D/X)',
    PRIMARY KEY (ORDERID, LINENUM),
    FOREIGN KEY (ORDERID) REFERENCES ORDERS(ORDERID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 12. 订单项表 (LINEITEM)
--    存储订单中的商品明细
-- ============================================================
CREATE TABLE IF NOT EXISTS LINEITEM (
    ORDERID   VARCHAR(80) NOT NULL COMMENT '订单ID',
    LINENUM   INT NOT NULL COMMENT '行号',
    ITEMID    VARCHAR(80) NOT NULL COMMENT '商品ID',
    QUANTITY  INT NOT NULL COMMENT '数量',
    UNITPRICE DECIMAL(10,2) NOT NULL COMMENT '单价',
    PRIMARY KEY (ORDERID, LINENUM),
    FOREIGN KEY (ORDERID) REFERENCES ORDERS(ORDERID),
    FOREIGN KEY (ITEMID) REFERENCES ITEM(ITEMID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- 13. 操作日志表 (LOG)
--    记录用户操作日志
-- ============================================================
CREATE TABLE IF NOT EXISTS LOG (
    LOGID            VARCHAR(80) PRIMARY KEY COMMENT '日志ID(UUID)',
    USERNAME         VARCHAR(80) COMMENT '操作用户',
    OPERATIONTYPE    VARCHAR(80) COMMENT '操作类型(LOGIN/LOGOUT/ORDER/…)',
    OPERATIONCONTENT VARCHAR(255) COMMENT '操作内容',
    OPERATIONTIME    DATETIME COMMENT '操作时间',
    IPADDRESS        VARCHAR(80) COMMENT 'IP地址',
    USERAGENT        VARCHAR(255) COMMENT '浏览器信息'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- ==================== 插入测试数据 ============================
-- ============================================================

-- ------------------------------------------------------------
-- 测试数据 - 分类
-- ------------------------------------------------------------
INSERT INTO CATEGORY (CATEGORYID, NAME, `DESC`) VALUES
('FISH',  '鱼类', '各种观赏鱼和水生宠物'),
('DOGS',  '狗狗', '各类犬种宠物'),
('CATS',  '猫咪', '各类猫种宠物'),
('BIRDS', '鸟类', '各类观赏鸟'),
('REPTILES', '爬行类', '各类爬行宠物')
ON DUPLICATE KEY UPDATE NAME=VALUES(NAME), `DESC`=VALUES(`DESC`);

-- ------------------------------------------------------------
-- 测试数据 - 产品
-- ------------------------------------------------------------
INSERT INTO PRODUCT (PRODUCTID, CATEGORY, NAME, `DESC`) VALUES
('FI-SW-01',    'FISH',  '天使鱼',   '美丽的热带淡水鱼，适合初学者饲养'),
('FI-SW-02',    'FISH',  '虎皮鱼',   '色彩斑斓的热带鱼，群游效果极佳'),
('FI-FW-01',    'FISH',  '锦鲤',     '吉祥的观赏鱼，适合庭院池塘'),
('FI-FW-02',    'FISH',  '金鱼',     '经典的观赏鱼，易于饲养'),
('K9-BD-01',    'DOGS',  '拉布拉多', '友善的家庭宠物犬，智商排名第七'),
('K9-CW-01',    'DOGS',  '金毛寻回犬', '聪明忠诚的工作犬，非常适合家庭'),
('K9-RT-01',    'DOGS',  '柯基犬',   '短腿可爱的牧牛犬，活泼好动'),
('K9-PO-01',    'DOGS',  '泰迪犬',   '小巧可爱的伴侣犬，不掉毛'),
('FL-DSH-01',   'CATS',  '波斯猫',   '长毛优雅的猫咪，性格温顺'),
('FL-DLH-01',   'CATS',  '暹罗猫',   '活泼好动的短毛猫，善于交流'),
('FL-MC-01',    'CATS',  '英短蓝猫', '圆脸憨厚的猫咪，性格独立'),
('AV-CB-01',    'BIRDS', '鹦鹉',     '会说话的彩色鹦鹉，智商极高'),
('AV-SB-01',    'BIRDS', '金丝雀',   '歌声优美的笼中鸟，色彩鲜艳'),
('RP-LI-01',    'REPTILES', '鬃狮蜥', '温顺的入门级爬宠，适合新手'),
('RP-SN-01',    'REPTILES', '玉米蛇', '颜色多变的宠物蛇，无毒温顺')
ON DUPLICATE KEY UPDATE NAME=VALUES(NAME), `DESC`=VALUES(`DESC`);

-- ------------------------------------------------------------
-- 测试数据 - 商品（带详细属性）
-- ------------------------------------------------------------
INSERT INTO ITEM (ITEMID, PRODUCTID, LISTPRICE, UNITCOST, SUPPLIER, STATUS, ATTR1, ATTR2, ATTR3, ATTR4, ATTR5) VALUES
-- 天使鱼变体
('EST-1',  'FI-SW-01', 16.50,  10.00,  '水族之家',   'P', '小型', '银色', '3-5cm',  '热带淡水', '群游'),
('EST-2',  'FI-SW-01', 22.00,  14.00,  '水族之家',   'P', '中型', '金色', '5-8cm',  '热带淡水', '独居'),
-- 虎皮鱼
('EST-3',  'FI-SW-02', 18.50,  12.00,  '海洋世界',   'P', '小型', '虎纹', '3-4cm',  '热带淡水', '群游'),
('EST-4',  'FI-SW-02', 25.00,  16.00,  '海洋世界',   'P', '中型', '红虎纹', '4-6cm','热带淡水', '群游'),
-- 锦鲤
('EST-5',  'FI-FW-01', 88.00,  60.00,  '锦鲤世家',   'P', '小型', '红白', '15-20cm','冷水',   '池塘'),
('EST-6',  'FI-FW-01', 188.00, 130.00, '锦鲤世家',   'P', '中型', '三色', '25-35cm','冷水',   '池塘'),
-- 金鱼
('EST-7',  'FI-FW-02', 15.00,  8.00,   '水族之家',   'P', '小型', '红色', '5-8cm',  '冷水',   '缸养'),
-- 拉布拉多
('EST-8',  'K9-BD-01', 1900.00, 1500.00, '名犬基地', 'P', '幼犬', '黄色', '公',   '3个月',  '已驱虫'),
('EST-9',  'K9-BD-01', 2200.00, 1700.00, '名犬基地', 'P', '幼犬', '黑色', '母',   '4个月',  '已疫苗'),
-- 金毛
('EST-10', 'K9-CW-01', 2200.00, 1800.00, '名犬基地', 'P', '幼犬', '金色', '公',   '3个月',  '已驱虫'),
('EST-11', 'K9-CW-01', 2500.00, 2000.00, '名犬基地', 'P', '幼犬', '金色', '母',   '4个月',  '已疫苗'),
-- 柯基
('EST-12', 'K9-RT-01', 3500.00, 2800.00, '萌宠乐园', 'P', '幼犬', '黄白', '公',   '2个月',  '已驱虫'),
-- 泰迪
('EST-13', 'K9-PO-01', 1800.00, 1200.00, '萌宠乐园', 'P', '幼犬', '棕色', '母',   '3个月',  '玩具型'),
('EST-14', 'K9-PO-01', 2000.00, 1400.00, '萌宠乐园', 'P', '幼犬', '灰色', '公',   '3个月',  '迷你型'),
-- 波斯猫
('EST-15', 'FL-DSH-01', 1200.00, 900.00,  '猫咪之家', 'P', '幼猫', '白色', '母',   '3个月',  '长毛'),
('EST-16', 'FL-DSH-01', 1500.00, 1100.00, '猫咪之家', 'P', '幼猫', '蓝色', '公',   '4个月',  '长毛'),
-- 暹罗猫
('EST-17', 'FL-DLH-01', 1500.00, 1200.00, '猫咪之家', 'P', '幼猫', '海豹色', '公', '3个月',  '短毛'),
-- 英短蓝猫
('EST-18', 'FL-MC-01',  2800.00, 2200.00, '猫咪之家', 'P', '幼猫', '蓝色', '母',   '4个月',  '短毛'),
('EST-19', 'FL-MC-01',  3200.00, 2500.00, '猫咪之家', 'P', '成猫', '蓝色', '公',   '1岁',    '绝育'),
-- 鹦鹉
('EST-20', 'AV-CB-01',  500.00,  350.00,  '飞鸟天地', 'P', '中型', '绿色', '公',   '6个月',  '会说话'),
('EST-21', 'AV-CB-01',  600.00,  420.00,  '飞鸟天地', 'P', '中型', '蓝色', '母',   '8个月',  '会说话'),
-- 金丝雀
('EST-22', 'AV-SB-01',  200.00,  150.00,  '飞鸟天地', 'P', '小型', '黄色', '公',   '4个月',  '鸣叫型'),
('EST-23', 'AV-SB-01',  180.00,  130.00,  '飞鸟天地', 'P', '小型', '白色', '母',   '3个月',  '观赏型'),
-- 鬃狮蜥
('EST-24', 'RP-LI-01',  350.00,  250.00,  '爬虫世界', 'P', '幼体', '普通色', '公', '15cm',   '适合新手'),
-- 玉米蛇
('EST-25', 'RP-SN-01',  400.00,  280.00,  '爬虫世界', 'P', '幼体', '红色',   '母', '30cm',   '无毒温顺'),
('EST-26', 'RP-SN-01',  450.00,  320.00,  '爬虫世界', 'P', '幼体', '白化',   '公', '35cm',   '无毒温顺')
ON DUPLICATE KEY UPDATE LISTPRICE=VALUES(LISTPRICE), UNITCOST=VALUES(UNITCOST), STATUS=VALUES(STATUS);

-- ------------------------------------------------------------
-- 测试数据 - 库存
-- ------------------------------------------------------------
INSERT INTO INVENTORY (ITEMID, QTY) VALUES
('EST-1',  100), ('EST-2',  80),  ('EST-3',  150), ('EST-4',  60),
('EST-5',  30),  ('EST-6',  20),  ('EST-7',  200), ('EST-8',  8),
('EST-9',  10),  ('EST-10', 6),   ('EST-11', 5),   ('EST-12', 4),
('EST-13', 12),  ('EST-14', 10),  ('EST-15', 6),   ('EST-16', 8),
('EST-17', 5),   ('EST-18', 3),   ('EST-19', 2),   ('EST-20', 15),
('EST-21', 10),  ('EST-22', 30),  ('EST-23', 25),  ('EST-24', 8),
('EST-25', 5),   ('EST-26', 3)
ON DUPLICATE KEY UPDATE QTY=VALUES(QTY);

-- ------------------------------------------------------------
-- 测试数据 - 用户账户
-- ------------------------------------------------------------
INSERT INTO ACCOUNT (USERID, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, STATUS,
    ADDR1, ADDR2, CITY, STATE, ZIP, COUNTRY, PHONE,
    FAVCATEGORY, LANGPREF, MYLISTOPT, BANNEROPT) VALUES
('admin',    'admin123',  'admin@petstore.com',   '系统',   '管理员', 'P',
    '长沙市岳麓区中南大学', '计算机楼301', '长沙', '湖南', '410083', '中国', '13800000000',
    'DOGS', 'CN', 1, 1),
('zhangsan', '123456',    'zhangsan@qq.com',      '三',     '张',     'P',
    '北京市朝阳区望京SOHO', 'T1栋1001', '北京', '北京', '100102', '中国', '13900001111',
    'CATS', 'CN', 1, 1),
('lisi',     '123456',    'lisi@163.com',         '四',     '李',     'P',
    '上海市浦东新区张江高科技园区', 'A座201', '上海', '上海', '201203', '中国', '13900002222',
    'FISH', 'CN', 1, 1),
('wangwu',   '123456',    'wangwu@gmail.com',     '五',     '王',     'P',
    '广州市天河区体育西路', '天河城501', '广州', '广东', '510620', '中国', '13900003333',
    'BIRDS', 'CN', 1, 0),
('zhaoliu',  '123456',    'zhaoliu@outlook.com',  '六',     '赵',     'L',
    '深圳市南山区科技园', '腾讯大厦1001', '深圳', '广东', '518057', '中国', '13900004444',
    'DOGS', 'CN', 1, 1)
ON DUPLICATE KEY UPDATE PASSWORD=VALUES(PASSWORD), EMAIL=VALUES(EMAIL);

-- ------------------------------------------------------------
-- 测试数据 - 横幅数据
-- ------------------------------------------------------------
INSERT INTO BANNERDATA (FAVCATEGORY, BANNERNAME) VALUES
('FISH',     'banner_fish.jpg'),
('DOGS',     'banner_dogs.jpg'),
('CATS',     'banner_cats.jpg'),
('BIRDS',    'banner_birds.jpg'),
('REPTILES', 'banner_reptiles.jpg')
ON DUPLICATE KEY UPDATE BANNERNAME=VALUES(BANNERNAME);

-- ------------------------------------------------------------
-- 测试数据 - 购物车
-- ------------------------------------------------------------
INSERT INTO CART (CARTID) VALUES
('zhangsan'),
('lisi'),
('wangwu')
ON DUPLICATE KEY UPDATE CARTID=VALUES(CARTID);

-- ------------------------------------------------------------
-- 测试数据 - 购物车项
-- ------------------------------------------------------------
INSERT INTO CARTITEM (CARTID, ITEMID, QUANTITY) VALUES
('zhangsan', 'EST-1',  2),
('zhangsan', 'EST-10', 1),
('lisi',     'EST-20', 1),
('lisi',     'EST-22', 2),
('wangwu',   'EST-5',  3)
ON DUPLICATE KEY UPDATE QUANTITY=VALUES(QUANTITY);

-- ------------------------------------------------------------
-- 测试数据 - 订单
-- ------------------------------------------------------------
INSERT INTO ORDERS (ORDERID, USERID, ORDERDATE,
    SHIPADDR1, SHIPADDR2, SHIPCITY, SHIPSTATE, SHIPZIP, SHIPCOUNTRY,
    BILLADDR1, BILLADDR2, BILLCITY, BILLSTATE, BILLZIP, BILLCOUNTRY,
    TOTALPRICE, COURIER,
    BILLTOFIRSTNAME, BILLTOLASTNAME, SHIPTOFIRSTNAME, SHIPTOLASTNAME,
    CREDITCARD, EXPRDATE, CARDTYPE, LOCALE, STATUS) VALUES
-- 张三的已完成订单
('ORD-2026-001', 'zhangsan', '2026-05-01 10:30:00',
    '北京市朝阳区望京SOHO', 'T1栋1001', '北京', '北京', '100102', '中国',
    '北京市朝阳区望京SOHO', 'T1栋1001', '北京', '北京', '100102', '中国',
    2216.50, 'SF',
    '三', '张', '三', '张',
    '9999 9999 9999 9999', '12/28', 'Visa', 'zh_CN', 'D'),
-- 张三的待处理订单
('ORD-2026-002', 'zhangsan', '2026-06-10 14:20:00',
    '北京市朝阳区望京SOHO', 'T1栋1001', '北京', '北京', '100102', '中国',
    '北京市朝阳区望京SOHO', 'T1栋1001', '北京', '北京', '100102', '中国',
    33.00, 'SF',
    '三', '张', '三', '张',
    '9999 9999 9999 9999', '12/28', 'Visa', 'zh_CN', 'P'),
-- 李四的已发货订单
('ORD-2026-003', 'lisi', '2026-05-15 09:15:00',
    '上海市浦东新区张江高科技园区', 'A座201', '上海', '上海', '201203', '中国',
    '上海市浦东新区张江高科技园区', 'A座201', '上海', '上海', '201203', '中国',
    900.00, 'ZTO',
    '四', '李', '四', '李',
    '8888 8888 8888 8888', '06/27', 'MasterCard', 'zh_CN', 'S'),
-- 李四的已取消订单
('ORD-2026-004', 'lisi', '2026-06-01 16:45:00',
    '上海市浦东新区张江高科技园区', 'A座201', '上海', '上海', '201203', '中国',
    '上海市浦东新区张江高科技园区', 'A座201', '上海', '上海', '201203', '中国',
    200.00, 'YTO',
    '四', '李', '四', '李',
    '8888 8888 8888 8888', '06/27', 'MasterCard', 'zh_CN', 'X'),
-- 王五的已确认订单
('ORD-2026-005', 'wangwu', '2026-06-12 11:00:00',
    '广州市天河区体育西路', '天河城501', '广州', '广东', '510620', '中国',
    '广州市天河区体育西路', '天河城501', '广州', '广东', '510620', '中国',
    176.00, 'STO',
    '五', '王', '五', '王',
    '7777 7777 7777 7777', '03/26', 'UnionPay', 'zh_CN', 'C')
ON DUPLICATE KEY UPDATE TOTALPRICE=VALUES(TOTALPRICE), STATUS=VALUES(STATUS);

-- ------------------------------------------------------------
-- 测试数据 - 订单状态历史
-- ------------------------------------------------------------
INSERT INTO ORDERSTATUS (ORDERID, LINENUM, TIMESTAMP, STATUS) VALUES
-- ORD-2026-001: 张三的已完成订单状态流
('ORD-2026-001', 1, '2026-05-01 10:30:00', 'P'),
('ORD-2026-001', 2, '2026-05-01 15:00:00', 'C'),
('ORD-2026-001', 3, '2026-05-02 08:00:00', 'S'),
('ORD-2026-001', 4, '2026-05-05 18:00:00', 'D'),
-- ORD-2026-002: 张三的待处理订单
('ORD-2026-002', 1, '2026-06-10 14:20:00', 'P'),
-- ORD-2026-003: 李四的已发货订单状态流
('ORD-2026-003', 1, '2026-05-15 09:15:00', 'P'),
('ORD-2026-003', 2, '2026-05-15 16:30:00', 'C'),
('ORD-2026-003', 3, '2026-05-16 10:00:00', 'S'),
-- ORD-2026-004: 李四的已取消订单状态流
('ORD-2026-004', 1, '2026-06-01 16:45:00', 'P'),
('ORD-2026-004', 2, '2026-06-01 20:00:00', 'X'),
-- ORD-2026-005: 王五的已确认订单状态流
('ORD-2026-005', 1, '2026-06-12 11:00:00', 'P'),
('ORD-2026-005', 2, '2026-06-12 14:30:00', 'C')
ON DUPLICATE KEY UPDATE TIMESTAMP=VALUES(TIMESTAMP), STATUS=VALUES(STATUS);

-- ------------------------------------------------------------
-- 测试数据 - 订单项
-- ------------------------------------------------------------
INSERT INTO LINEITEM (ORDERID, LINENUM, ITEMID, QUANTITY, UNITPRICE) VALUES
-- ORD-2026-001 订单项（2个天使鱼 + 1个金毛幼犬）
('ORD-2026-001', 1, 'EST-1',  2, 16.50),
('ORD-2026-001', 2, 'EST-10', 1, 2200.00),
-- ORD-2026-002 订单项（2个天使鱼）
('ORD-2026-002', 1, 'EST-1',  2, 16.50),
-- ORD-2026-003 订单项（1个鹦鹉 + 2个金丝雀）
('ORD-2026-003', 1, 'EST-20', 1, 500.00),
('ORD-2026-003', 2, 'EST-22', 2, 200.00),
-- ORD-2026-004 订单项（1个金丝雀）
('ORD-2026-004', 1, 'EST-22', 1, 200.00),
-- ORD-2026-005 订单项（2个锦鲤）
('ORD-2026-005', 1, 'EST-5',  2, 88.00)
ON DUPLICATE KEY UPDATE QUANTITY=VALUES(QUANTITY), UNITPRICE=VALUES(UNITPRICE);

-- ------------------------------------------------------------
-- 测试数据 - 操作日志
-- ------------------------------------------------------------
INSERT INTO LOG (LOGID, USERNAME, OPERATIONTYPE, OPERATIONCONTENT, OPERATIONTIME, IPADDRESS, USERAGENT) VALUES
('LOG001', 'zhangsan', 'LOGIN',    '用户登录系统',                  '2026-06-10 14:00:00', '192.168.1.100', 'Mozilla/5.0 Chrome/120.0'),
('LOG002', 'zhangsan', 'ADD_CART', '添加商品 EST-1 到购物车',       '2026-06-10 14:15:00', '192.168.1.100', 'Mozilla/5.0 Chrome/120.0'),
('LOG003', 'zhangsan', 'ORDER',    '创建订单 ORD-2026-002',         '2026-06-10 14:20:00', '192.168.1.100', 'Mozilla/5.0 Chrome/120.0'),
('LOG004', 'lisi',     'LOGIN',    '用户登录系统',                  '2026-06-12 09:00:00', '10.0.0.55',      'Mozilla/5.0 Safari/17.0'),
('LOG005', 'lisi',     'ADD_CART', '添加商品 EST-20 到购物车',      '2026-06-12 09:10:00', '10.0.0.55',      'Mozilla/5.0 Safari/17.0'),
('LOG006', 'wangwu',   'LOGIN',    '用户登录系统',                  '2026-06-12 10:30:00', '172.16.0.200',   'Mozilla/5.0 Edge/120.0'),
('LOG007', 'wangwu',   'ORDER',    '创建订单 ORD-2026-005',         '2026-06-12 11:00:00', '172.16.0.200',   'Mozilla/5.0 Edge/120.0'),
('LOG008', 'wangwu',   'LOGOUT',   '用户退出系统',                  '2026-06-12 11:05:00', '172.16.0.200',   'Mozilla/5.0 Edge/120.0'),
('LOG009', 'admin',    'LOGIN',    '管理员登录系统',                '2026-06-14 08:00:00', '10.0.0.1',       'Mozilla/5.0 Chrome/120.0'),
('LOG010', 'admin',    'VIEW_ORD', '查看所有订单列表',              '2026-06-14 08:30:00', '10.0.0.1',       'Mozilla/5.0 Chrome/120.0')
ON DUPLICATE KEY UPDATE OPERATIONCONTENT=VALUES(OPERATIONCONTENT);

-- ============================================================
-- ==================== 数据验证查询 ============================
-- ============================================================

SELECT '========================================' AS '';
SELECT '   MyPetStore 数据库初始化完成！' AS '';
SELECT '========================================' AS '';

-- 验证：分类表
SELECT '--- 1. 分类表 (CATEGORY) ---' AS '';
SELECT * FROM CATEGORY;

-- 验证：产品表
SELECT '--- 2. 产品表 (PRODUCT) ---' AS '';
SELECT * FROM PRODUCT;

-- 验证：商品表
SELECT '--- 3. 商品表 (ITEM) ---' AS '';
SELECT ITEMID, PRODUCTID, LISTPRICE, UNITCOST, SUPPLIER, STATUS, ATTR1, ATTR2 FROM ITEM;

-- 验证：库存表
SELECT '--- 4. 库存表 (INVENTORY) ---' AS '';
SELECT * FROM INVENTORY;

-- 验证：用户账户表
SELECT '--- 5. 用户账户表 (ACCOUNT) ---' AS '';
SELECT USERID, EMAIL, FIRSTNAME, LASTNAME, STATUS, CITY, FAVCATEGORY, PHONE FROM ACCOUNT;

-- 验证：横幅数据表
SELECT '--- 6. 横幅数据表 (BANNERDATA) ---' AS '';
SELECT * FROM BANNERDATA;

-- 验证：购物车表
SELECT '--- 7. 购物车表 (CART) ---' AS '';
SELECT * FROM CART;

-- 验证：购物车项表
SELECT '--- 8. 购物车项表 (CARTITEM) ---' AS '';
SELECT * FROM CARTITEM;

-- 验证：订单表
SELECT '--- 9. 订单表 (ORDERS) ---' AS '';
SELECT ORDERID, USERID, ORDERDATE, TOTALPRICE, STATUS, SHIPCITY FROM ORDERS;

-- 验证：订单状态表
SELECT '--- 10. 订单状态表 (ORDERSTATUS) ---' AS '';
SELECT * FROM ORDERSTATUS;

-- 验证：订单项表
SELECT '--- 11. 订单项表 (LINEITEM) ---' AS '';
SELECT * FROM LINEITEM;

-- 验证：操作日志表
SELECT '--- 12. 操作日志表 (LOG) ---' AS '';
SELECT LOGID, USERNAME, OPERATIONTYPE, OPERATIONTIME, IPADDRESS FROM LOG;

-- ============================================================
-- 跨表关联验证查询
-- ============================================================

SELECT '========================================' AS '';
SELECT '   跨表关联验证' AS '';
SELECT '========================================' AS '';

-- 每个分类下的产品数量
SELECT '--- 各分类产品数量 ---' AS '';
SELECT c.CATEGORYID, c.NAME AS 分类名称, COUNT(p.PRODUCTID) AS 产品数量
FROM CATEGORY c
LEFT JOIN PRODUCT p ON c.CATEGORYID = p.CATEGORY
GROUP BY c.CATEGORYID, c.NAME;

-- 库存不足10件的商品（需要补货）
SELECT '--- 库存不足预警（QTY < 10） ---' AS '';
SELECT i.ITEMID, i.PRODUCTID, inv.QTY, p.NAME AS 产品名称
FROM INVENTORY inv
JOIN ITEM i ON inv.ITEMID = i.ITEMID
JOIN PRODUCT p ON i.PRODUCTID = p.PRODUCTID
WHERE inv.QTY < 10
ORDER BY inv.QTY ASC;

-- 张三的订单历史（含订单项明细）
SELECT '--- 张三订单详情 ---' AS '';
SELECT o.ORDERID, o.ORDERDATE, o.TOTALPRICE, o.STATUS,
       li.LINENUM, li.ITEMID, li.QUANTITY, li.UNITPRICE,
       p.NAME AS 产品名称
FROM ORDERS o
JOIN LINEITEM li ON o.ORDERID = li.ORDERID
JOIN ITEM i ON li.ITEMID = i.ITEMID
JOIN PRODUCT p ON i.PRODUCTID = p.PRODUCTID
WHERE o.USERID = 'zhangsan'
ORDER BY o.ORDERDATE DESC, li.LINENUM;

-- 各用户消费统计
SELECT '--- 用户消费统计 ---' AS '';
SELECT a.USERID, a.FIRSTNAME AS 名, a.LASTNAME AS 姓,
       COUNT(o.ORDERID) AS 订单数,
       COALESCE(SUM(CASE WHEN o.STATUS != 'X' THEN o.TOTALPRICE ELSE 0 END), 0) AS 有效消费总额
FROM ACCOUNT a
LEFT JOIN ORDERS o ON a.USERID = o.USERID
GROUP BY a.USERID, a.FIRSTNAME, a.LASTNAME
ORDER BY 有效消费总额 DESC;

-- 当前购物车内容汇总
SELECT '--- 当前购物车内容 ---' AS '';
SELECT ci.CARTID, ci.ITEMID, ci.QUANTITY, i.LISTPRICE,
       (ci.QUANTITY * i.LISTPRICE) AS 小计,
       p.NAME AS 产品名称
FROM CARTITEM ci
JOIN ITEM i ON ci.ITEMID = i.ITEMID
JOIN PRODUCT p ON i.PRODUCTID = p.PRODUCTID
ORDER BY ci.CARTID;
