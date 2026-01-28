package com.example.demo.dto.response;

import lombok.Data;

@Data
public class InventoryResponseDto {
    private Long id;
    private Long merchantId;
    private String productId;
    private Integer quantity;
    private Double price;
    private Double totalValue;
}