# Spring Boot 基础项目-V0

这是一个基于 Spring Boot 的基础项目，用于演示 Spring Boot 的基本功能和使用方法。

## 技术栈

- Spring Boot 2.2.5
- Spring Data JPA
- MySQL 数据库
- Thymeleaf 模板引擎
- Maven 构建工具

## 项目功能

- 用户注册功能
- 管理员后台管理
- 用户信息管理

## 环境要求

- JDK 1.8 或以上版本
- MySQL 8.0 或以上版本
- Maven 3.6 或以上版本

## 数据库配置

项目使用 MySQL 数据库，默认配置如下：
- 数据库名：springboot
- 用户名：root
- 密码：123456
- 端口：3306

如果需要修改数据库配置，请编辑 `src/main/resources/application.properties` 文件。

## 项目部署

1. 克隆项目到本地
```bash
git clone [项目地址]
```

2. 创建数据库
```sql
CREATE DATABASE springboot CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. 修改数据库配置（如果需要）
编辑 `src/main/resources/application.properties` 文件，修改数据库连接信息。

4. 编译项目
```bash
mvn clean package
```

5. 运行项目
```bash
mvn spring-boot:run
```

项目启动后，可以通过以下地址访问：
- 首页：http://localhost:8080
- 注册页面：http://localhost:8080/register
- 管理员页面：http://localhost:8080/admin

## 使用说明

### 用户注册
1. 访问 http://localhost:8080/register
2. 填写用户信息
3. 点击注册按钮提交

### 管理员功能
1. 访问 http://localhost:8080/admin
2. 可以查看所有注册用户
3. 可以添加新用户
4. 可以管理现有用户

## 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── com/example/demo/
│   │       ├── controller/    # 控制器层
│   │       ├── model/         # 数据模型
│   │       ├── repository/    # 数据访问层
│   │       ├── service/       # 业务逻辑层
│   │       └── DemoApplication.java
│   └── resources/
│       ├── static/           # 静态资源
│       ├── templates/        # 模板文件
│       └── application.properties
```

## 注意事项

1. 首次运行时会自动创建数据库表结构
2. 开发环境下模板缓存已关闭，修改模板文件后无需重启
3. 生产环境部署时，请修改数据库密码等敏感信息
4. 建议在生产环境中开启模板缓存

## 常见问题

1. 如果遇到数据库连接问题，请检查：
   - MySQL 服务是否启动
   - 数据库配置是否正确
   - 数据库用户权限是否足够

2. 如果遇到编译问题，请检查：
   - JDK 版本是否符合要求
   - Maven 配置是否正确
   - 网络连接是否正常（用于下载依赖）

## 目前存在的问题

1. 没有网页缓存功能
2. 账号密码没有安全机制
3. 没有任务管理
4. 如何打包部署