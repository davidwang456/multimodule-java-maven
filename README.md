# 多模块SpringBoot项目

这是一个基于Spring Boot的多模块Maven项目，采用分层架构设计。

## 项目结构

```
multi-springboot/
├── pom.xml                    # 父模块POM文件
├── common/                    # 公共模块
│   ├── pom.xml
│   └── src/main/java/com/example/common/
│       ├── Result.java        # 通用返回结果类
│       └── exception/
│           └── BusinessException.java  # 业务异常类
├── api/                       # API模块
│   ├── pom.xml
│   └── src/main/java/com/example/api/
│       ├── dto/
│       │   └── UserDTO.java   # 数据传输对象
│       └── vo/
│           └── UserVO.java     # 视图对象
├── service/                   # 服务模块
│   ├── pom.xml
│   └── src/main/java/com/example/service/
│       ├── entity/
│       │   └── User.java      # 实体类
│       ├── repository/
│       │   └── UserRepository.java  # 数据访问层
│       └── service/
│           ├── UserService.java      # 服务接口
│           └── impl/
│               └── UserServiceImpl.java  # 服务实现
└── web/                       # Web模块
    ├── pom.xml
    └── src/main/java/com/example/web/
        ├── Application.java    # 启动类
        ├── controller/
        │   └── UserController.java  # 控制器
        └── exception/
            └── GlobalExceptionHandler.java  # 全局异常处理器
```

## 模块说明

### common模块

- 包含通用工具类、常量、异常等
- 提供统一的返回结果类 `Result`
- 定义业务异常类 `BusinessException`

### api模块

- 包含数据传输对象（DTO）和视图对象（VO）
- 定义API接口的数据结构
- 依赖common模块

### service模块

- 包含业务逻辑和数据处理
- 定义实体类、Repository、Service接口和实现
- 依赖common和api模块

### web模块

- 包含控制器和启动类
- 提供RESTful API接口
- 依赖所有其他模块

## 技术栈

- **Spring Boot 3.2.0**: 主框架
- **MyBatis-Plus 3.5.4.1**: 数据访问层
- **MySQL 8.0.33**: 数据库
- **Lombok**: 简化代码
- **Maven**: 项目管理工具
- **JDK 17**: Java开发环境

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 2. 数据库配置

创建数据库：

```sql
CREATE DATABASE multi_springboot CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 `web/src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/multi_springboot?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 3. 编译运行

```bash
# 编译整个项目
mvn clean compile

# 运行项目
mvn spring-boot:run -pl web

# 或者进入web模块目录运行
cd web
mvn spring-boot:run
```

### 4. 访问API

项目启动后，可以通过以下URL访问API：

- 创建用户: `POST http://localhost:8080/api/users`
- 获取所有用户: `GET http://localhost:8080/api/users`
- 根据ID获取用户: `GET http://localhost:8080/api/users/{id}`
- 更新用户: `PUT http://localhost:8080/api/users/{id}`
- 删除用户: `DELETE http://localhost:8080/api/users/{id}`
- 搜索用户: `GET http://localhost:8080/api/users/search?username={username}`

## API示例

### 创建用户

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com",
    "nickname": "测试用户",
    "phone": "13800138000"
  }'
```

### 获取所有用户

```bash
curl -X GET http://localhost:8080/api/users
```

## 项目特点

1. **模块化设计**: 清晰的分层架构，便于维护和扩展
2. **统一异常处理**: 全局异常处理器，统一错误响应格式
3. **参数校验**: 使用JSR-303注解进行参数校验
4. **RESTful API**: 遵循RESTful设计原则
5. **数据库自动建表**: 使用MyBatis-Plus自动创建数据库表结构

## 开发建议

1. 新增功能时，按照模块职责进行代码组织
2. 使用DTO进行数据传输，VO进行数据展示
3. 在Service层处理业务逻辑，Controller层只负责参数校验和结果返回
4. 统一使用 `Result` 类包装返回结果
5. 使用 `BusinessException` 处理业务异常 