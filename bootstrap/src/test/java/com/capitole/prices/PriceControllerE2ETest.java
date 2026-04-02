package com.capitole.prices;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test1_price_at_10_on_14() throws Exception {

        mockMvc.perform(get("/api/price")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.priceList").value(1))
                .andExpect(jsonPath("$.data.price").value(35.50));
    }

    @Test
    void test2_price_at_16_on_14() throws Exception {

        mockMvc.perform(get("/api/price")
                        .param("applicationDate", "2020-06-14T16:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.priceList").value(2))
                .andExpect(jsonPath("$.data.price").value(25.45));
    }

    @Test
    void test3_price_at_21_on_14() throws Exception {

        mockMvc.perform(get("/api/price")
                        .param("applicationDate", "2020-06-14T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.priceList").value(1))
                .andExpect(jsonPath("$.data.price").value(35.50));
    }

    @Test
    void test4_price_at_10_on_15() throws Exception {

        mockMvc.perform(get("/api/price")
                        .param("applicationDate", "2020-06-15T10:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.priceList").value(3))
                .andExpect(jsonPath("$.data.price").value(30.50));
    }

    @Test
    void test5_price_at_21_on_16() throws Exception {

        mockMvc.perform(get("/api/price")
                        .param("applicationDate", "2020-06-16T21:00:00")
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.priceList").value(4))
                .andExpect(jsonPath("$.data.price").value(38.95));
    }
}