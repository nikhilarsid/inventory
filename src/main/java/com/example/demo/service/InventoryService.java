package com.example.demo.service;

import com.example.demo.dto.request.InventoryRequestDto;
import com.example.demo.dto.response.InventoryResponseDto;
import java.util.List;

public interface InventoryService {

    InventoryResponseDto createInventoryItem(InventoryRequestDto requestDto);

    // ✅ CHANGED: Long id -> String productId
    InventoryResponseDto getInventoryItem(String productId);

    List<InventoryResponseDto> getInventoryByMerchant(Long merchantId);

    boolean deleteInventoryItem(Long id);
}