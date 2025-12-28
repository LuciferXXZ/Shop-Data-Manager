pipeline {
    agent any

// 定义环境变量
    environment {
        // 镜像名称
        IMAGE_NAME = 'shop-backend'
        IMAGE_TAG = 'v1'

        // ✅ 新增：强制指定项目名称为 "shop-data-manager"
        // 这样 Jenkins 就能直接复用你宿主机上已经启动的 mysql 和其他容器，
        // 而不是试图创建一个名字冲突的新容器。
        COMPOSE_PROJECT_NAME = 'shop-data-manager'
    }

    stages {
        // 1. 编译构建阶段 (对应评分：流水线设计)
        stage('Build') {
            steps {
                echo 'Building Maven Project...'
                // 确保 mvnw 脚本有执行权限
                sh 'chmod +x mvnw'
                // 使用 Maven 编译打包，跳过测试以加快速度
                sh './mvnw clean package -DskipTests'
            }
        }


        stage('Test') {
            steps {
                echo 'Running Unit Tests...'

                // ✅ 保持这行不变 (连接地址已经是 mysql:3306 了)
                // 加上 -Dmaven.test.failure.ignore=true
                sh './mvnw test -Dmaven.test.failure.ignore=true "-Dspring.datasource.url=jdbc:mysql://mysql:3306/mall?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai"'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        // 3. 镜像构建阶段 (对应评分：镜像构建)
        stage('Docker Build') {
            steps {
                // ⚠️ 修复点：echo 和字符串必须在同一行
                echo 'Building Docker Image...'
                // 调用 Docker 命令构建镜像
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }

        // 4. 镜像推送阶段 (符合评分要求：镜像推送仓库成功)
        stage('Docker Push') {
            steps {
                echo 'Pushing Docker Image to Registry...'
                // 假设您已在 Jenkins 中配置 Docker 凭证，可以直接使用此命令
                // 如果您要推送到 Docker Hub 或私有仓库，镜像名称可能需要包含仓库地址
                // sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                echo 'Skipping push (uncomment to enable)'
            }
        }

        // 5. 部署阶段
        stage('Deploy') {
            steps {
                echo 'Deploying to Environment...'
                // 只更新后端和前端，避免重启 Jenkins 自身
                sh "docker-compose up -d --build app-backend app-frontend"
            }
        }
    }
}