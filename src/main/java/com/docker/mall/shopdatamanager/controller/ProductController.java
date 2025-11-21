package com.docker.mall.shopdatamanager.controller;

import com.docker.mall.shopdatamanager.entity.Product;
import com.docker.mall.shopdatamanager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 声明这是一个 RESTful 接口控制器
@RequestMapping("/api/products") // 统一的基础路径
@CrossOrigin(origins = "*") // 允许跨域（为了配合后面前端调用，防止报错）
public class ProductController {

    @Autowired
    private ProductService service;

    // 1. 获取列表 (GET /api/products)
    @GetMapping
    public List<Product> getAllProducts() {
        return service.findAll();
    }

    // 2. 获取单个详情 (GET /api/products/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. 新增商品 (POST /api/products)
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return service.save(product);
    }

    // 4. 更新商品 (PUT /api/products/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return service.findById(id)
                .map(existing -> {
                    existing.setName(product.getName());
                    existing.setPrice(product.getPrice());
                    existing.setDescription(product.getDescription());
                    return ResponseEntity.ok(service.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. 删除商品 (DELETE /api/products/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}