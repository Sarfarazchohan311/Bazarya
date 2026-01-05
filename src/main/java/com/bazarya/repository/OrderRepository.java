package com.bazarya.repository;

import com.bazarya.domain.entities.Order;
import com.bazarya.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNo(String orderNo);
    long countByUser(User user);
}
