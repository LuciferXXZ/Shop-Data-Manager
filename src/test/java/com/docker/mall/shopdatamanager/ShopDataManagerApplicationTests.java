package com.docker.mall.shopdatamanager;

import com.docker.mall.shopdatamanager.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

// === 核心修复：显式导入 MockMvc 方法和结果匹配器 ===
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional // 确保每个测试方法执行后，所有数据库操作都会自动回滚。
public class ShopDataManagerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    // 1. 测试新增 (Create)
    @Test
    @Order(1) // 保证第一个执行
    void testCreateProduct() throws Exception {
        Product p = new Product();
        p.setName("测试商品 - 键盘");
        p.setPrice(BigDecimal.valueOf(299.99));
        p.setDescription("测试新增的商品");

        // 发起 POST 请求，验证是否成功（返回 200 OK）
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        // 使用 ObjectMapper 将对象转换为 JSON 字符串
                        .content(new ObjectMapper().writeValueAsString(p)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()) // 验证 ID 是否已被生成
                .andExpect(jsonPath("$.name").value("测试商品 - 键盘"));
    }

    // 2. 测试分页查询 (Read - List with Search)
    @Test
    @Order(2)
    void testGetAllProductsAndSearch() throws Exception {
        // 2.1 测试默认分页列表
        mockMvc.perform(get("/api/products?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray()) // 验证返回的是分页结构
                .andExpect(jsonPath("$.totalPages").exists())
                // ⚠️ 已回滚：列表现在是倒序排列，第一个应该是 ID 最大的最新商品
                .andExpect(jsonPath("$.content[0].name").value("测试商品 - 键盘"));

        // 2.2 测试模糊查询功能 (根据 name=键盘 搜索)
        mockMvc.perform(get("/api/products?name=键盘"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                // ⚠️ 已回滚：搜索结果也是倒序，第一个应该是 ID 最大的最新商品
                .andExpect(jsonPath("$.content[0].name").value("测试商品 - 键盘"));
    }

    // 3. 测试获取详情 (Read - Detail)
    @Test
    @Order(3)
    void testGetProductById() throws Exception {
        // 查询 ID=3 的商品 ("机械键盘")，保持这个 ID 以防止数据污染，确保测试可靠性。
        mockMvc.perform(get("/api/products/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("机械键盘"));

        // 测试查询不存在的 ID
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound()); // 验证返回 404
    }

    // 4. 测试修改 (Update)
    @Test
    @Order(4)
    void testUpdateProduct() throws Exception {
        // 假设要修改 ID=1 的商品
        Product updateData = new Product();
        updateData.setName("Docker入门教程 (已修改)");
        updateData.setPrice(BigDecimal.valueOf(66.66)); // 修改价格
        updateData.setDescription("修改后的描述");

        // 发起 PUT 请求，修改 ID 为 1 的商品
        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Docker入门教程 (已修改)"))
                .andExpect(jsonPath("$.price").value(66.66)); // 验证价格更新
    }

    // 5. 测试删除 (Delete)
    @Test
    @Order(5)
    void testDeleteProduct() throws Exception {
        // 假设要删除 ID=2 的商品
        mockMvc.perform(delete("/api/products/2"))
                .andExpect(status().isOk()); // 验证删除成功（返回 200 OK）

        // 验证商品是否真的被删除 (查询 ID=2 详情接口应该返回 404)
        mockMvc.perform(get("/api/products/2"))
                .andExpect(status().isNotFound());
    }
}