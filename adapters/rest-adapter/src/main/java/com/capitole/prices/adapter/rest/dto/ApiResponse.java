package com.capitole.prices.adapter.rest.dto;

public record ApiResponse<T>(T data, Object notification) {}
