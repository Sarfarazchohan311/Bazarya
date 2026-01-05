package com.bazarya.domain.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "zone_cities")
public class ZoneCity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private ShippingZone zone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
