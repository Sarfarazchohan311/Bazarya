package com.bazarya.service.pricing;

import com.bazarya.domain.entities.Coupon;
import com.bazarya.domain.entities.CouponType;
import com.bazarya.domain.entities.ProductVariant;
import com.bazarya.repository.CouponRepository;
import com.bazarya.repository.CouponUsageRepository;
import com.bazarya.repository.ProductVariantRepository;
import com.bazarya.service.dto.CartItemRequest;
import com.bazarya.service.shipping.ShippingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class PricingService {
    private final ProductVariantRepository variantRepository;
    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final ShippingService shippingService;

    public PricingService(ProductVariantRepository variantRepository,
                          CouponRepository couponRepository,
                          CouponUsageRepository couponUsageRepository,
                          ShippingService shippingService) {
        this.variantRepository = variantRepository;
        this.couponRepository = couponRepository;
        this.couponUsageRepository = couponUsageRepository;
        this.shippingService = shippingService;
    }

    public PricingResult calculate(List<CartItemRequest> items, String couponCode, String city) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;
        for (CartItemRequest item : items) {
            ProductVariant variant = variantRepository.findBySku(item.getSku())
                    .orElseThrow(() -> new IllegalArgumentException("SKU not found"));
            BigDecimal lineTotal = variant.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(lineTotal);
            totalWeight = totalWeight.add(variant.getWeightKg().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        BigDecimal discount = applyCoupon(subtotal, couponCode);
        BigDecimal shippingFee = shippingService.calculateShipping(city, subtotal.subtract(discount), totalWeight);
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal codFee = BigDecimal.ZERO;
        BigDecimal grandTotal = subtotal.subtract(discount).add(shippingFee).add(codFee).add(tax);
        return new PricingResult(subtotal, discount, shippingFee, codFee, tax, grandTotal);
    }

    private BigDecimal applyCoupon(BigDecimal subtotal, String couponCode) {
        if (couponCode == null || couponCode.isBlank()) {
            return BigDecimal.ZERO;
        }
        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
        if (!coupon.isActive()) {
            return BigDecimal.ZERO;
        }
        Instant now = Instant.now();
        if (coupon.getStartsAt() != null && now.isBefore(coupon.getStartsAt())) {
            return BigDecimal.ZERO;
        }
        if (coupon.getEndsAt() != null && now.isAfter(coupon.getEndsAt())) {
            return BigDecimal.ZERO;
        }
        if (coupon.getMinOrderAmount() != null && subtotal.compareTo(coupon.getMinOrderAmount()) < 0) {
            return BigDecimal.ZERO;
        }
        if (coupon.getUsageLimit() != null && couponUsageRepository.countByCoupon(coupon) >= coupon.getUsageLimit()) {
            return BigDecimal.ZERO;
        }
        BigDecimal discount = BigDecimal.ZERO;
        if (coupon.getType() == CouponType.PERCENT) {
            discount = subtotal.multiply(coupon.getValue()).divide(BigDecimal.valueOf(100));
        } else if (coupon.getType() == CouponType.FIXED) {
            discount = coupon.getValue();
        } else if (coupon.getType() == CouponType.FREESHIP) {
            discount = BigDecimal.ZERO;
        }
        if (coupon.getMaxDiscountAmount() != null && discount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
            return coupon.getMaxDiscountAmount();
        }
        return discount;
    }
}
