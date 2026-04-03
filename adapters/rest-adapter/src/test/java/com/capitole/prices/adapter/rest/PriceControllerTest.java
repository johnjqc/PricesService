package com.capitole.prices.adapter.rest;

import com.capitole.prices.adapter.rest.advice.PriceExceptionHandler;
import com.capitole.prices.application.port.in.GetProductPrice;
import com.capitole.prices.application.dto.ApplicationError;
import com.capitole.prices.application.dto.PriceResult;
import com.capitole.prices.domain.exception.ErrorCode;
import com.capitole.prices.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
public class PriceControllerTest {

    @SpringBootConfiguration
    @Import({PriceController.class, PriceExceptionHandler.class})
    static class TestConfig {}

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetProductPrice getProductPrice;


    @ParameterizedTest
    @CsvSource({
            "2020-06-14T10:00:00,1,35.50",
            "2020-06-14T16:00:00,2,25.45",
            "2020-06-14T21:00:00,1,35.50",
            "2020-06-15T10:00:00,3,30.50",
            "2020-06-16T21:00:00,4,38.95"
    })
    void givenApplicationDate_whenGetPrice_thenReturnCorrectPrice(
            String date,
            int expectedPriceList,
            BigDecimal expectedPrice) throws Exception {

        Price price = new Price(1L,
                35455L,
                expectedPriceList,
                0,
                LocalDateTime.parse("2020-06-14T15:00:00"),
                LocalDateTime.parse("2020-06-14T18:30:00"),
                expectedPrice,
                "EUR");

        when(getProductPrice.findApplicablePrice(any(), any(), any()))
                .thenReturn(PriceResult.ok(price));

        mockMvc.perform(get("/api/price")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.priceList").value(expectedPriceList))
                .andExpect(jsonPath("$.data.price").value(expectedPrice.doubleValue()));
    }

    @Test
    void givenNoPrice_whenGetPrice_thenReturn404() throws Exception {

        when(getProductPrice.findApplicablePrice(any(), any(), any()))
                .thenReturn(
                        PriceResult.fail(
                                new ApplicationError(
                                        ErrorCode.PRICE_NOT_FOUND,
                                        "Price not found"
                                )
                        )
                );

        mockMvc.perform(get("/api/price")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenMissingAllParameters_whenGetPrice_thenReturn400() throws Exception {

        mockMvc.perform(get("/api/price"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNullProductId_whenGetPrice_thenReturn400WithValidationMessage() throws Exception {

        mockMvc.perform(get("/api/price")
                        .param("productId", "")
                        .param("brandId", "1")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.notification.code").value(ErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.notification.description").exists());
    }

    @Test
    void givenNullBrandId_whenGetPrice_thenReturn400() throws Exception {

        mockMvc.perform(get("/api/price")
                        .param("productId", "35455")
                        .param("brandId", "")
                        .param("applicationDate", "2020-06-14T10:00:00"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNullApplicationDate_whenGetPrice_thenReturn400() throws Exception {

        mockMvc.perform(get("/api/price")
                        .param("productId", "35455")
                        .param("brandId", "1")
                        .param("applicationDate", ""))
                .andExpect(status().isBadRequest());
    }

}