package com.bazarya.repository;

import com.bazarya.domain.entities.City;
import com.bazarya.domain.entities.ZoneCity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ZoneCityRepository extends JpaRepository<ZoneCity, Long> {
    Optional<ZoneCity> findByCity(City city);
}
