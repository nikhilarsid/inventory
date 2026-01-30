package com.example.demo.repository;

import com.example.demo.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

    // ✅ ADDED: Find by String Product ID
    Optional<InventoryItem> findByProductId(String productId);

    Optional<InventoryItem> findByMerchantIdAndProductId(Long merchantId, String productId);
    boolean existsByMerchantIdAndProductId(Long merchantId, String productId);

    List<InventoryItem> findByMerchantId(Long merchantId);
}