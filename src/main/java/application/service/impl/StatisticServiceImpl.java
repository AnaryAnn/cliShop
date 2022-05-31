package application.service.impl;

import application.model.ItemDTO;
import application.model.OrderDTO;
import application.model.Status;
import application.service.api.OrderService;
import application.service.api.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private OrderService orderService;

    @Override
    public Set<OrderDTO> getOrderHistory(Long userId) {
        return orderService.getUserOrdersSet(userId).stream()
                .sorted(Comparator.comparing(OrderDTO::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Map<String, Long> getBestSellers() {
        Optional<Collection<OrderDTO>> ordersOptional = Optional.ofNullable(orderService.getAllOrders());
        if (!ordersOptional.isPresent()) {
            return Collections.emptyMap();
        }

        Collection<OrderDTO> orders = ordersOptional.get();
        Map<String, Long> notSortedMap = orders.stream()
                .filter(order -> order.getStatus() == Status.PAID)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(ItemDTO::getName, Collectors.counting()));

        return notSortedMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public Map<String, Long> getBestSellCategory() {
        Optional<Collection<OrderDTO>> ordersOptional = Optional.ofNullable(orderService.getAllOrders());
        if (!ordersOptional.isPresent()) {
            return Collections.emptyMap();
        }

        Collection<OrderDTO> orders = ordersOptional.get();
        Map<String, Long> notSortedMap = orders.stream()
                .filter(order -> order.getStatus() == Status.PAID)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(item -> item.getCategory().getName(), Collectors.counting()));

        return notSortedMap.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    @Override
    public Map<String, Double> getFinancialIncome() {
        return orderService.getAllOrders().stream()
                .filter(order -> order.getStatus() == Status.PAID)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(item -> item.getCategory().getName(), Collectors.summingDouble(value -> value.getAmount().getSum())));
    }
}
