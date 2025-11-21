# 🛒 基于 Docker 的电商数据管理系统 (Docker-Mall)

## 📋 项目概述
本项目是一个基于容器化技术的电商后台管理系统，采用前后端分离架构。
- **前端**：Nginx + HTML5 + Bootstrap
- **后端**：Spring Boot 3 + JPA + Docker 多阶段构建
- **数据库**：MySQL 8.0 (支持数据持久化与自动初始化)

## 🏗️ 技术架构
| 服务 | 技术栈 | 端口 | 说明 |
| --- | --- | --- | --- |
| **Frontend** | Nginx (Alpine) | 8081 | 静态页面展示，反向代理 API 请求 |
| **Backend** | Spring Boot (JDK 21) | 8080 | 核心业务逻辑，RESTful API |
| **Database** | MySQL 8.0 | 3307 | 数据存储，挂载初始化脚本 |

## 🚀 快速开始 (运行指南)

### 前置条件
- Docker Desktop (版本 4.0+)
- Git

### 启动步骤
1. **克隆仓库**
   ```bash
   git clone <你的仓库地址>
   cd Shop-Data-Manager

## 🔄 CI/CD 流水线设计
本项目遵循 DevOps 实践，使用 **Jenkins** 实现自动化流水线，配置文件为根目录下的 `Jenkinsfile`。
流水线包含以下阶段：
1. **Build**: 代码编译与打包。
2. **Test**: 自动化执行 JUnit 单元测试。
3. **Docker Build**: 构建后端服务镜像。
4. **Deploy**: 基于 Docker Compose 的自动化部署。