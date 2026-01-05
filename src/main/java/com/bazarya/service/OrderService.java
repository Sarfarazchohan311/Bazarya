package com.bazarya.service;

import com.bazarya.domain.entities.*;
import com.bazarya.repository.*;
import com.bazarya.service.dto.CartItemRequest;
import com.bazarya.service.dto.CheckoutRequest;
import com.bazarya.service.pricing.PricingResult;
import com.bazarya.service.pricing.PricingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository variantRepository;
    private final InventoryService inventoryService;
    private final PricingService pricingService;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        OrderStatusHistoryRepository historyRepository,
                        UserRepository userRepository,
                        ProductVariantRepository variantRepository,
                        InventoryService inventoryService,
                        PricingService pricingService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
        this.variantRepository = variantRepository;
        this.inventoryService = inventoryService;
        this.pricingService = pricingService;
    }

    @Transactional
    public Order createOrder(CheckoutRequest request) {
        orderRepository.findByOrderNo(request.getOrderNo()).ifPresent(existing -> {
            throw new IllegalStateException("Order already exists");
        });
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PricingResult pricingResult = pricingService.calculate(request.getItems(), request.getCouponCode(), request.getShippingCity());

        Order order = new Order();
        order.setOrderNo(request.getOrderNo());
        order.setUser(user);
        order.setStatus(OrderStatus.NEW);
        order.setSubtotal(pricingResult.getSubtotal());
        order.setDiscountTotal(pricingResult.getDiscountTotal());
        order.setShippingFee(pricingResult.getShippingFee());
        order.setCodFee(pricingResult.getCodFee());
        order.setTax(pricingResult.getTax());
        order.setGrandTotal(pricingResult.getGrandTotal());
        order.setShippingState(request.getShippingState());
        order.setShippingCity(request.getShippingCity());
        order.setShippingAddress(request.getShippingAddress());
        order.setPhone(request.getPhone());
        orderRepository.save(order);

        for (CartItemRequest item : request.getItems()) {
            ProductVariant variant = variantRepository.findBySku(item.getSku())
                    .orElseThrow(() -> new IllegalArgumentException("SKU not found"));
            inventoryService.reserveStock(item.getSku(), item.getQuantity());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setSku(item.getSku());
            orderItem.setProductName(variant.getProduct().getName());
            orderItem.setUnitPrice(variant.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setLineTotal(variant.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            orderItemRepository.save(orderItem);
        }

        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setStatus(OrderStatus.NEW);
        history.setNotes("Order created");
        historyRepository.save(history);

        return order;
    }
}
