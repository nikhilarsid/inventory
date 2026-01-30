package com.example.demo.service;

import com.example.demo.dto.request.InventoryRequestDto;
import com.example.demo.dto.response.InventoryResponseDto;
import java.util.List;

public interface InventoryService {

    /**
     * Creates a new inventory record.
     * @param requestDto The data to create.
     * @return The created record.
     */
    InventoryResponseDto createInventoryItem(InventoryRequestDto requestDto);

    /**
     * Retrieves a specific item by ID.
     * @param id The ID of the inventory item.
     * @return The mapped DTO.
     */
    InventoryResponseDto getInventoryItem(Long id);

    List<InventoryResponseDto> getInventoryByMerchant(Long merchantId);

    /**
     * Soft delete or hard delete based on requirement.
     * @param id The ID to delete.
     * @return True if successful.
     */
    boolean deleteInventoryItem(Long id);
}