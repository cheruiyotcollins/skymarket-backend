package com.gigster.skymarket.dto;

public record AddProductRequestDto(String productName, String category, String manufacturer, double price, int stock) {}
