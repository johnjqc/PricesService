package com.capitole.prices.adapter.rest.dto;

public record ApiResponseDto<T>(T data, Object notification) {}
