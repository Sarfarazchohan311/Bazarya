package com.bazarya.service;

import com.bazarya.domain.entities.*;
import com.bazarya.repository.InventoryRepository;
import com.bazarya.repository.ProductRepository;
import com.bazarya.repository.ProductVariantRepository;
import com.bazarya.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class InventoryServiceTest {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        Category category = new Category();
        category.setName("Fashion");
        category.setSlug("fashion");
        categoryRepository.save(category);

        Product product = new Product();
        product.setCategory(category);
        product.setName("Shirt");
        product.setSlug("shirt");
        productRepository.save(product);

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setSku("SKU-INV");
        variant.setPrice(java.math.BigDecimal.valueOf(1000));
        variant.setWeightKg(java.math.BigDecimal.valueOf(0.2));
        variantRepository.save(variant);

        Inventory inventory = new Inventory();
        inventory.setVariant(variant);
        inventory.setOnHand(10);
        inventory.setReserved(0);
        inventoryRepository.save(inventory);
    }

    @Test
    void reserveAndDeductStock() {
        inventoryService.reserveStock("SKU-INV", 3);
        Inventory inventory = inventoryRepository.findAll().get(0);
        assertThat(inventory.getReserved()).isEqualTo(3);

        inventoryService.deductStock("SKU-INV", 2);
        inventory = inventoryRepository.findAll().get(0);
        assertThat(inventory.getOnHand()).isEqualTo(8);
        assertThat(inventory.getReserved()).isEqualTo(1);
    }
}
