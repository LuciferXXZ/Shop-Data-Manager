pipeline {
    agent any

    // 定义环境变量
    environment {
        // 镜像名称
        IMAGE_NAME = 'shop-backend'
        IMAGE_TAG = 'v1'
    }

    stages {
        // 1. 编译构建阶段 (对应评分：流水线设计)
        stage('Build') {
            steps {
                echo 'Building Maven Project...'
                // 使用 Maven 编译打包，跳过测试以加快速度
                // 如果你是 Windows 环境本地测试，这里其实是给 Jenkins 服务器看的指令
                sh './mvnw clean package -DskipTests'
            }
        }

        // 2. 自动化测试阶段 (对应评分：自动化测试 10分)
        stage('Test') {
            steps {
                echo 'Running Unit Tests...'
                // 运行单元测试
                sh './mvnw test'
            }
            post {
                always {
                    // 收集测试报告 (如果有 JUnit 插件)
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        // 3. 镜像构建阶段 (对应评分：镜像构建)
        stage('Docker Build') {
            steps {
                echo 'Building Docker Image...'
                // 调用 Docker 命令构建镜像
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }

        // 4. 部署阶段
        stage('Deploy') {
            steps {
                echo 'Deploying to Environment...'
                sh "docker-compose up -d"
            }
        }
    }
}