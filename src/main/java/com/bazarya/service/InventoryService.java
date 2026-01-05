package com.bazarya.service;

import com.bazarya.domain.entities.Inventory;
import com.bazarya.domain.entities.ProductVariant;
import com.bazarya.repository.InventoryRepository;
import com.bazarya.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductVariantRepository variantRepository;

    public InventoryService(InventoryRepository inventoryRepository, ProductVariantRepository variantRepository) {
        this.inventoryRepository = inventoryRepository;
        this.variantRepository = variantRepository;
    }

    @Transactional
    public void reserveStock(String sku, int quantity) {
        ProductVariant variant = variantRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("SKU not found"));
        Inventory inventory = inventoryRepository.findByVariant(variant)
                .orElseThrow(() -> new IllegalArgumentException("Inventory missing"));
        if (inventory.getAvailable() < quantity) {
            throw new IllegalStateException("Insufficient stock");
        }
        inventory.setReserved(inventory.getReserved() + quantity);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void releaseStock(String sku, int quantity) {
        ProductVariant variant = variantRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("SKU not found"));
        Inventory inventory = inventoryRepository.findByVariant(variant)
                .orElseThrow(() -> new IllegalArgumentException("Inventory missing"));
        inventory.setReserved(Math.max(0, inventory.getReserved() - quantity));
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void deductStock(String sku, int quantity) {
        ProductVariant variant = variantRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("SKU not found"));
        Inventory inventory = inventoryRepository.findByVariant(variant)
                .orElseThrow(() -> new IllegalArgumentException("Inventory missing"));
        inventory.setReserved(Math.max(0, inventory.getReserved() - quantity));
        inventory.setOnHand(Math.max(0, inventory.getOnHand() - quantity));
        inventoryRepository.save(inventory);
    }
}
