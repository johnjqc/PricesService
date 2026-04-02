package com.capitole.prices.adapter.rest;

import com.capitole.prices.adapter.rest.dto.ApiResponseDto;
import com.capitole.prices.adapter.rest.dto.NotificationResponse;
import com.capitole.prices.adapter.rest.dto.PriceResponse;
import com.capitole.prices.application.port.in.GetProductPrice;
import com.capitole.prices.domain.model.Price;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@Tag(name = "Prices", description = "Product price management operations")
@Validated
public class PriceController {

    private final GetProductPrice getProductPrice;

    public PriceController(GetProductPrice getProductPrice) {
        this.getProductPrice = getProductPrice;
    }

    @Operation(
            summary = "Get applicable product price",
            description = "Retrieves the applicable price for a product based on brand and application date"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Price found successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - invalid parameters"
            )
    })
    @GetMapping("/price")
    public ResponseEntity<ApiResponseDto<PriceResponse>> getPrice(
            @Parameter(description = "Product identifier", required = true)
            @NotNull(message = "productId is required")
            @RequestParam(name = "productId") Long productId,
            @Parameter(description = "Brand identifier", required = true)
            @NotNull(message = "brandId is required")
            @RequestParam(name = "brandId") Long brandId,
            @Parameter(description = "Application date (ISO 8601 format)", required = true)
            @NotNull(message = "applicationDate is required")
            @RequestParam(name = "applicationDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime applicationDate) {

        Price price = getProductPrice.getPrice(brandId, productId, applicationDate);

        PriceResponse response = new PriceResponse(
                price.productId(),
                price.brandId(),
                price.priceList(),
                price.startDate(),
                price.endDate(),
                price.price());

        NotificationResponse notificationResponse = new NotificationResponse("SUCCESS",
                LocalDateTime.now(),
                "SCS-00");
        ApiResponseDto<PriceResponse> apiResponse = new ApiResponseDto<>(response, notificationResponse);

        return ResponseEntity.ok(apiResponse);
    }
}
