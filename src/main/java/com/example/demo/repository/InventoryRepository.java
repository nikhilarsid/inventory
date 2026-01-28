package com.example.demo.repository;

import com.example.demo.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByMerchantIdAndProductId(Long merchantId, String productId);
    boolean existsByMerchantIdAndProductId(Long merchantId, String productId);
}