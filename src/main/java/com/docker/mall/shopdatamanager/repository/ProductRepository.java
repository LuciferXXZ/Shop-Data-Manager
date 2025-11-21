package com.docker.mall.shopdatamanager.repository;

import com.docker.mall.shopdatamanager.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 分页模糊查询
    // SQL: SELECT * FROM product WHERE name LIKE %name% LIMIT ?, ?
    Page<Product> findByNameContaining(String name, Pageable pageable);
}