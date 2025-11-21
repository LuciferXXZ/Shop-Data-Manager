package com.docker.mall.shopdatamanager;

import com.docker.mall.shopdatamanager.entity.Product;
import com.docker.mall.shopdatamanager.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays; // 保留这个普通导入，用于 Arrays.asList()

// === 核心修复：显式导入 MockMvc 方法和结果匹配器 ===
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;   // 导入 get()
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;  // 导入 post()
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // 导入 jsonPath()
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;   // 导入 status()
// 🔴 其它所有静态导入（比如 MockMvcRequestBuilders.*）都删掉，只保留上面这四行，就能解决 IDEA 的警告！

@SpringBootTest
@AutoConfigureMockMvc
public class ShopDataManagerApplicationTests { // 修复了 class 的 public 权限问题

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void testGetAllProducts() throws Exception {
        // 1. 准备模拟数据
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("测试商品A");
        p1.setPrice(BigDecimal.valueOf(100.0));

        // 2. Mock Service：匹配任意参数的 findAll
        Mockito.when(productService.findAll(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new PageImpl<>(Arrays.asList(p1))); // 使用 Arrays.asList (普通导入)

        // 3. 发起请求并验证
        mockMvc.perform(get("/api/products")) // 使用显式导入的 get()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("测试商品A"));
    }

    @Test
    void testCreateProduct() throws Exception {
        Product p = new Product();
        p.setName("新商品");
        p.setPrice(BigDecimal.valueOf(99.9));

        Mockito.when(productService.save(Mockito.any(Product.class))).thenReturn(p);

        mockMvc.perform(post("/api/products") // 使用显式导入的 post()
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(p)))
                .andExpect(status().isOk());
    }
}