# 仿微信 Netty 微服务项目

## 项目简介

本项目为仿微信的分布式即时通讯系统，采用 Spring Cloud 微服务架构，集成 Netty 实现高性能 WebSocket 聊天，支持用户注册、登录、好友管理、朋友圈、文件上传等功能。适合学习和实践分布式微服务、IM 通讯、Spring Cloud、Netty 等技术。

前端地址：https://github.com/3323223659/WeChat-uniapp

---

## 技术架构

- **微服务框架**：Spring Cloud 2022.x、Spring Boot 3.x
- **服务注册/配置中心**：Nacos
- **API 网关**：Spring Cloud Gateway
- **数据库**：MySQL 8.x
- **ORM**：MyBatis-Plus
- **缓存/消息队列**：Redis、RabbitMQ
- **分布式唯一 ID**：雪花算法（idworker-snowflake）
- **分布式协调**：Zookeeper（Curator）
- **对象存储**：MinIO
- **WebSocket 通讯**：Netty
- **容器化**：Docker（部分服务已提供 Dockerfile）
- **其他**：Lombok、Feign、AOP、JUnit

---

## 微服务体系与模块说明

```
wechat-dev/
├── wechat-common      # 公共依赖模块，工具类、枚举、异常、通用配置
├── wechat-pojo        # 实体类、VO/BO/DTO
├── api/
│   ├── auth-service-88    # 认证服务，用户注册、登录、短信验证码
│   ├── file-service-55    # 文件服务，头像/图片/视频上传，MinIO集成
│   ├── main-service-66    # 业务主服务，用户信息、朋友圈、好友、评论等
│   ├── base-service       # 基础服务，AOP、通用Feign接口等
│   └── chat-server-875    # 聊天服务，基于Netty实现WebSocket即时通讯
├── gateway-1000       # API网关，统一入口、路由、鉴权
└── pom.xml            # 父工程，统一依赖与版本管理
```

### 各模块功能简述

- **wechat-common**：项目通用工具、枚举、异常、Redis 操作、JSON 工具等。
- **wechat-pojo**：数据库实体、业务对象、视图对象。
- **auth-service-88**：用户注册、登录、短信验证码、分布式会话管理。
- **file-service-55**：文件上传、头像/图片/视频存储，集成 MinIO。
- **main-service-66**：用户信息管理、好友关系、朋友圈、评论等核心业务。
- **base-service**：AOP 日志、通用 Feign 接口、RabbitMQ 测试等。
- **chat-server-875**：基于 Netty 的 WebSocket 聊天服务，支持分布式部署，Zookeeper 注册，消息通过 RabbitMQ 分发，Redis 管理在线状态。
- **gateway-1000**：Spring Cloud Gateway 实现的 API 网关，动态路由、统一鉴权、CORS 配置。

---

## 主要技术亮点

- **Spring Cloud 微服务架构**，服务注册与发现、配置中心、负载均衡。
- **Netty 高性能 WebSocket 聊天**，支持分布式部署，自动端口分配与注册。
- **RabbitMQ 消息队列**，实现消息异步分发与解耦。
- **Redis 缓存与分布式会话**，高效存储用户 Token、验证码、在线状态等。
- **MinIO 对象存储**，支持大文件、图片、视频上传。
- **Zookeeper 协调**，Netty 节点注册与自动下线。
- **Spring Cloud Gateway**，统一 API 入口，动态路由，安全控制。
- **AOP 日志监控**，服务调用耗时统计。

---

## 环境依赖

- JDK 17+
- Maven 3.6+
- MySQL 8.x
- Redis 5.x/6.x
- RabbitMQ 3.x
- Nacos 2.x
- Zookeeper 3.x
- MinIO 8.x
- Docker（可选）

---

## 启动与使用教程

### 1. 数据库准备

- 创建数据库 `wechat-dev`，导入表结构（ 根目录下有sql脚本 ）。

### 2. 配置中心与中间件

- 启动 Nacos，配置好 `application-dev.yml` 中的 Nacos 地址。
- 启动 Redis、RabbitMQ、Zookeeper、MinIO，确保端口与各服务配置一致。

### 3. 各服务启动

- **父工程编译**：`mvn clean install -DskipTests`
- **依次启动各微服务**（可用 IDE 或命令行）：
  - `wechat-common`、`wechat-pojo`（依赖库，无需单独启动）
  - `api/auth-service-88`
  - `api/file-service-55`
  - `api/main-service-66`
  - `api/base-service`
  - `api/chat-server-875`（Netty 聊天服务，需单独 main 方法启动）
  - `gateway-1000`（API 网关，端口 1000）

### 4. 访问入口

- 网关地址：http://localhost:1000
- 认证服务：http://localhost:88
- 文件服务：http://localhost:55
- 主业务服务：http://localhost:66
- 聊天服务（WebSocket）：ws://localhost:875

### 5. 典型接口

- 用户注册/登录：`POST /passport/registOrLogin`
- 发送短信验证码：`GET /a/sms`
- 文件上传：`POST /file/uploadFace1`
- 聊天消息：WebSocket 连接 `ws://localhost:875`

---

## 目录结构说明

- `src/main/java/org/itzixi/`：Java 源码
- `src/main/resources/`：配置文件、Mapper XML
- `src/test/java/`：单元测试

---

## 常见问题

- 各服务端口、数据库、Redis、RabbitMQ、MinIO、Nacos、Zookeeper 地址需与配置文件一致。
- Netty 聊天服务需保证 Redis、Zookeeper、RabbitMQ 正常运行。
- 文件上传需配置 MinIO 并保证 bucket 存在。

---

## 参考命令

```bash
# 编译所有模块
mvn clean install -DskipTests

# 启动单个服务（以auth-service-88为例）
cd api/auth-service-88
mvn spring-boot:run

# 启动Netty聊天服务
cd api/chat-server-875
# 运行 org.itzixi.netty.ChatServer 的 main 方法
```

---

## 联系与贡献

如有问题或建议，欢迎 issue 或 PR。

---

如需更详细的接口文档、数据库建表 SQL、前端对接说明等，可进一步补充。
