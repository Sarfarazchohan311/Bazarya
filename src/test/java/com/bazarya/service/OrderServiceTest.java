package com.bazarya.service;

import com.bazarya.domain.entities.*;
import com.bazarya.repository.*;
import com.bazarya.service.dto.CartItemRequest;
import com.bazarya.service.dto.CheckoutRequest;
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
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

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

    @BeforeEach
    void setup() {
        Role role = new Role();
        role.setName(RoleName.ROLE_CUSTOMER);
        roleRepository.save(role);

        User user = new User();
        user.setEmail("customer@example.com");
        user.setPassword("encoded");
        user.setFullName("Customer");
        user.getRoles().add(role);
        userRepository.save(user);

        Category category = new Category();
        category.setName("Gadgets");
        category.setSlug("gadgets");
        categoryRepository.save(category);

        Product product = new Product();
        product.setCategory(category);
        product.setName("Headphones");
        product.setSlug("headphones");
        productRepository.save(product);

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setSku("SKU-ORDER");
        variant.setPrice(BigDecimal.valueOf(1500));
        variant.setWeightKg(BigDecimal.valueOf(0.3));
        variantRepository.save(variant);

        Inventory inventory = new Inventory();
        inventory.setVariant(variant);
        inventory.setOnHand(5);
        inventory.setReserved(0);
        inventoryRepository.save(inventory);

        Country pakistan = new Country();
        pakistan.setName("Pakistan");
        pakistan.setIsoCode("PK");
        countryRepository.save(pakistan);

        State state = new State();
        state.setCountry(pakistan);
        state.setName("Sindh");
        state.setCode("SD");
        stateRepository.save(state);

        City city = new City();
        city.setState(state);
        city.setName("Karachi");
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
        rate.setRate(BigDecimal.valueOf(150));
        shippingRateRepository.save(rate);
    }

    @Test
    void createsOrderAndReservesInventory() {
        CheckoutRequest request = new CheckoutRequest();
        request.setOrderNo("ORD-1001");
        request.setEmail("customer@example.com");
        request.setShippingState("Sindh");
        request.setShippingCity("Karachi");
        request.setShippingAddress("Street 1");
        request.setPhone("03001234567");

        CartItemRequest item = new CartItemRequest();
        item.setSku("SKU-ORDER");
        item.setQuantity(2);
        request.setItems(List.of(item));

        Order order = orderService.createOrder(request);

        assertThat(order.getGrandTotal()).isGreaterThan(BigDecimal.ZERO);
        Inventory inventory = inventoryRepository.findAll().get(0);
        assertThat(inventory.getReserved()).isEqualTo(2);
    }
}
