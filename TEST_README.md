# 测试说明文档

## 概述

本项目为多模块Spring Boot项目，包含完整的测试用例，覆盖单元测试、集成测试和端到端测试。

## 测试结构

### 1. Common模块测试
- **位置**: `common/src/test/java/com/example/common/`
- **测试内容**:
  - `ResultTest.java` - Result类的单元测试
  - `BusinessExceptionTest.java` - 业务异常类的单元测试

### 2. API模块测试
- **位置**: `api/src/test/java/com/example/api/dto/`
- **测试内容**:
  - `UserDTOTest.java` - UserDTO类的验证测试

### 3. Service模块测试
- **位置**: `service/src/test/java/com/example/service/`
- **测试内容**:
  - `UserServiceTest.java` - UserService接口的单元测试
  - `UserTest.java` - User实体类的单元测试

### 4. Web模块测试
- **位置**: `web/src/test/java/com/example/web/`
- **测试内容**:
  - `UserControllerTest.java` - UserController的单元测试
  - `UserControllerIntegrationTest.java` - UserController的集成测试
  - `BaseControllerTest.java` - 控制器测试基类
  - `util/TestUtil.java` - 测试工具类

## 测试配置

### 测试环境配置
- **文件**: `web/src/test/resources/application-test.yml`
- **数据库**: H2内存数据库
- **端口**: 随机端口（避免冲突）

### 测试依赖
所有模块都添加了以下测试依赖：
- `spring-boot-starter-test`
- `junit-jupiter`
- `mockito-core`
- `mockito-junit-jupiter`

## 运行测试

### 运行所有测试
```bash
mvn test
```

### 运行特定模块测试
```bash
# 运行common模块测试
mvn test -pl common

# 运行api模块测试
mvn test -pl api

# 运行service模块测试
mvn test -pl service

# 运行web模块测试
mvn test -pl web
```

### 运行特定测试类
```bash
mvn test -Dtest=UserControllerTest
```

### 运行特定测试方法
```bash
mvn test -Dtest=UserControllerTest#testCreateUserSuccess
```

## 测试类型

### 1. 单元测试 (Unit Tests)
- **目的**: 测试单个类或方法的功能
- **特点**: 快速、独立、可重复
- **示例**: `ResultTest`, `UserDTOTest`, `UserTest`

### 2. 服务层测试 (Service Tests)
- **目的**: 测试业务逻辑
- **特点**: 使用Mock对象模拟依赖
- **示例**: `UserServiceTest`

### 3. 控制器测试 (Controller Tests)
- **目的**: 测试Web层接口
- **特点**: 使用MockMvc测试HTTP请求
- **示例**: `UserControllerTest`

### 4. 集成测试 (Integration Tests)
- **目的**: 测试组件间的交互
- **特点**: 使用真实的Spring上下文
- **示例**: `UserControllerIntegrationTest`

## 测试最佳实践

### 1. 测试命名
- 使用描述性的测试方法名
- 遵循 `test[MethodName]_[Scenario]_[ExpectedResult]` 格式
- 使用 `@DisplayName` 注解提供中文描述

### 2. 测试结构
- 使用 Given-When-Then 结构
- 每个测试方法只测试一个功能点
- 使用 `@BeforeEach` 进行测试准备

### 3. Mock使用
- 使用 `@Mock` 注解创建Mock对象
- 使用 `@InjectMocks` 注入Mock对象
- 使用 `when().thenReturn()` 设置Mock行为

### 4. 断言
- 使用明确的断言方法
- 测试边界条件和异常情况
- 验证Mock对象的调用

## 测试覆盖率

### 目标覆盖率
- 单元测试覆盖率: > 80%
- 集成测试覆盖率: > 60%
- 总体测试覆盖率: > 70%

### 查看覆盖率报告
```bash
mvn jacoco:report
```

## 持续集成

### GitLab CI/CD
测试已集成到GitLab CI/CD流水线中：
- 编译阶段运行单元测试
- 测试阶段运行集成测试
- 只有测试通过才能部署

### 测试环境
- 使用H2内存数据库
- 独立的测试配置文件
- 随机端口避免冲突

## 故障排除

### 常见问题

1. **测试失败**
   - 检查测试数据是否正确
   - 验证Mock对象设置
   - 查看测试日志

2. **依赖问题**
   - 确保所有测试依赖已添加
   - 检查模块间依赖关系

3. **数据库连接问题**
   - 确认H2数据库配置正确
   - 检查测试配置文件

### 调试技巧

1. **启用详细日志**
   ```yaml
   logging:
     level:
       com.example: DEBUG
   ```

2. **使用断点调试**
   - 在IDE中设置断点
   - 使用Debug模式运行测试

3. **查看测试报告**
   ```bash
   mvn surefire-report:report
   ```

## 扩展测试

### 添加新测试
1. 在对应模块的 `src/test/java` 目录下创建测试类
2. 继承适当的基类或使用相关注解
3. 编写测试方法并添加 `@Test` 注解
4. 运行测试验证功能

### 测试数据管理
- 使用 `TestUtil` 工具类创建测试数据
- 考虑使用测试数据工厂模式
- 使用 `@TestPropertySource` 指定测试配置

### 性能测试
- 使用 `@Test` 和 `@RepeatedTest` 进行重复测试
- 使用 `@Timeout` 设置测试超时
- 考虑添加专门的性能测试模块

## 总结

本项目的测试体系完整，覆盖了从单元测试到集成测试的各个层面，确保了代码质量和系统稳定性。通过持续集成和自动化测试，可以快速发现和修复问题，提高开发效率。 