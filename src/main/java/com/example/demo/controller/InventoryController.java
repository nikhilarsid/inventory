package com.example.demo.controller;

import com.example.demo.dto.request.InventoryRequestDto;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.InventoryResponseDto;
import com.example.demo.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/inventory") // Proper Versioning
@RequiredArgsConstructor // Constructor Injection
public class InventoryController {

    // Using Interface, not Implementation
    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryResponseDto>> addInventory(@Valid @RequestBody InventoryRequestDto request) {
        InventoryResponseDto responseDto = inventoryService.createInventoryItem(request);

        ApiResponse<InventoryResponseDto> response = ApiResponse.success(responseDto, "Inventory item created successfully");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponseDto> getInventory(@PathVariable Long id) {
        InventoryResponseDto response = inventoryService.getInventoryItem(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteInventory(@PathVariable Long id) {
        boolean isDeleted = inventoryService.deleteInventoryItem(id);

        Map<String, Object> response = java.util.Map.of(
                "success", isDeleted,
                "message", "Item deleted successfully"
        );
        return ResponseEntity.ok(response);
    }
}