package application.service.impl;

import application.service.api.ItemService;
import application.service.api.OrderService;
import exceptions.OrderException;
import exceptions.WalletException;
import model.Currency;
import model.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static model.Currency.RUB;

public class OrderServiceImpl implements OrderService {

    private static final Currency defaultCurrency = RUB;
    private static OrderServiceImpl orderService;
    /**
     * Хранилище идентификаторов заказов пользователя (id пользователя - список id заказов пользователя)
     */
    private final Map<Long, Set<Long>> userOrdersMap = new HashMap<>();
    /**
     * Хранилище заказов (id заказа - заказ)
     */
    private final Map<Long, Order> ordersMap = new HashMap<>();
    private ItemService itemService = new ItemServiceImpl();
    private Long orderSequence = 0L;

    private OrderServiceImpl() {
    }

    public static OrderServiceImpl getInstance() {
        if (orderService == null) {
            orderService = new OrderServiceImpl();
        }
        return orderService;
    }

    @Override
    public Order createOrder(Long userId, Collection<Long> itemIds) throws OrderException {
        requireNonNull(userId, "userId");
        if (itemIds.isEmpty()) {
            throw new OrderException("Корзина пуста");
        }

        List<Long> notExistedItemIds = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        for (Long id : itemIds) {
            Optional<Item> itemOptional = itemService.findItemById(id);
            if (itemOptional.isEmpty()) {
                notExistedItemIds.add(id);
            } else {
                items.add(itemOptional.get());
            }
        }
        if (!notExistedItemIds.isEmpty()){
            throw new OrderException(String.format("Товары с id=%s не найдены\n", notExistedItemIds));
        }

        Order order = Order.builder()
                .setId(++orderSequence)
                .setUserId(userId)
                .setStatus(Status.CREATED)
                .setItems(items)
                .setTotalAmount(getTotalAmount(items))
                .build();

        saveOrder(order);

        return order;
    }

    private void saveOrder(Order order) {
        ordersMap.put(order.getId(), order);

        Set<Long> userOrderIds = userOrdersMap.get(order.getUserId());
        if (userOrderIds == null) {
            userOrderIds = new HashSet<>();
        }
        userOrderIds.add(order.getId());
        userOrdersMap.put(order.getUserId(), userOrderIds);
    }

    private void deleteOrder(Order order) {
        ordersMap.remove(order.getId());
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

        return amount.getSum();
    }

    @Override
    public void payment(Long userId, Long orderId) throws WalletException, OrderException {
        requireNonNull(userId, "userId");
        requireNonNull(orderId, "orderId");

        Optional<Order> orderOptional = findOrder(orderId);
        if (orderOptional.isEmpty()) {
            throw new OrderException(String.format("Заказ №%s не существует", orderId));
        }

        Order order = orderOptional.get();

        if (!order.getUserId().equals(userId)) {
            throw new OrderException(String.format("Заказ №%s не существует", orderId));
        }

        if (order.getStatus().equals(Status.PAID)) {
            throw new OrderException(String.format("Заказ №%s уже оплачен", orderId));
        }

        if (order.getStatus().equals(Status.REFUNDED)) {
            throw new OrderException(String.format("Невозможно оплатить.\nНа заказ №%s оформлен возврат", orderId));
        }

        WalletServiceImpl.getInstance().withdraw(userId, order.getTotalAmount());

        deleteOrder(order);

        order = Order.builder()
                .setId(order.getId())
                .setUserId(userId)
                .setStatus(Status.PAID)
                .setItems(order.getItems())
                .setTotalAmount(order.getTotalAmount())
                .build();

        saveOrder(order);
    }

    @Override
    public void refund(Long userId, Long orderId) throws OrderException, WalletException {
        requireNonNull(userId, "userId");
        requireNonNull(orderId, "orderId");

        Optional<Order> orderOptional = findOrder(orderId);
        if (orderOptional.isEmpty()) {
            throw new OrderException(String.format("Заказ №%s не существует", orderId));
        }

        Order order = orderOptional.get();

        if (!order.getUserId().equals(userId)) {
            throw new OrderException(String.format("Заказ №%s не существует", orderId));
        }

        if (!order.getStatus().equals(Status.PAID)) {
            throw new OrderException(String.format("Оформить возврат можно только по оплаченному заказу\n" +
                    "Статус заказа №%s: %s", orderId, order.getStatus()));
        }

        WalletServiceImpl.getInstance().deposit(userId, order.getTotalAmount());

        deleteOrder(order);

        order = Order.builder()
                .setId(order.getId())
                .setUserId(userId)
                .setStatus(Status.REFUNDED)
                .setItems(order.getItems())
                .setTotalAmount(order.getTotalAmount())
                .build();

        saveOrder(order);
    }

    @Override
    public Optional<Order> findOrder(Long orderId) {
        return Optional.ofNullable(ordersMap.get(orderId));
    }

    @Override
    public Set<Order> getUserOrdersSet(Long userId) {
        requireNonNull(userId, "userId");

        Optional<Set<Long>> userOrderIdsOptional = Optional.ofNullable(userOrdersMap.get(userId));
        if (userOrderIdsOptional.isEmpty()) {
            return Collections.emptySet();
        }

        return userOrderIdsOptional.get().stream()
                .map(ordersMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Order> getAllOrders() {
        return ordersMap.values();
    }

}
