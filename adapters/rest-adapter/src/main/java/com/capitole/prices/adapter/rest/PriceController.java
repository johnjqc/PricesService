package com.capitole.prices.adapter.rest;

import com.capitole.prices.adapter.rest.dto.ApiResponse;
import com.capitole.prices.adapter.rest.dto.NotificationResponse;
import com.capitole.prices.adapter.rest.dto.PriceResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class PriceController {

    @GetMapping("/price")
    public ResponseEntity<ApiResponse<PriceResponse>> getPrice(
            @RequestParam(name = "productId") Long productId,
            @RequestParam(name = "brandId") Long brandId,
            @RequestParam(name = "applicationDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime applicationDate) {

        PriceResponse response = new PriceResponse(0L, 0L,0L, LocalDateTime.now(), LocalDateTime.now(), BigDecimal.ZERO);
        NotificationResponse notificationResponse = new NotificationResponse("SUCCESS",
                LocalDateTime.now(),
                "SCS-00");
        ApiResponse<PriceResponse> apiResponse = new ApiResponse<>(response, notificationResponse);

        return ResponseEntity.ok(apiResponse);
    }
}
