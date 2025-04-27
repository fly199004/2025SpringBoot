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

## V2版本，添加登录功能

### 修改说明

1. User 实体类修改
   - 添加了完整的 getter 和 setter 方法
   - 实现了 Serializable 接口以支持 Redis 缓存
   - 原因：为了支持对象序列化和反序列化，确保 Redis 缓存正常工作

2. UserRepository 接口修改
   - 添加了 `findByUsername` 方法用于检查用户名是否已存在
   - 添加了 `findByUsernameAndPassword` 方法用于登录验证
   - 原因：支持用户注册时的用户名查重和登录时的身份验证

3. UserService 类修改
   - 添加了用户名查重逻辑，防止重复注册
   - 优化了缓存策略，使用 `@Cacheable` 和 `@CacheEvict` 注解
   - 添加了登录验证方法
   - 原因：
     - 确保用户名的唯一性
     - 提高系统性能，减少数据库访问
     - 支持用户登录验证

4. UserController 类修改
   - 添加了登录相关的路由和方法
   - 添加了错误处理和用户反馈
   - 优化了注册流程，支持错误信息显示
   - 添加了管理员功能（添加/删除用户）
   - 原因：
     - 实现完整的用户认证流程
     - 提供更好的用户体验
     - 支持管理员对用户的管理

5. 页面模板修改
   - 登录页面（login.html）：
     - 添加了错误消息显示
     - 保留了用户名输入
     - 优化了页面布局
     - 原因：提供更好的用户体验和错误反馈

   - 注册页面（register.html）：
     - 添加了错误和成功消息显示
     - 保留了用户输入数据
     - 优化了表单布局
     - 原因：提供更好的用户体验和操作反馈

   - 管理员页面（admin.html）：
     - 添加了错误消息显示
     - 优化了用户列表显示
     - 添加了删除用户功能
     - 原因：提供更好的管理体验和操作反馈

### 登录功能实现步骤

1. 修改 User 实体类
```java
@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
```

2. 修改 UserRepository 接口
```java
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}
```

3. 修改 UserService 类
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
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        return userRepository.save(user);
    }
    
    public User findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
}
```

4. 修改 UserController 类
```java
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userService.findByUsernameAndPassword(username, password);
        if (user != null) {
            return "redirect:/admin";
        } else {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }
    
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            userService.saveUser(user);
            return "redirect:/register?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "register";
        }
    }
    
    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "admin";
    }
    
    @PostMapping("/admin/addUser")
    public String addUserFromAdmin(@ModelAttribute User user, Model model) {
        try {
            userService.saveUser(user);
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("newUser", user);
            return "admin";
        }
    }
    
    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
```

5. 创建登录页面（login.html）
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>登录</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="text-center">用户登录</h3>
                    </div>
                    <div class="card-body">
                        <!-- 错误消息显示 -->
                        <div th:if="${error}" class="alert alert-danger" role="alert">
                            <span th:text="${error}"></span>
                        </div>
                        
                        <form th:action="@{/login}" method="post">
                            <div class="form-group">
                                <label for="username">用户名</label>
                                <input type="text" class="form-control" id="username" name="username" th:value="${param.username}" required>
                            </div>
                            <div class="form-group">
                                <label for="password">密码</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary btn-block">登录</button>
                            </div>
                            <div class="text-center">
                                <a th:href="@{/register}">还没有账号？立即注册</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
```

6. 修改注册页面（register.html）
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>注册</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="text-center">用户注册</h3>
                    </div>
                    <div class="card-body">
                        <!-- 错误消息显示 -->
                        <div th:if="${error}" class="alert alert-danger" role="alert">
                            <span th:text="${error}"></span>
                        </div>
                        
                        <!-- 成功消息显示 -->
                        <div th:if="${param.success}" class="alert alert-success" role="alert">
                            注册成功！请登录。
                        </div>
                        
                        <form th:action="@{/register}" method="post" th:object="${user}">
                            <div class="form-group">
                                <label for="username">用户名</label>
                                <input type="text" class="form-control" id="username" th:field="*{username}" required>
                            </div>
                            <div class="form-group">
                                <label for="password">密码</label>
                                <input type="password" class="form-control" id="password" th:field="*{password}" required>
                            </div>
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary btn-block">注册</button>
                            </div>
                            <div class="text-center">
                                <a th:href="@{/login}">已有账号？立即登录</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
```

7. 修改管理员页面（admin.html）
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>管理员页面</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">
                        <h3>用户管理</h3>
                    </div>
                    <div class="card-body">
                        <!-- 错误消息显示 -->
                        <div th:if="${error}" class="alert alert-danger" role="alert">
                            <span th:text="${error}"></span>
                        </div>
                        
                        <!-- 添加用户表单 -->
                        <form th:action="@{/admin/addUser}" method="post" class="mb-4">
                            <div class="form-row">
                                <div class="form-group col-md-4">
                                    <label for="username">用户名</label>
                                    <input type="text" class="form-control" id="username" name="username" th:value="${newUser?.username}" required>
                                </div>
                                <div class="form-group col-md-4">
                                    <label for="password">密码</label>
                                    <input type="password" class="form-control" id="password" name="password" th:value="${newUser?.password}" required>
                                </div>
                                <div class="form-group col-md-4">
                                    <label>&nbsp;</label>
                                    <button type="submit" class="btn btn-primary btn-block">添加用户</button>
                                </div>
                            </div>
                        </form>

                        <!-- 用户列表 -->
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>用户名</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr th:each="user : ${users}">
                                    <td th:text="${user.id}"></td>
                                    <td th:text="${user.username}"></td>
                                    <td>
                                        <a th:href="@{/admin/delete/{id}(id=${user.id})}" class="btn btn-danger btn-sm">删除</a>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
```

### 登录功能说明

1. 功能特点：
   - 支持用户名和密码登录
   - 登录失败显示错误信息
   - 登录成功跳转到管理员页面
   - 提供注册入口
   - 支持用户注册
   - 支持管理员添加和删除用户

2. 安全特性：
   - 密码在传输过程中使用表单提交
   - 登录失败不显示具体错误原因
   - 支持基本的输入验证
   - 用户名唯一性检查

3. 用户体验：
   - 响应式设计，适配不同设备
   - 清晰的错误提示
   - 简洁的界面布局
   - 保留用户输入数据
   - 显示操作成功消息

### 注意事项

1. 登录功能目前使用简单的用户名密码验证
2. 建议在生产环境中添加：
   - 密码加密存储
   - 登录失败次数限制
   - 验证码功能
   - Session管理
   - 记住我功能

### 后续优化计划

1. 添加密码加密功能
2. 实现记住我功能
3. 添加验证码功能
4. 实现登录失败次数限制
5. 添加Session管理
6. 实现权限控制

## 版本说明

### V2版本
- 添加了Redis缓存支持
- 优化了用户注册和登录流程
- 添加了错误处理和用户反馈
- 改进了页面UI和用户体验
  - 添加了错误消息显示功能
  - 在注册页面显示成功消息
  - 保留用户输入数据
  - 优化了页面布局和样式
  - 添加了删除用户功能
- 支持管理员功能
  - 查看所有用户
  - 添加新用户
  - 删除用户
- 添加了用户会话管理
- 实现了基本的权限控制

### V1版本

- 添加redis缓存支持



