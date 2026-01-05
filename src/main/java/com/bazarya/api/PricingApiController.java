package com.bazarya.api;

import com.bazarya.service.dto.CartItemRequest;
import com.bazarya.service.pricing.PricingResult;
import com.bazarya.service.pricing.PricingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pricing")
public class PricingApiController {
    private final PricingService pricingService;

    public PricingApiController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @PostMapping
    public PricingResult calculate(@RequestBody @Valid Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
        String coupon = (String) body.get("couponCode");
        String city = (String) body.get("city");
        List<CartItemRequest> requests = items.stream().map(item -> {
            CartItemRequest request = new CartItemRequest();
            request.setSku((String) item.get("sku"));
            request.setQuantity(((Number) item.get("quantity")).intValue());
            return request;
        }).toList();
        return pricingService.calculate(requests, coupon, city);
    }
}
