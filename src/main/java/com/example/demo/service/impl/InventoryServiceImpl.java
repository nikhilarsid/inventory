package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import com.example.demo.dto.request.InventoryRequestDto;
import com.example.demo.dto.response.InventoryResponseDto;
import com.example.demo.entity.InventoryItem;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public InventoryResponseDto createInventoryItem(InventoryRequestDto requestDto) {
        if (inventoryRepository.existsByMerchantIdAndProductId(requestDto.getMerchantId(), requestDto.getProductId())) {
            throw new RuntimeException("Item already exists for this merchant. Use update instead.");
        }

        InventoryItem entity = new InventoryItem();
        BeanUtils.copyProperties(requestDto, entity);

        InventoryItem savedEntity = inventoryRepository.save(entity);
        return mapToResponse(savedEntity);
    }

    // ✅ CHANGED: Accepts String productId instead of Long id
    @Override
    public InventoryResponseDto getInventoryItem(String productId) {
        // ✅ CHANGED: Uses findByProductId instead of findById
        InventoryItem entity = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with Product ID: " + productId));
        return mapToResponse(entity);
    }

    @Override
    public List<InventoryResponseDto> getInventoryByMerchant(Long merchantId) {
        List<InventoryItem> items = inventoryRepository.findByMerchantId(merchantId);
        return items.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteInventoryItem(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Item not found with id: " + id);
        }
        inventoryRepository.deleteById(id);
        return true;
    }

    private InventoryResponseDto mapToResponse(InventoryItem entity) {
        InventoryResponseDto dto = new InventoryResponseDto();
        BeanUtils.copyProperties(entity, dto);
        dto.setTotalValue(Math.round(entity.getPrice() * entity.getQuantity() * 100.0) / 100.0);
        return dto;
    }
}