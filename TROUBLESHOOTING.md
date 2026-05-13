# MyPetStore 后端500错误排查指南

## 问题描述
访问 http://localhost:3000/api/catalog/products 时返回 500 Internal Server Error

## 可能的原因

### 1. MySQL数据库服务未运行
**检查方法：**
- Windows: 打开"服务"（Win+R 输入 services.msc），查找 MySQL 服务是否正在运行
- 命令行: `net start | findstr MySQL`

**解决方法：**
- 启动MySQL服务：`net start MySQL80` （服务名可能是 MySQL 或 MySQL57）

### 2. 数据库密码不一致（已修复）
**问题：** 
- application.yml 中配置的密码是 `123456`
- DBUtil.java 中配置的密码是 `888` （已修改为 `123456`）

**解决方法：**
- 确保两个配置文件中的密码一致
- 如果你的MySQL密码不是 `123456`，请修改以下两个文件：
  1. `src/main/resources/application.yml` (第13行)
  2. `src/main/java/csu/web/mypetstore/persistence/DBUtil.java` (第9行)

### 3. 数据库中没有表或数据
**检查方法：**
在MySQL命令行执行：
```sql
USE mypetstore;
SHOW TABLES;
SELECT COUNT(*) FROM PRODUCT;
```

**解决方法：**
执行初始化脚本：
```bash
mysql -u root -p < E:\AAA软件平台架构\实验\mypetstore\database_check.sql
```

### 4. 后端服务未正确启动
**检查方法：**
- 查看后端启动日志是否有错误
- 访问 http://localhost:8080/api/catalog/products 直接测试

**解决方法：**
1. 停止当前后端服务（Ctrl+C）
2. 重新编译：`mvn clean compile`
3. 重新启动：`mvn spring-boot:run`
4. 观察启动日志，确保没有报错

## 快速排查步骤

### 步骤1：验证数据库连接
```sql
-- 在MySQL命令行或Workbench中执行
mysql -u root -p
-- 输入密码后执行：
SHOW DATABASES;
USE mypetstore;
SHOW TABLES;
SELECT * FROM PRODUCT LIMIT 5;
```

如果看到表和数据，说明数据库正常。

### 步骤2：测试后端API
在浏览器中直接访问：
```
http://localhost:8080/api/catalog/products
```

**预期结果：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [...],
  "timestamp": 1234567890
}
```

如果返回500错误，查看后端控制台输出的错误堆栈信息。

### 步骤3：查看后端日志
后端启动后，应该看到类似这样的日志：
```
2026-05-13 xx:xx:xx [main] INFO  c.c.web.mypetstore.MyPetStoreApplication - Started MyPetStoreApplication
2026-05-13 xx:xx:xx [main] INFO  com.zaxxer.hikari.HikariDataSource - HikariPool-1 - Start completed.
```

如果有数据库连接错误，会显示：
```
java.sql.SQLException: Access denied for user 'root'@'localhost'
```
或
```
java.sql.SQLSyntaxErrorException: Table 'mypetstore.product' doesn't exist
```

### 步骤4：检查数据库配置

打开 `src/main/resources/application.yml`，确认以下配置正确：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mypetstore?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: 123456  # 确保这是你的MySQL密码
```

## 常见错误及解决方法

### 错误1：Access denied for user 'root'@'localhost'
**原因：** 数据库密码错误
**解决：** 修改 application.yml 和 DBUtil.java 中的密码

### 错误2：Table 'mypetstore.product' doesn't exist
**原因：** 数据库中没有创建表
**解决：** 执行 database_check.sql 脚本初始化数据库

### 错误3：Unknown database 'mypetstore'
**原因：** 数据库不存在
**解决：** 
```sql
CREATE DATABASE mypetstore DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;
```
然后执行 database_check.sql 脚本

### 错误4：Communications link failure
**原因：** MySQL服务未启动
**解决：** 启动MySQL服务

## 重启后端服务的正确步骤

1. **停止当前服务**：在运行后端的终端按 `Ctrl+C`

2. **清理编译**：
   ```bash
   cd E:\AAA软件平台架构\实验\mypetstore
   mvn clean compile
   ```

3. **重新启动**：
   ```bash
   mvn spring-boot:run
   ```

4. **等待启动完成**：看到 "Started MyPetStoreApplication" 表示启动成功

5. **测试API**：
   访问 http://localhost:8080/api/catalog/products

## 如果问题仍然存在

请提供以下信息以便进一步诊断：

1. **后端启动日志**（从启动到出现错误的完整日志）
2. **数据库连接测试结果**（执行上述步骤1的SQL命令的输出）
3. **直接访问后端API的结果**（http://localhost:8080/api/catalog/products 的返回内容）

## 已完成的修复

✅ 添加了 `/help` 路由和页面
✅ 创建了全局异常处理器 (GlobalExceptionHandler.java)
✅ 为 CatalogController 添加了 try-catch 错误处理
✅ 统一了数据库密码配置（DBUtil.java 和 application.yml）
✅ 创建了数据库初始化脚本 (database_check.sql)
