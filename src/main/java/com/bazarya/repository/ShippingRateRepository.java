package com.bazarya.repository;

import com.bazarya.domain.entities.ShippingRate;
import com.bazarya.domain.entities.ShippingZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShippingRateRepository extends JpaRepository<ShippingRate, Long> {
    List<ShippingRate> findByZone(ShippingZone zone);
}
