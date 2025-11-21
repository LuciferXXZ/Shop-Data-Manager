-- 切换到 mall 数据库
SET NAMES utf8mb4;
USE mall;

-- 创建商品表
CREATE TABLE IF NOT EXISTS product (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                       name VARCHAR(100) NOT NULL COMMENT '商品名称',
    price DECIMAL(10, 2) NOT NULL COMMENT '商品价格',
    description VARCHAR(255) COMMENT '商品描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入几条测试数据
INSERT INTO product (name, price, description) VALUES
                                                   ('Docker入门教程', 59.90, '深入浅出学习容器技术'),
                                                   ('Spring Boot实战', 89.00, 'Java后端开发必读'),
                                                   ('机械键盘', 299.00, '程序员编码神器');