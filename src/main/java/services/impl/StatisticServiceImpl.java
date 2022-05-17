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
                .limit(10)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    @Override
    public Map<Category, Long> getBestSellCategory() {
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
                .collect(Collectors.groupingBy(Item::getCategory, Collectors.counting()));

    }

    @Override
    public Map<Category, Double> getFinancialIncome() {

        return OrderServiceImpl.getInstance().getAllOrders().stream()
                .filter(order -> order.getStatus() == Status.PAID)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(Item::getCategory, Collectors.summingDouble(value -> value.getAmount().getSum())));
    }
}
