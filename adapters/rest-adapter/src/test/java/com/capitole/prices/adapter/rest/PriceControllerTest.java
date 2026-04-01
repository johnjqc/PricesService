package com.capitole.prices.adapter.rest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
public class PriceControllerTest {

    @SpringBootConfiguration
    @Import(PriceController.class)
    static class TestConfig {}

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    /*@CsvSource({
            "2020-06-14T10:00:00,1,35.50",
            "2020-06-14T16:00:00,2,25.45",
            "2020-06-14T21:00:00,1,35.50",
            "2020-06-15T10:00:00,3,30.50",
            "2020-06-16T21:00:00,4,38.95"
    })*/
    @CsvSource({
            "2020-06-14T10:00:00,0,0",
            "2020-06-14T16:00:00,0,0",
            "2020-06-14T21:00:00,0,0",
            "2020-06-15T10:00:00,0,0",
            "2020-06-16T21:00:00,0,0"
    })
    void givenApplicationDate_whenGetPrice_thenReturnCorrectPrice(
            String date,
            int expectedPriceList,
            double expectedPrice) throws Exception {

        mockMvc.perform(get("/api/price")
                        .param("productId", "35455")
                        .param("brandId", "0")
                        .param("applicationDate", date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.priceList").value(expectedPriceList))
                .andExpect(jsonPath("$.data.price").value(expectedPrice));
    }

}