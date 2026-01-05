package com.bazarya.service.shipping;

import com.bazarya.domain.entities.City;
import com.bazarya.domain.entities.ShippingRate;
import com.bazarya.domain.entities.ShippingRuleType;
import com.bazarya.domain.entities.ShippingZone;
import com.bazarya.domain.entities.ZoneCity;
import com.bazarya.repository.CityRepository;
import com.bazarya.repository.ShippingRateRepository;
import com.bazarya.repository.ZoneCityRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class ShippingService {
    private final CityRepository cityRepository;
    private final ZoneCityRepository zoneCityRepository;
    private final ShippingRateRepository shippingRateRepository;

    public ShippingService(CityRepository cityRepository,
                           ZoneCityRepository zoneCityRepository,
                           ShippingRateRepository shippingRateRepository) {
        this.cityRepository = cityRepository;
        this.zoneCityRepository = zoneCityRepository;
        this.shippingRateRepository = shippingRateRepository;
    }

    public BigDecimal calculateShipping(String cityName, BigDecimal orderAmount, BigDecimal totalWeight) {
        City city = cityRepository.findByName(cityName)
                .orElseThrow(() -> new IllegalArgumentException("City not found"));
        ZoneCity zoneCity = zoneCityRepository.findByCity(city)
                .orElseThrow(() -> new IllegalArgumentException("City not mapped to zone"));
        ShippingZone zone = zoneCity.getZone();
        List<ShippingRate> rates = shippingRateRepository.findByZone(zone);
        if (rates.isEmpty()) {
            return BigDecimal.ZERO;
        }
        ShippingRate rate = selectRate(rates, orderAmount, totalWeight);
        if (rate.getFreeShippingThreshold() != null && orderAmount.compareTo(rate.getFreeShippingThreshold()) >= 0) {
            return BigDecimal.ZERO;
        }
        return rate.getRate();
    }

    private ShippingRate selectRate(List<ShippingRate> rates, BigDecimal orderAmount, BigDecimal totalWeight) {
        return rates.stream()
                .sorted(Comparator.comparing(ShippingRate::getId))
                .filter(rate -> matches(rate, orderAmount, totalWeight))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No shipping rate available"));
    }

    private boolean matches(ShippingRate rate, BigDecimal orderAmount, BigDecimal totalWeight) {
        BigDecimal target;
        if (rate.getRuleType() == ShippingRuleType.BY_WEIGHT) {
            target = totalWeight;
        } else if (rate.getRuleType() == ShippingRuleType.BY_ORDER_AMOUNT) {
            target = orderAmount;
        } else {
            return true;
        }
        boolean minOk = rate.getMinValue() == null || target.compareTo(rate.getMinValue()) >= 0;
        boolean maxOk = rate.getMaxValue() == null || target.compareTo(rate.getMaxValue()) <= 0;
        return minOk && maxOk;
    }
}
