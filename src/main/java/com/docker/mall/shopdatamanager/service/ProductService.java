package com.docker.mall.shopdatamanager.service;

import com.docker.mall.shopdatamanager.entity.Product;
import com.docker.mall.shopdatamanager.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ⚠️ 最终修复：新增导入

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    // 1. 分页查询 (支持搜索) - 给列表用
    public Page<Product> findAll(String keyword, int page, int size) {
        // 创建分页对象：按 id 倒序排列 (已回滚到倒序)
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        if (keyword != null && !keyword.isEmpty()) {
            return repository.findByNameContaining(keyword, pageable);
        }
        return repository.findAll(pageable);
    }

    // 2. 查询所有 (不分页) - 给图表用
    public List<Product> findAllList() {
        return repository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    // ⚠️ 最终修复：添加 @Transactional 确保事务在 K8s 环境中正确提交
    @Transactional
    public Product save(Product product) {
        return repository.save(product);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}