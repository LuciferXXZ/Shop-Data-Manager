package com.docker.mall.shopdatamanager.repository;

import com.docker.mall.shopdatamanager.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// 继承 JpaRepository，直接拥有 CRUD 功能
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 可以在这里定义自定义查询，比如 findByName 等，暂时不需要
}