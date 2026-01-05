package com.bazarya.service.pricing;

import java.math.BigDecimal;

public class PricingResult {
    private BigDecimal subtotal;
    private BigDecimal discountTotal;
    private BigDecimal shippingFee;
    private BigDecimal codFee;
    private BigDecimal tax;
    private BigDecimal grandTotal;

    public PricingResult(BigDecimal subtotal, BigDecimal discountTotal, BigDecimal shippingFee, BigDecimal codFee, BigDecimal tax, BigDecimal grandTotal) {
        this.subtotal = subtotal;
        this.discountTotal = discountTotal;
        this.shippingFee = shippingFee;
        this.codFee = codFee;
        this.tax = tax;
        this.grandTotal = grandTotal;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public BigDecimal getCodFee() {
        return codFee;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }
}
