package com.docker.mall.shopdatamanager.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data // Lombok 注解：自动生成 Getter/Setter/ToString
@Entity // JPA 注解：声明这是一个实体类
@Table(name = "product") // 对应数据库中的 product 表
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private String description;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    // 在插入数据前，自动设置创建时间
    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    // 在更新数据前，自动更新修改时间
    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}