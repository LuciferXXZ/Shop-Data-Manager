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
import java.util.Arrays;

// 这一行非常重要！静态导入
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ShopDataManagerApplicationTests {

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

        // 2. 修改 Mock 行为：
        // 因为 Controller 现在调用的是带参数的 findAll，所以这里用 any() 匹配任意参数
        // 返回值必须是 Page 类型，所以用 new PageImpl(...) 包裹 List
        Mockito.when(productService.findAll(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(new PageImpl<>(Arrays.asList(p1)));

        // 3. 发起请求并验证
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                // 注意：分页后的 JSON 数据在 content 字段里，所以是 $.content[0].name
                .andExpect(jsonPath("$.content[0].name").value("测试商品A"));
    }

    @Test
    void testCreateProduct() throws Exception {
        Product p = new Product();
        p.setName("新商品");
        p.setPrice(BigDecimal.valueOf(99.9));

        Mockito.when(productService.save(Mockito.any(Product.class))).thenReturn(p);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(p)))
                .andExpect(status().isOk());
    }
}