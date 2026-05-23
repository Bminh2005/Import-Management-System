package com.app.Ioms.service;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.OrderStatus;
import com.app.Ioms.repository.InMemoryOrderRepository;
import com.app.Ioms.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

public class OrderService {
    private static final OrderService INSTANCE = new OrderService();

    public static OrderService getInstance() {
        return INSTANCE;
    }

    private final OrderRepository repository = new InMemoryOrderRepository();

    private OrderService() { }

    public List<Order> listAll() {
        return repository.findAll();
    }

    public List<Order> listByStatus(OrderStatus status) {
        return repository.findByStatus(status);
    }

    public List<Order> listCanceledBySite(String siteCode) {
        return repository.findCanceledBySite(siteCode);
    }
package com.app.Ioms.service;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.OrderStatus;
import com.app.Ioms.repository.InMemoryOrderRepository;
import com.app.Ioms.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

public class OrderService {
    private static final OrderService INSTANCE = new OrderService();
    public static OrderService getInstance() { return INSTANCE; }

    private final OrderRepository repository = new InMemoryOrderRepository();

    private OrderService() {}

    public List<Order> listAll() { return repository.findAll(); }
    public List<Order> listByStatus(OrderStatus status) { return repository.findByStatus(status); }
    public List<Order> listCanceledBySite(String siteCode) { return repository.findCanceledBySite(siteCode); }
    public Optional<Order> getById(String id) { return repository.findById(id); }
    public void save(Order order) { repository.save(order); }
    public void update(Order order) { repository.update(order); }
    public void markProcessing(Order order) { order.setStatus(OrderStatus.PROCESSING); repository.update(order); }
    public void markReallocating(Order order) { order.setStatus(OrderStatus.REALLOCATING); repository.update(order); }
}
    public Optional<Order> getById(String id) {
        return repository.findById(id);
    }

    public void save(Order order) {
        repository.save(order);
    }

    public void update(Order order) {
        repository.update(order);
    }

    public void markProcessing(Order order) {
        order.setStatus(OrderStatus.PROCESSING);
        repository.update(order);
    }

    public void markReallocating(Order order) {
        order.setStatus(OrderStatus.REALLOCATING);
        repository.update(order);
    }
}
