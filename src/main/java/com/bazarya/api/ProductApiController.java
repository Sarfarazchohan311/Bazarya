package com.bazarya.api;

import com.bazarya.domain.entities.Product;
import com.bazarya.repository.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductApiController {
    private final ProductRepository productRepository;

    public ProductApiController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> list() {
        return productRepository.findAll();
    }

    @GetMapping("/{slug}")
    public Product get(@PathVariable String slug) {
        return productRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }
}
