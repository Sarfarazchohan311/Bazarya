package com.bazarya.repository;

import com.bazarya.domain.entities.CouponUsage;
import com.bazarya.domain.entities.Coupon;
import com.bazarya.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {
    long countByCoupon(Coupon coupon);
    long countByCouponAndUser(Coupon coupon, User user);
}
