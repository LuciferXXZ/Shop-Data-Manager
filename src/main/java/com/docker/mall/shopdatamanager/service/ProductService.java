package com.docker.mall.shopdatamanager.service;

import com.docker.mall.shopdatamanager.entity.Product;
import com.docker.mall.shopdatamanager.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // 告诉 Spring 这是一个业务类
public class ProductService {

    @Autowired
    private ProductRepository repository;

    // 查询所有商品
    public List<Product> findAll() {
        return repository.findAll();
    }

    // 根据ID查询
    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    // 保存或更新商品
    public Product save(Product product) {
        return repository.save(product);
    }

    // 删除商品
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}