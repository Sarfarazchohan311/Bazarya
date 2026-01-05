package com.bazarya.service;

import com.bazarya.domain.entities.*;
import com.bazarya.repository.*;
import com.bazarya.service.dto.CartItemRequest;
import com.bazarya.service.pricing.PricingResult;
import com.bazarya.service.pricing.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PricingServiceTest {
    @Autowired
    private PricingService pricingService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ShippingZoneRepository shippingZoneRepository;

    @Autowired
    private ZoneCityRepository zoneCityRepository;

    @Autowired
    private ShippingRateRepository shippingRateRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        Country pakistan = new Country();
        pakistan.setName("Pakistan");
        pakistan.setIsoCode("PK");
        countryRepository.save(pakistan);

        State state = new State();
        state.setCountry(pakistan);
        state.setName("Punjab");
        state.setCode("PB");
        stateRepository.save(state);

        City city = new City();
        city.setState(state);
        city.setName("Lahore");
        cityRepository.save(city);

        ShippingZone zone = new ShippingZone();
        zone.setName("Major Cities");
        shippingZoneRepository.save(zone);

        ZoneCity zoneCity = new ZoneCity();
        zoneCity.setZone(zone);
        zoneCity.setCity(city);
        zoneCityRepository.save(zoneCity);

        ShippingRate rate = new ShippingRate();
        rate.setZone(zone);
        rate.setRuleType(ShippingRuleType.FLAT);
        rate.setRate(BigDecimal.valueOf(200));
        rate.setFreeShippingThreshold(BigDecimal.valueOf(5000));
        shippingRateRepository.save(rate);

        Category category = new Category();
        category.setName("Electronics");
        category.setSlug("electronics");
        categoryRepository.save(category);

        Product product = new Product();
        product.setCategory(category);
        product.setName("Phone");
        product.setSlug("phone");
        productRepository.save(product);

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setSku("SKU-1");
        variant.setPrice(BigDecimal.valueOf(2500));
        variant.setWeightKg(BigDecimal.valueOf(0.5));
        variantRepository.save(variant);

        Coupon coupon = new Coupon();
        coupon.setCode("SAVE10");
        coupon.setType(CouponType.PERCENT);
        coupon.setValue(BigDecimal.valueOf(10));
        coupon.setActive(true);
        couponRepository.save(coupon);
    }

    @Test
    void calculatesTotalsWithCoupon() {
        CartItemRequest item = new CartItemRequest();
        item.setSku("SKU-1");
        item.setQuantity(2);

        PricingResult result = pricingService.calculate(List.of(item), "SAVE10", "Lahore");

        assertThat(result.getSubtotal()).isEqualByComparingTo(BigDecimal.valueOf(5000));
        assertThat(result.getDiscountTotal()).isEqualByComparingTo(BigDecimal.valueOf(500));
        assertThat(result.getShippingFee()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getGrandTotal()).isEqualByComparingTo(BigDecimal.valueOf(4500));
    }
}
