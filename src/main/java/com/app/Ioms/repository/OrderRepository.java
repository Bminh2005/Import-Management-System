package com.app.Ioms.repository;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<Order> findAll();
    List<Order> findByStatus(OrderStatus status);
    List<Order> findCanceledBySite(String siteCode);
    Optional<Order> findById(String id);
    void save(Order order);
    void update(Order order);
}
