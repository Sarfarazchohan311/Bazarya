package com.bazarya.domain.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "shipping_rates")
public class ShippingRate extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private ShippingZone zone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShippingRuleType ruleType;

    @Column(nullable = false)
    private BigDecimal rate;

    private BigDecimal minValue;

    private BigDecimal maxValue;

    private BigDecimal freeShippingThreshold;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShippingZone getZone() {
        return zone;
    }

    public void setZone(ShippingZone zone) {
        this.zone = zone;
    }

    public ShippingRuleType getRuleType() {
        return ruleType;
    }

    public void setRuleType(ShippingRuleType ruleType) {
        this.ruleType = ruleType;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public BigDecimal getFreeShippingThreshold() {
        return freeShippingThreshold;
    }

    public void setFreeShippingThreshold(BigDecimal freeShippingThreshold) {
        this.freeShippingThreshold = freeShippingThreshold;
    }
}
