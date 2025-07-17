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

### Maven默认行为
- **版本号以`-SNAPSHOT`结尾**: 自动部署到`snapshotRepository`
- **版本号不以`-SNAPSHOT`结尾**: 自动部署到`repository`
- **无需手动指定仓库**: Maven根据版本号自动选择正确的仓库

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

# 多模块SpringBoot项目CI/CD流程

## 概述

本项目实现了完整的CI/CD流程，包括构建、测试、打包、部署、发布和扫描等阶段。

## 配置说明

### 1. 变量控制

通过以下变量可以控制各个阶段的执行：

```yaml
variables:
  build_enable: "true"      # 构建阶段
  test_enable: "true"       # 测试阶段
  package_enable: "true"    # 打包阶段
  deploy_enable: "true"     # 部署阶段
  publish_enable: "true"    # 发布阶段
  scan_enable: "true"       # 扫描阶段
```

### 2. 分支控制

只有以下分支和标签会触发CI/CD流程：
- `main` 分支
- `master` 分支
- 任何标签（tag）

### 3. 项目路径配置

如果`.gitlab-ci.yml`不在项目根目录，可以通过以下变量指定：

```yaml
variables:
  PROJECT_PATH: .  # 修改为实际的项目路径
```

## 阶段说明

### 构建阶段 (build)
- 编译项目代码
- 生成构建产物

### 测试阶段 (test)
- 运行单元测试
- 生成测试覆盖率报告
- 收集JUnit测试报告

### 打包阶段 (package)
- 打包项目为JAR文件
- 构建Docker镜像

### 部署阶段 (deploy)
- 部署到测试环境
- 部署到生产环境（手动触发）

### 发布阶段 (publish)
- 发布到Maven私有仓库

### SonarQube分析阶段 (sonarqube)
- 代码质量分析
- 测试覆盖率分析

### 扫描阶段 (scan)
- 安全漏洞扫描

## 环境变量配置

需要在GitLab项目中配置以下环境变量：

### Maven仓库配置
- `MAVEN_REPOSITORY_USERNAME`: Maven仓库用户名
- `MAVEN_REPOSITORY_PASSWORD`: Maven仓库密码
- `MAVEN_REPOSITORY_HOST`: Maven仓库主机地址

### 部署配置
- `SSH_PRIVATE_KEY`: SSH私钥
- `SSH_KNOWN_HOSTS`: SSH已知主机
- `TEST_SERVER_USER`: 测试服务器用户名
- `TEST_SERVER_HOST`: 测试服务器主机
- `TEST_SERVER_PATH`: 测试服务器部署路径
- `PROD_SERVER_USER`: 生产服务器用户名
- `PROD_SERVER_HOST`: 生产服务器主机
- `PROD_SERVER_PATH`: 生产服务器部署路径

### SonarQube配置
- `SONAR_HOST_URL`: SonarQube服务器地址
- `SONAR_TOKEN`: SonarQube访问令牌

## 使用说明

1. 推送代码到main/master分支或创建标签
2. 在GitLab CI/CD页面查看流水线执行情况
3. 根据需要手动触发生产环境部署

## 测试覆盖率配置

### 问题描述
在CI流程中，单元测试全部通过，但SonarQube报告中显示覆盖率为0。

### 解决方案

#### 1. 添加JaCoCo插件配置
在`pom.xml`中添加JaCoCo插件配置：

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <id>prepare-agent</id>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>jacoco-check</id>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>BUNDLE</element>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.60</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

#### 2. 添加SonarQube插件配置
```xml
<plugin>
    <groupId>org.sonarsource.scanner.maven</groupId>
    <artifactId>sonar-maven-plugin</artifactId>
    <version>3.10.0.2594</version>
</plugin>
```

#### 3. 修改CI配置
在`.gitlab-ci.yml`的test阶段中添加覆盖率报告收集：

```yaml
test:
  script:
    - mvn clean test $MAVEN_CLI_OPTS
  artifacts:
    paths:
      - "**/target/surefire-reports/"
      - "**/target/site/jacoco/"    # 添加JaCoCo报告
      - "**/target/jacoco.exec"     # 添加JaCoCo执行文件
```

#### 4. 添加SonarQube分析阶段
```yaml
sonarqube:
  stage: sonarqube
  script:
    - mvn sonar:sonar $MAVEN_CLI_OPTS \
        -Dsonar.host.url=$SONAR_HOST_URL \
        -Dsonar.login=$SONAR_TOKEN \
        -Dsonar.projectKey=$CI_PROJECT_PATH_SLUG \
        -Dsonar.coverage.jacoco.xmlReportPaths=**/target/site/jacoco/jacoco.xml
```

### 常见问题排查

1. **覆盖率报告文件不存在**
   - 检查JaCoCo插件是否正确配置
   - 确认测试是否真正执行了代码

2. **SonarQube无法找到覆盖率文件**
   - 检查`sonar.coverage.jacoco.xmlReportPaths`路径配置
   - 确认覆盖率报告文件已生成

3. **覆盖率显示为0**
   - 检查测试是否覆盖了实际业务代码
   - 确认JaCoCo插件在test阶段正确执行

### 覆盖率要求
当前配置要求代码覆盖率不低于60%，可以通过修改`minimum`值来调整要求。 