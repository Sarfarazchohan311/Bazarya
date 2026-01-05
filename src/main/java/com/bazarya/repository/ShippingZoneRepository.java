package com.bazarya.repository;

import com.bazarya.domain.entities.ShippingZone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingZoneRepository extends JpaRepository<ShippingZone, Long> {
}
