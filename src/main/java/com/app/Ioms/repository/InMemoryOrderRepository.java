package com.app.Ioms.repository;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.OrderItem;
import com.app.Ioms.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class InMemoryOrderRepository implements OrderRepository {

    private static final List<Order> store = new CopyOnWriteArrayList<>();

    static {
        // Seed dữ liệu mẫu
        store.add(new Order(null, "ACME Corp", OrderStatus.NEW, null, LocalDateTime.now().minusDays(1),
                List.of(new OrderItem("SKU-1001", "Widget A", 10), new OrderItem("SKU-1002", "Widget B", 5))));
        store.add(new Order(null, "Globex", OrderStatus.CANCELED, "SGN", LocalDateTime.now().minusHours(10),
                List.of(new OrderItem("SKU-2001", "Gadget X", 2))));
        store.add(new Order(null, "Initech", OrderStatus.CANCELED, "NYC", LocalDateTime.now().minusHours(5),
                List.of(new OrderItem("SKU-1001", "Widget A", 1), new OrderItem("SKU-3003", "Device Z", 3))));
        store.add(new Order(null, "Hooli", OrderStatus.PROCESSING, null, LocalDateTime.now().minusHours(2),
                List.of(new OrderItem("SKU-4004", "Part Q", 7))));
    }

    @Override
    public List<Order> findAll() {
        // copy an toàn
        return new ArrayList<>(store);
    }
package com.app.Ioms.repository;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.OrderItem;
import com.app.Ioms.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class InMemoryOrderRepository implements OrderRepository {

    private static final List<Order> store = new CopyOnWriteArrayList<>();

    static {
        store.add(new Order(null, "ACME Corp", OrderStatus.NEW, null, LocalDateTime.now().minusDays(1),
                List.of(new OrderItem("SKU-1001", "Widget A", 10), new OrderItem("SKU-1002", "Widget B", 5))));
        store.add(new Order(null, "Globex", OrderStatus.CANCELED, "SGN", LocalDateTime.now().minusHours(10),
                List.of(new OrderItem("SKU-2001", "Gadget X", 2))));
        store.add(new Order(null, "Initech", OrderStatus.CANCELED, "NYC", LocalDateTime.now().minusHours(5),
                List.of(new OrderItem("SKU-1001", "Widget A", 1), new OrderItem("SKU-3003", "Device Z", 3))));
        store.add(new Order(null, "Hooli", OrderStatus.PROCESSING, null, LocalDateTime.now().minusHours(2),
                List.of(new OrderItem("SKU-4004", "Part Q", 7))));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(store);
    }

    @Override
    public List<Order> findByStatus(com.app.Ioms.domain.OrderStatus status) {
        return store.stream().filter(o -> o.getStatus() == status).collect(Collectors.toList());
    }

    @Override
    public List<Order> findCanceledBySite(String siteCode) {
        return store.stream()
                .filter(o -> o.getStatus() == OrderStatus.CANCELED)
                .filter(o -> siteCode == null || siteCode.isBlank() || siteCode.equalsIgnoreCase(o.getCanceledBySite()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Order> findById(String id) {
        return store.stream().filter(o -> o.getId().equals(id)).findFirst();
    }

    @Override
    public void save(Order order) {
        store.add(order);
    }

    @Override
    public void update(Order order) {
        store.replaceAll(o -> o.getId().equals(order.getId()) ? order : o);
    }
}
    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return store.stream().filter(o -> o.getStatus() == status).collect(Collectors.toList());
    }

    @Override
    public List<Order> findCanceledBySite(String siteCode) {
        return store.stream()
                .filter(o -> o.getStatus() == OrderStatus.CANCELED)
                .filter(o -> siteCode == null || siteCode.isBlank() || siteCode.equalsIgnoreCase(o.getCanceledBySite()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Order> findById(String id) {
        return store.stream().filter(o -> o.getId().equals(id)).findFirst();
    }

    @Override
    public void save(Order order) {
        store.add(order);
    }

    @Override
    public void update(Order order) {
        store.replaceAll(o -> o.getId().equals(order.getId()) ? order : o);
    }
}
