
FROM jenkins/jenkins:lts

# 2. 切换到 root 安装工具
USER root

# 3. 安装必要的系统工具
RUN apt-get update && \
    apt-get install -y lsb-release curl gnupg

# 4. 【核心修复】添加 Docker 官方 GPG 密钥和软件源
# 这样才能安装到最新版的 Docker CLI，解决 "client version too old" 的问题
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg && \
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/debian \
    $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list

# 5. 安装最新的 Docker CLI
RUN apt-get update && \
    apt-get install -y docker-ce-cli

# 6. 安装最新的 Docker Compose
RUN curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose

# 7. 切回 jenkins 用户
USER jenkins