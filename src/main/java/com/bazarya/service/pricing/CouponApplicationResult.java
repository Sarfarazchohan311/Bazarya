package com.bazarya.service.pricing;

import java.math.BigDecimal;

public class CouponApplicationResult {
    private final BigDecimal discount;
    private final boolean freeShipping;

    public CouponApplicationResult(BigDecimal discount, boolean freeShipping) {
        this.discount = discount;
        this.freeShipping = freeShipping;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public boolean isFreeShipping() {
        return freeShipping;
    }
}
