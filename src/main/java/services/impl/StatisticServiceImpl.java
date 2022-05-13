package services.impl;

import model.Category;
import model.Item;
import model.Order;
import model.Status;
import services.api.OrderService;
import services.api.StatisticService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatisticServiceImpl implements StatisticService {
    @Override
    public Set<Order> getOrderHistory(Long userId) {
        OrderService orderService = OrderServiceImpl.getInstance();
        return orderService.getUserOrdersSet(userId);

    }

    @Override
    public Map<Item, Long> getBestSellers() {
        OrderService orderService = OrderServiceImpl.getInstance();
        Optional<Collection<Order>> ordersOptional = Optional.ofNullable(orderService.getAllOrders());
        if (ordersOptional.isEmpty()) {
            return Collections.emptyMap();
        }

        Collection<Order> orders = ordersOptional.get();
        return orders.stream()
                .filter(order -> order.getStatus() == Status.PAID)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    @Override
    public Map<Category, Long> getBestSellerCategory() {
        OrderService orderService = OrderServiceImpl.getInstance();
        Optional<Collection<Order>> ordersOptional = Optional.ofNullable(orderService.getAllOrders());
        if (ordersOptional.isEmpty()) {
            return Collections.emptyMap();
        }

        Collection<Order> orders = ordersOptional.get();
        return orders.stream()
                .filter(order -> order.getStatus() == Status.PAID)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(item -> item.getCategory(), Collectors.counting()));

    }

    @Override
    public void getCirculationMoney() {

    }
}
