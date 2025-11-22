<p align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-3.3-6DB33F?logo=springboot" />
  <img src="https://img.shields.io/badge/Docker-2496ED?logo=docker" />
  <img src="https://img.shields.io/badge/Kubernetes-Deployment-326CE5?logo=kubernetes" />
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql" />
  <img src="https://img.shields.io/badge/Prometheus-Monitoring-E6522C?logo=prometheus" />
</p>

# 🛒 基于 Docker 的电商数据管理系统 (Docker-Mall)

## 📖 项目概述 (Project Overview)

本项目是一个**企业级容器化电商后台管理系统**，专为验证 Docker 容器技术、微服务编排及 DevOps 流水线能力而设计。系统采用前后端分离架构，实现了从代码构建、镜像打包到自动化部署的完整闭环。

> **✨ 核心考核点达成情况：**
> - ✅ **深度容器化**：后端采用 Maven 多阶段构建，镜像体积优化 90% 以上。
> - ✅ **高可用编排**：使用 Docker Compose 定义服务依赖与健康检查 (Healthcheck)。
> - ✅ **可观测性**：集成 Prometheus + Grafana 实现 JVM 与 API 实时监控。
> - ✅ **K8s 进阶**：实现了基于 Kubernetes 的**蓝绿部署**策略。

## 🏗️ 技术栈与架构 (Tech Stack)

| 模块 | 技术选型 | 版本 | 说明 |
| :--- | :--- | :--- | :--- |
| **Frontend** | Nginx + HTML5 + Bootstrap | Alpine | 静态资源服务，配置反向代理解决跨域问题 |
| **Backend** | Spring Boot (Java 21) | 3.3.0 | RESTful API，集成 Actuator 监控与 JPA |
| **Database** | MySQL | 8.0 | 数据持久化存储，配置字符集与时区 |
| **DevOps** | Jenkins + Docker Compose | Latest | 自动化 CI/CD 流水线 |
| **Monitor** | Prometheus + Grafana | Latest | 服务指标采集与可视化大屏 |

## 📂 项目目录结构 (Project Structure)

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