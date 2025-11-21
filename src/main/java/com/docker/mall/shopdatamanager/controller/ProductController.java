package com.docker.mall.shopdatamanager.controller;

import com.docker.mall.shopdatamanager.entity.Product;
import com.docker.mall.shopdatamanager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService service;

    // 1. 获取分页列表 (GET /api/products?page=0&size=5&name=xxx)
    @GetMapping
    public Page<Product> getAllProducts(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return service.findAll(name, page, size);
    }

    // 2. 获取所有数据 (GET /api/products/all) - 专门给图表用
    @GetMapping("/all")
    public List<Product> getAllProductsList() {
        return service.findAllList();
    }

    // 3. 获取详情
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. 新增
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return service.save(product);
    }

    // 5. 修改
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

    // 6. 删除
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}