package com.bazarya.api;

import com.bazarya.domain.entities.Order;
import com.bazarya.service.OrderService;
import com.bazarya.service.dto.CheckoutRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderApiController {
    private final OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order place(@RequestBody @Valid CheckoutRequest request) {
        return orderService.createOrder(request);
    }
}
