# GitLab CI/CD 流程说明

## 概述

本项目配置了完整的GitLab CI/CD流水线，包含构建、测试、打包和部署等阶段。

## 流水线阶段

### 1. 构建阶段 (Build)
- **任务**: `build`
- **镜像**: `maven:3.9.6-openjdk-17`
- **功能**: 编译项目代码
- **触发条件**: main分支、develop分支、合并请求
- **产物**: 编译后的class文件

### 2. 测试阶段 (Test)
- **任务**: `test`
- **镜像**: `maven:3.9.6-openjdk-17`
- **服务**: MySQL 8.0数据库
- **功能**: 运行单元测试和集成测试
- **触发条件**: main分支、develop分支、合并请求
- **产物**: 测试报告、覆盖率报告

### 3. 打包阶段 (Package)
- **任务**: `package`
- **镜像**: `maven:3.9.6-openjdk-17`
- **功能**: 打包项目为JAR文件
- **触发条件**: main分支、develop分支、标签
- **产物**: 可执行的JAR文件

### 4. Docker构建阶段
- **任务**: `docker_build`
- **镜像**: `docker:latest`
- **服务**: Docker-in-Docker
- **功能**: 构建Docker镜像并推送到GitLab容器注册表
- **触发条件**: main分支、标签

### 5. 发布阶段 (Publish)
- **Maven仓库部署**: `deploy_to_maven_repository`
  - 自动部署JAR包到私有Maven仓库
  - 根据分支/标签决定部署到Releases或Snapshots仓库
  - 触发条件: main分支、develop分支、标签

### 6. 部署阶段 (Deploy)
- **测试环境部署**: `deploy_test`
  - 自动部署到测试环境
  - 触发条件: develop分支
  
- **生产环境部署**: `deploy_prod`
  - 手动触发部署到生产环境
  - 触发条件: main分支、标签

## 环境变量配置

### GitLab CI/CD变量

在GitLab项目设置中配置以下变量：

#### 部署相关变量
```
SSH_PRIVATE_KEY          # SSH私钥
SSH_KNOWN_HOSTS          # 服务器SSH公钥
TEST_SERVER_USER         # 测试服务器用户名
TEST_SERVER_HOST         # 测试服务器地址
TEST_SERVER_PATH         # 测试服务器部署路径
PROD_SERVER_USER         # 生产服务器用户名
PROD_SERVER_HOST         # 生产服务器地址
PROD_SERVER_PATH         # 生产服务器部署路径
```

#### Docker相关变量
```
CI_REGISTRY_USER         # GitLab容器注册表用户名
CI_REGISTRY_PASSWORD     # GitLab容器注册表密码
CI_REGISTRY              # GitLab容器注册表地址
CI_REGISTRY_IMAGE        # 镜像名称
```

#### Maven仓库相关变量
```
MAVEN_REPOSITORY_USERNAME    # Maven仓库用户名
MAVEN_REPOSITORY_PASSWORD    # Maven仓库密码
MAVEN_REPOSITORY_HOST        # Maven仓库主机地址
MAVEN_REPOSITORY_URL         # Releases仓库URL
MAVEN_SNAPSHOT_URL          # Snapshots仓库URL
```

## 分支策略

### main分支
- 生产环境代码
- 触发: 构建、测试、打包、Docker构建、Maven仓库发布、生产部署(手动)

### develop分支
- 开发环境代码
- 触发: 构建、测试、打包、Maven仓库发布(SNAPSHOT)、测试环境部署

### 功能分支
- 开发新功能
- 触发: 构建、测试
- 通过合并请求集成到develop分支

## 部署流程

### Maven仓库发布
1. **Releases版本发布**
   - 代码推送到main分支或创建标签
   - 自动触发CI/CD流水线
   - 构建成功后自动发布到Releases仓库

2. **Snapshots版本发布**
   - 代码推送到develop分支
   - 自动触发CI/CD流水线
   - 构建成功后自动发布到Snapshots仓库

