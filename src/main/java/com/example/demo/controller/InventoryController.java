package com.example.demo.controller;

import com.example.demo.dto.request.InventoryRequestDto;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.InventoryResponseDto;
import com.example.demo.security.User;
import com.example.demo.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryResponseDto>> addInventory(@Valid @RequestBody InventoryRequestDto request) {
        InventoryResponseDto responseDto = inventoryService.createInventoryItem(request);
        ApiResponse<InventoryResponseDto> response = ApiResponse.success(responseDto, "Inventory item created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ✅ CHANGED: PathVariable is now String productId
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponseDto> getInventory(@PathVariable String productId) {
        InventoryResponseDto response = inventoryService.getInventoryItem(productId);
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

    @GetMapping("/my-listings")
    public ResponseEntity<ApiResponse<List<InventoryResponseDto>>> getMyListings() {
        System.out.println("📍 [Controller] Entered getMyListings endpoint");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof User)) {
            System.out.println("❌ [Controller] Principal is NOT a User object!");
            throw new RuntimeException("Principal is not of type User");
        }

        User user = (User) principal;
        System.out.println("🆔 [Controller] User ID: " + user.getId());

        List<InventoryResponseDto> listings = inventoryService.getInventoryByMerchant(user.getId());

        return ResponseEntity.ok(ApiResponse.success(listings, "Merchant listings fetched successfully"));
    }
}