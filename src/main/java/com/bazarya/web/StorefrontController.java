package com.bazarya.web;

import com.bazarya.domain.entities.Category;
import com.bazarya.domain.entities.Product;
import com.bazarya.repository.CategoryRepository;
import com.bazarya.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StorefrontController {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public StorefrontController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        return "home";
    }

    @GetMapping("/c/{slug}")
    public String category(@PathVariable String slug, Model model) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        model.addAttribute("category", category);
        model.addAttribute("products", productRepository.findAll());
        return "category";
    }

    @GetMapping("/p/{slug}")
    public String product(@PathVariable String slug, Model model) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        model.addAttribute("product", product);
        return "product";
    }
}
