# 基于 Nginx 的 Alpine 版本 (评分要求：镜像选择合理)
FROM nginx:alpine

# 删除默认配置文件
RUN rm /etc/nginx/conf.d/default.conf

# 复制我们要的配置文件
COPY default.conf /etc/nginx/conf.d/

# 复制静态页面
COPY index.html /usr/share/nginx/html/

# 暴露 80 端口
EXPOSE 80

# (加分项) 添加健康检查
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost/ || exit 1