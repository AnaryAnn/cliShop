package services.impl;

import exceptions.OrderException;
import model.Currency;
import model.*;
import services.api.OrderService;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static model.Currency.RUB;

public class OrderServiceImpl implements OrderService {

    private static final Currency defaultCurrency = RUB;

    private final Map<Long, Set<Order>> ordersMap = new HashMap<>();
    private Long orderSequence = 0L;

    @Override
    public Order createOrder(Long userId, Collection<Item> items) throws OrderException {
        requireNonNull(userId, "userId");
        if (items.isEmpty()) {
            throw new OrderException("Корзина пуста");
        }

        Order order = Order.builder()
                .setId(++orderSequence)
                .setStatus(Status.CREATED)
                .setItems(items)
                .setTotalAmount(getTotalAmount(items))
                .build();

        saveOrder(userId, order);

        return order;
    }

    private void saveOrder(Long userId, Order order) {
        Set<Order> userOrders = ordersMap.get(userId);
        if (userOrders == null) {
            userOrders = new HashSet<>();
        }
        userOrders.add(order);
        ordersMap.put(userId, userOrders);
    }

    private Amount getTotalAmount(Collection<Item> items) {
        double sum = items.stream()
                .mapToDouble(item -> convertSumByCurrency(item.getAmount(), defaultCurrency))
                .sum();
        return new Amount(defaultCurrency, sum);
    }

    private double convertSumByCurrency(Amount amount, Currency newCurrency) {
        requireNonNull(amount, "amount");
        requireNonNull(newCurrency, "newCurrency");

        // todo: сделать конвертацию в отдельном сервисе
//        switch (newCurrency) {
//            case RUB:
//                break;
//            case USD:
//                break;
//            case EUR:
//                break;
//        }
        return amount.getSum();
    }

    @Override
    public void payment() {

    }

    @Override
    public void refund() {

    }
}
