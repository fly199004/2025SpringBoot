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

## 版本控制

项目使用 Git 进行版本控制，目前已经同步到以下仓库：
- Gitee: https://gitee.com/xiaofei90/2025-spring-boot.git
- GitHub: https://github.com/fly199004/2025SpringBoot.git

### Git 操作说明

1. 初始化仓库
```bash
git init
```

2. 添加文件到暂存区
```bash
git add .
```

3. 提交更改
```bash
git commit -m "提交说明"
```

4. 添加远程仓库
```bash
# 添加 Gitee 仓库
git remote add gitee https://gitee.com/xiaofei90/2025-spring-boot.git

# 添加 GitHub 仓库
git remote add github https://github.com/fly199004/2025SpringBoot.git
```

5. 推送到远程仓库
```bash
# 推送到 Gitee
git push -u gitee main

# 推送到 GitHub
git push -u github main
```

## 目前存在的问题

1. 没有网页缓存功能
2. 账号密码没有安全机制
3. 没有任务管理
4. 如何打包部署

## V1版本，添加Redis缓存功能

### Redis缓存功能实现步骤

1. 添加Redis依赖到pom.xml
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

2. 配置Redis连接信息（application.properties）
```properties
# Redis配置
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=
spring.redis.database=0

# 缓存配置
spring.cache.type=redis
spring.cache.redis.time-to-live=600000
spring.cache.redis.cache-null-values=true
```

3. 在主应用类中添加缓存注解
```java
@SpringBootApplication
@EnableCaching
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

4. 在Service层添加缓存注解
```java
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Cacheable(value = "users", key = "'allUsers'")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @CacheEvict(value = "users", allEntries = true)
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
```

5. 在Controller层添加缓存注解
```java
@Controller
public class UserController {
    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }
    
    @PostMapping("/register")
    @CacheEvict(value = "users", allEntries = true)
    public String registerUser(User user) {
        userService.saveUser(user);
        return "redirect:/register?success";
    }
}
```

6. 修改实体类实现序列化接口
```java
@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    // ... getters and setters
}
```

### 常见问题及解决方案

1. 序列化问题
   - 错误信息：`java.lang.IllegalArgumentException: DefaultSerializer requires a Serializable payload but received an object of type`
   - 解决方案：让实体类实现 `Serializable` 接口
   - 原因：Redis 在存储对象时需要将对象序列化，因此实体类必须实现 `Serializable` 接口

2. 缓存键问题
   - 错误信息：`Null key returned for cache operation`
   - 解决方案：在 `@Cacheable` 注解中指定缓存键
   - 示例：`@Cacheable(value = "users", key = "'allUsers'")`

### Redis缓存功能说明

1. 缓存策略：
   - 使用 `@Cacheable` 注解缓存查询结果
   - 使用 `@CacheEvict` 注解在数据更新时清除缓存
   - 缓存过期时间设置为10分钟（600000毫秒）

2. 缓存效果：
   - 首次访问管理员页面时会从数据库加载数据并缓存
   - 后续访问会直接从Redis缓存中获取数据
   - 当添加新用户或注册新用户时，缓存会被清除
   - 下次访问时会重新从数据库加载最新数据

3. 性能优化：
   - 减少数据库查询次数
   - 提高页面响应速度
   - 降低服务器负载

### 注意事项

1. 确保Redis服务器已启动并可以访问
2. 生产环境中建议设置Redis密码
3. 根据实际需求调整缓存过期时间
4. 监控Redis内存使用情况，避免内存溢出
5. 所有需要缓存的实体类必须实现 `Serializable` 接口
6. 缓存键的命名要清晰明确，避免冲突