### 测试环境部署
1. 代码推送到develop分支
2. 自动触发CI/CD流水线
3. 通过测试后自动部署到测试环境

### 生产环境部署
1. 代码合并到main分支或创建标签
2. 自动触发CI/CD流水线
3. 手动确认后部署到生产环境

## 本地开发

### 使用Docker Compose
```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f app

# 停止服务
docker-compose down
```

### 手动构建Docker镜像
```bash
# 构建镜像
docker build -t multi-springboot:latest .

# 运行容器
docker run -p 8080:8080 multi-springboot:latest
```

### 使用Maven仓库
```bash
# 从私有仓库下载依赖
mvn clean install

# 发布到私有仓库
mvn clean deploy

# 使用特定版本
mvn dependency:get -Dartifact=com.example:multi-springboot:1.0.0
```

## 监控和日志

### 健康检查
- 应用健康检查: `http://localhost:8080/actuator/health`
- 数据库健康检查: MySQL连接测试
- Redis健康检查: Redis连接测试

### 日志查看
```bash
# 查看应用日志
docker-compose logs -f app

# 查看数据库日志
docker-compose logs -f mysql

# 查看Redis日志
docker-compose logs -f redis
```

## 故障排除

### 常见问题

1. **构建失败**
   - 检查Maven依赖是否正确
   - 确认JDK版本是否为17
   - 查看构建日志获取详细错误信息

2. **测试失败**
   - 确认MySQL服务是否正常启动
   - 检查数据库连接配置
   - 查看测试日志获取详细错误信息

3. **部署失败**
   - 确认SSH密钥配置是否正确
   - 检查服务器连接是否正常
   - 确认部署脚本是否存在

### 调试命令
```bash
# 进入容器调试
docker-compose exec app sh

# 查看容器资源使用情况
docker stats

# 清理Docker资源
docker system prune -a
```

## 安全注意事项

1. **敏感信息保护**
   - 不要在代码中硬编码密码
   - 使用GitLab CI/CD变量存储敏感信息
   - 定期轮换SSH密钥和密码

2. **权限控制**
   - 限制生产环境部署权限
   - 使用最小权限原则
   - 定期审查访问权限

3. **安全扫描**
   - 定期进行依赖安全扫描
   - 及时更新有安全漏洞的依赖
   - 监控安全警报

## 性能优化

1. **构建优化**
   - 使用Maven缓存减少构建时间
   - 并行执行测试
   - 优化Docker镜像大小

2. **部署优化**
   - 使用蓝绿部署减少停机时间
   - 配置自动回滚机制
   - 监控部署性能指标

## Maven仓库配置

### 私有仓库设置
1. **Nexus Repository Manager**
   - 安装并配置Nexus Repository Manager
   - 创建Releases和Snapshots仓库
   - 配置用户认证和权限

2. **Maven Settings配置**
   - 使用提供的`maven-settings.xml`模板
   - 配置服务器认证信息
   - 设置仓库镜像

3. **项目配置**
   - 在`pom.xml`中配置`distributionManagement`
   - 添加Maven插件配置
   - 设置版本管理策略

### 版本管理策略
- **SNAPSHOT版本**: 开发阶段使用，自动递增
- **Release版本**: 稳定版本，手动发布
- **标签版本**: 特定版本，用于生产环境

## 扩展功能

### 添加代码质量检查
```yaml
code_quality:
  stage: test
  image: maven:3.9.6-openjdk-17
  script:
    - mvn checkstyle:check
    - mvn spotbugs:check
  allow_failure: true
```

### 添加安全扫描
```yaml
security_scan:
  stage: test
  image: maven:3.9.6-openjdk-17
  script:
    - mvn dependency:check
    - mvn org.owasp:dependency-check-maven:check
  allow_failure: true
```

### 添加通知功能
```yaml
notify:
  stage: deploy
  image: alpine:latest
  script:
    - curl -X POST -H 'Content-type: application/json' \
      --data "{\"text\":\"部署完成\"}" \
      $SLACK_WEBHOOK_URL
  when: on_success
``` 