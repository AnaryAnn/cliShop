package services.impl;

import model.Item;
import model.Order;
import model.Status;
import services.api.OrderService;
import services.api.StatisticService;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticServiceImpl implements StatisticService {
    @Override
    public Set<Order> getOrderHistory(Long userId) {
        OrderService orderService = OrderServiceImpl.getInstance();
        return orderService.getUserOrdersSet(userId).stream()
                .sorted(Comparator.comparing(Order::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Map<String, Long> getBestSellers() {
        OrderService orderService = OrderServiceImpl.getInstance();
        Optional<Collection<Order>> ordersOptional = Optional.ofNullable(orderService.getAllOrders());
        if (ordersOptional.isEmpty()) {
            return Collections.emptyMap();
        }

        Collection<Order> orders = ordersOptional.get();
        return orders.stream()
                .filter(order -> order.getStatus() == Status.PAID)
                .flatMap(order -> order.getItems().stream())
                .limit(10)
                .collect(Collectors.groupingBy(Item::getName, Collectors.counting()));
    }

    @Override
    public Map<String, Long> getBestSellCategory() {
        OrderService orderService = OrderServiceImpl.getInstance();
        Optional<Collection<Order>> ordersOptional = Optional.ofNullable(orderService.getAllOrders());
        if (ordersOptional.isEmpty()) {
            return Collections.emptyMap();
        }

        Collection<Order> orders = ordersOptional.get();
        return orders.stream()
                .filter(order -> order.getStatus() == Status.PAID)
                .flatMap(order -> order.getItems().stream())
                .limit(10)
                .collect(Collectors.groupingBy(item -> item.getCategory().getName(), Collectors.counting()));
    }

    @Override
    public Map<String, Double> getFinancialIncome() {

        return OrderServiceImpl.getInstance().getAllOrders().stream()
                .filter(order -> order.getStatus() == Status.PAID)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(item -> item.getCategory().getName(), Collectors.summingDouble(value -> value.getAmount().getSum())));
    }
}
