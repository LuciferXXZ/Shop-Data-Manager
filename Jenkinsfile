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
                sh './mvnw clean package -DskipTests'
            }
        }

// 2. 自动化测试阶段
        stage('Test') {
            steps {
                echo 'Running Unit Tests...'
                // 💡 重点：必须使用 mysql (容器服务名) 和 3306 (容器内部端口)
                sh './mvnw test -Dspring.datasource.url=jdbc:mysql://mysql:3306/mall?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai'
            }
            post {
                always {
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

        // ⚠️ 修复点：新增镜像推送阶段 (符合评分要求：镜像推送仓库成功)
        stage('Docker Push') {
            steps {
                echo 'Pushing Docker Image to Registry...'
                // 假设您已在 Jenkins 中配置 Docker 凭证，可以直接使用此命令
                // 如果您要推送到 Docker Hub 或私有仓库，镜像名称可能需要包含仓库地址，例如：
                // sh "docker push your-registry-domain/${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
            }
        }

        // 4. 部署阶段
        //流水线Test1
        stage('Deploy') {
            steps {
                echo 'Deploying to Environment...'
                // 确保 Jenkins Agent 具有 docker-compose 权限
                sh "docker-compose up -d"
            }
        }
    }
}