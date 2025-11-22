<p align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-3.3-6DB33F?logo=springboot" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker" />
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql" />
  <img src="https://img.shields.io/badge/Kubernetes-Deployment-326CE5?logo=kubernetes" />
</p>

# 🛒 基于 Docker 的电商数据管理系统 (Docker-Mall)

## 📋 项目概述

本项目是一个基于容器化技术的电商后台管理系统，采用前后端分离架构，并实现了完整的 CI/CD 流水线。

* **前端**：Nginx + HTML5 + Bootstrap
* **后端**：Spring Boot 3 + JPA + Docker 多阶段构建 (JDK 21)
* **数据库**：MySQL 8.0 (支持数据持久化与自动初始化)
* **CI/CD**：Jenkins + Maven + Docker Compose

## 🏗️ 技术架构

| 服务 | 技术栈 | 端口 | 说明 |
| :--- | :--- | :--- | :--- |
| **Frontend** | Nginx (Alpine) | 8081 | 静态页面展示，反向代理 API 请求 |
| **Backend** | Spring Boot (JDK 21) | 8080 | 核心业务逻辑，RESTful API |
| **Database** | MySQL 8.0 | 3307 | 数据存储，挂载初始化脚本 |

## 🚀 快速开始 (运行指南)

### 前置条件

* Docker Desktop (版本 4.0+)
* Git

### 启动步骤

1.  **克隆仓库**
    ```bash
    git clone <你的仓库地址>
    cd Shop-Data-Manager
    ```

2.  **构建并启动服务**
    使用 `docker-compose up` 命令启动所有服务。服务将严格按照依赖关系（MySQL -> Backend -> Frontend）启动。
    ```bash
    # 使用 --build 确保基于最新的代码和配置构建镜像
    docker-compose up -d --build
    ```

3.  **验证服务健康**
    等待所有服务状态变为 `(healthy)`：
    ```bash
    docker-compose ps
    ```

4.  **访问应用**
    在浏览器中访问：`http://localhost:8081`

---

## ☸️ Kubernetes 部署指南 (拓展功能)

本项目提供了完整的 `k8s-deployment.yaml` 文件，用于在 Kubernetes 集群上部署应用。

### 部署步骤

1.  **构建并标记镜像**
    在部署到 K8s 之前，需要手动构建并标记镜像，或将其推送到私有仓库。
    ```bash
    # 构建并标记后端镜像
    docker build -t shop-backend:v1 . 
    # 构建并标记前端镜像
    docker build -f frontend/Dockerfile -t shop-frontend:v1 frontend/
    ```

2.  **部署到 K8s 集群**
    使用 `kubectl apply` 命令部署所有资源（PVC, Deployment, Service）：
    ```bash
    kubectl apply -f k8s-deployment.yaml
    ```

3.  **访问应用**
    前端 Service (`app-frontend-service`) 以 NodePort 方式暴露在集群节点的 **30081** 端口。
    *访问地址：* `http://<Node_IP>:30081`

---

## 🔄 CI/CD 流水线设计

本项目遵循 DevOps 实践，使用 **Jenkins** 实现自动化流水线，配置文件为根目录下的 `Jenkinsfile`。

流水线包含以下阶段：

1.  **Build**: 代码编译与打包。
2.  **Test**: 自动化执行 JUnit 单元测试，并生成测试报告。
3.  **Docker Build**: 构建后端服务镜像。
4.  **Docker Push**: 将镜像推送到镜像仓库。
5.  **Deploy**: 基于 Docker Compose 的自动化部署。

---

## 🛠️ 故障排查 (Troubleshooting)

### 1. 前端无法连接后端 (502 Bad Gateway)

* **问题**：访问前端 `http://localhost:8081` 时页面空白或控制台报错 `502 Bad Gateway`。
* **检查**：检查后端服务 `app-backend` 是否启动并健康。
    ```bash
    docker-compose ps 
    # 状态应为 Up (healthy)。若不是，检查 app-backend 容器日志。
    ```
* **修复**：确认 `frontend/default.conf` 中的代理地址是否已修正为 Docker Compose 服务名 `http://app-backend:9090/api/`。

### 2. 数据库连接失败或启动缓慢

* **问题**：后端容器启动失败并报错 `Connection Refused`，或 MySQL 容器无法启动。
* **检查**：检查 `mysql` 服务的日志，查看其初始化脚本 (`init.sql`) 是否成功执行。
    ```bash
    docker-compose logs mysql
    ```
* **修复**：检查 `docker-compose.yml` 中 `mysql` 服务的环境变量 (`MYSQL_ROOT_PASSWORD`, `MYSQL_DATABASE`) 是否正确。

### 3. 后端服务状态不是 (healthy)

* **问题**：`app-backend` 容器状态为 `Up` 但不是 `Up (healthy)`。
* **检查**：Actuator 健康检查端点可能未成功暴露。
    ```bash
    # 尝试从容器内部检查健康端点（需要进入容器）
    docker exec -it mall-backend curl -f http://localhost:9090/actuator/health
    ```
* **修复**：确保 `Dockerfile` 中安装了 `curl` 工具，并且 `application.properties` 中已包含：`management.endpoints.web.exposure.include=health`。