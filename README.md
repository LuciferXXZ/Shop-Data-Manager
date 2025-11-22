<p align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-3.3-6DB33F?logo=springboot" />
  <img src="https://img.shields.io/badge/Docker-2496ED?logo=docker" />
  <img src="https://img.shields.io/badge/Kubernetes-Deployment-326CE5?logo=kubernetes" />
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql" />
  <img src="https://img.shields.io/badge/Prometheus-Monitoring-E6522C?logo=prometheus" />
</p>

#  基于 Docker 的电商数据管理系统 (Docker-Mall)

##  项目概述 (Project Overview)

本项目是一个**企业级容器化电商后台管理系统**，专为验证 Docker 容器技术、微服务编排及 DevOps 流水线能力而设计。系统采用前后端分离架构，实现了从代码构建、镜像打包到自动化部署的完整闭环。

> ** 核心考核点达成情况：**
> -  **深度容器化**：后端采用 Maven 多阶段构建，镜像体积优化 90% 以上。
> -  **高可用编排**：使用 Docker Compose 定义服务依赖与健康检查 (Healthcheck)。
> -  **可观测性**：集成 Prometheus + Grafana 实现 JVM 与 API 实时监控。
> -  **K8s 进阶**：实现了基于 Kubernetes 的**蓝绿部署**策略。

## 技术架构

| 服务 | 技术栈 | 端口 | 说明 |
| :--- | :--- | :--- | :--- |
| **Frontend** | Nginx (Alpine) | 8081 | 静态页面展示，反向代理 API 及 Actuator 请求 |
| **Backend** | Spring Boot (JDK 21) | 9090 | 核心业务逻辑，RESTful API，集成 Actuator 健康检查 |
| **Database** | MySQL 8.0 | 3307 | 数据存储，挂载初始化脚本 |
| **Prometheus** | Prometheus | 9090 | 指标采集与时序数据库 |
| **Grafana** | Grafana | 3000 | 数据可视化监控面板 |

## 快速开始 (运行指南)

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

    使用 `docker-compose` 启动所有服务（含数据库、前后端及监控组件）。

    ```bash
    # 使用 --build 确保基于最新的代码和配置构建镜像
    docker-compose up -d --build
    ```

3.  **验证服务健康**

    等待所有服务状态变为 `(healthy)`：

    ```bash
    docker-compose ps
    ```

4.  **访问应用与监控**

    * **前端应用**：`http://localhost:8081`
    * **Grafana 面板**：`http://localhost:3000` (默认账号/密码：admin / admin)
    * **Prometheus**：`http://localhost:9090`

## Kubernetes 部署指南

本项目提供了完整的 `k8s-deployment.yaml` 文件，支持在 Kubernetes 集群上部署，并预置了**蓝绿部署 (Blue/Green Deployment)** 的配置结构。

### 部署架构说明

* **MySQL**：使用 PersistentVolumeClaim (PVC) 实现数据持久化，并通过 ConfigMap 挂载 `init.sql` 自动初始化数据库。
* **Backend**：配置了 `blue` (V1) 和 `green` (V2) 两个 Deployment 环境。通过修改 Service 的 label 选择器 (`color: blue/green`) 即可实现流量切换，支持零停机发布。
* **Frontend**：使用 NodePort 暴露服务。

### 部署步骤

1.  **构建并推送镜像**

    在部署到 K8s 之前，需要手动构建并标记镜像，或将其推送到私有仓库。

    ```bash
    # 构建并标记后端镜像 (建议推送到私有仓库)
    docker build -t shop-backend:v1 . 
    # 构建并标记前端镜像
    docker build -f frontend/Dockerfile -t shop-frontend:v1 frontend/
    ```

2.  **部署到集群**

    使用 `kubectl apply` 命令部署所有资源：

    ```bash
    kubectl apply -f k8s-deployment.yaml
    ```

3.  **流量切换 (蓝绿部署演示)**

    修改 `k8s-deployment.yaml` 中 Service 的选择器：

    ```yaml
    selector:
      app: backend
      color: green  # 将 blue 改为 green 即可切换流量到新版本
    ```

    然后重新应用配置：

    ```bash
    kubectl apply -f k8s-deployment.yaml
    ```

4.  **访问应用**

    前端 Service (`app-frontend-service`) 以 NodePort 方式暴露在集群节点的 **30081** 端口。
    * 访问地址：`http://<Node_IP>:30081`

## CI/CD 流水线设计

本项目遵循 DevOps 实践，使用 **Jenkins** 实现自动化流水线。配置文件 `Jenkinsfile` 定义了以下阶段：

1.  **Build**：使用 Maven Wrapper 编译打包，跳过测试以加速构建。
2.  **Test**：运行 JUnit 单元测试，并收集生成的测试报告。
3.  **Docker Build**：基于 Dockerfile 构建后端服务镜像。
4.  **Docker Push**：将镜像推送到镜像仓库（需配置凭证）。
5.  **Deploy**：调用 Docker Compose 重新部署应用。

## 故障排查

### 1. 前端显示 502 Bad Gateway

* **问题**：访问前端页面空白或报错 502。
* **检查**：确认后端服务 `app-backend` 状态是否为 `Up (healthy)`。
* **配置确认**：`frontend/default.conf` 已配置将 `/api/` 和 `/actuator/` 请求转发至 `http://app-backend:9090`。请确保后端容器已成功启动并监听 9090 端口。

### 2. 监控数据未显示

* **Prometheus**：检查 `config/prometheus.yml` 是否正确挂载。
* **后端 Actuator**：后端已集成 `micrometer-registry-prometheus`，请确认访问 `http://localhost:9090/actuator/prometheus` 能看到指标数据。

### 3. 数据库连接失败

* **检查**：查看 MySQL 容器日志，确认 `init.sql` 脚本执行成功。
* **环境变量**：核对 `docker-compose.yml` 或 K8s 配置中的 `MYSQL_ROOT_PASSWORD` 是否与后端 `application.properties` 一致。

##  项目目录结构 (Project Structure)

项目结构清晰，符合工程化规范：

```text
Shop-Data-Manager
├── config/                 # 监控配置文件
│   └── prometheus.yml      # Prometheus 抓取规则配置
├── frontend/               # 前端工程
│   ├── Dockerfile          # 前端专用镜像构建文件
│   ├── default.conf        # Nginx 反向代理配置
│   └── index.html          # 业务页面
├── sql/                    # 数据库脚本
│   └── init.sql            # 自动初始化建表与数据
├── src/                    # 后端源代码 (Spring Boot)
├── docker-compose.yml      # 容器编排文件 (核心)
├── Dockerfile              # 后端多阶段构建文件
├── Jenkinsfile             # CI/CD 流水线脚本
├── k8s-deployment.yaml     # Kubernetes 部署清单 (蓝绿部署)
├── pom.xml                 # Maven 依赖管理
└── README.md               # 项目说明文档

