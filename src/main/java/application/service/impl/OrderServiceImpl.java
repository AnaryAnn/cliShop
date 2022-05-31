package application.service.impl;

import application.data.Order;
import application.model.Currency;
import application.model.*;
import application.repository.OrderRepository;
import application.service.api.ItemService;
import application.service.api.OrderService;
import application.service.api.WalletService;
import exceptions.OrderException;
import exceptions.WalletException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static application.model.Currency.RUB;
import static java.util.Objects.requireNonNull;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Currency defaultCurrency = RUB;
    private final Function<Order, OrderDTO> MAPPER_TO_DTO = entity -> OrderDTO.builder()
            .setId(entity.getId())
            .setUserId(entity.getUserId())
            .setItems(entity.getItems().stream().map(ItemServiceImpl.MAPPER_TO_DTO).collect(Collectors.toList()))
            .setStatus(Status.valueOf(entity.getStatus()))
            .setTotalAmount(new AmountDTO(Currency.valueOf(entity.getCurrency()), entity.getAmount()))
            .build();

    @Autowired
    private ItemService itemService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrderDTO createOrder(Long userId, Collection<Long> itemIds) throws OrderException {
        requireNonNull(userId, "userId");
        if (itemIds.isEmpty()) {
            throw new OrderException("Корзина пуста");
        }

        List<Long> notExistedItemIds = new ArrayList<>();
        List<ItemDTO> items = new ArrayList<>();
        for (Long id : itemIds) {
            Optional<ItemDTO> itemOptional = itemService.findItemById(id);
            if (!itemOptional.isPresent()) {
                notExistedItemIds.add(id);
            } else {
                items.add(itemOptional.get());
            }
        }
        if (!notExistedItemIds.isEmpty()) {
            throw new OrderException(String.format("Товары с id=%s не найдены\n", notExistedItemIds));
        }

        return saveOrder(OrderDTO.builder()
                .setId(Long.MIN_VALUE)
                .setUserId(userId)
                .setStatus(Status.CREATED)
                .setItems(items)
                .setTotalAmount(getTotalAmount(items))
                .build());
    }

    private OrderDTO saveOrder(OrderDTO order) {
        mapperDtoToEntity();
        return MAPPER_TO_DTO.apply(orderRepository.save(modelMapper.map(order, Order.class)));
    }

    private void mapperDtoToEntity() {
        TypeMap<OrderDTO, Order> propertyMapper = modelMapper.getTypeMap(OrderDTO.class, Order.class);
        if (propertyMapper == null) {
            propertyMapper = modelMapper.createTypeMap(OrderDTO.class, Order.class);
            propertyMapper.addMappings(
                    mapper -> mapper.map(src -> src.getTotalAmount().getCurrency(), Order::setCurrency)
            );
            propertyMapper.addMappings(
                    mapper -> mapper.map(src -> src.getTotalAmount().getSum(), Order::setAmount)
            );
        }
    }

    private void deleteOrder(OrderDTO order) {
        orderRepository.delete(modelMapper.map(order, Order.class));
    }

    private AmountDTO getTotalAmount(Collection<ItemDTO> items) {
        double sum = items.stream()
                .mapToDouble(item -> convertSumByCurrency(item.getAmount(), defaultCurrency))
                .sum();
        return new AmountDTO(defaultCurrency, sum);
    }

    private double convertSumByCurrency(AmountDTO amount, Currency newCurrency) {
        requireNonNull(amount, "amount");
        requireNonNull(newCurrency, "newCurrency");

        return amount.getSum();
    }

    @Transactional
    @Override
    public void payment(Long userId, Long orderId) throws WalletException, OrderException {
        requireNonNull(userId, "userId");
        requireNonNull(orderId, "orderId");

        Optional<OrderDTO> orderOptional = findOrder(orderId);
        if (!orderOptional.isPresent()) {
            throw new OrderException(String.format("Заказ №%s не существует", orderId));
        }

        OrderDTO order = orderOptional.get();

        if (!order.getUserId().equals(userId)) {
            throw new OrderException(String.format("Заказ №%s не существует", orderId));
        }

        if (order.getStatus().equals(Status.PAID)) {
            throw new OrderException(String.format("Заказ №%s уже оплачен", orderId));
        }

        if (order.getStatus().equals(Status.REFUNDED)) {
            throw new OrderException(String.format("Невозможно оплатить.\nНа заказ №%s оформлен возврат", orderId));
        }

        AmountDTO totalAmount = order.getTotalAmount();
        walletService.withdraw(userId, totalAmount);
        saveOrder(OrderDTO.builder()
                .setId(orderId)
                .setUserId(userId)
                .setStatus(Status.PAID)
                .setItems(order.getItems())
                .setTotalAmount(totalAmount)
                .build());
    }

    @Override
    public void refund(Long userId, Long orderId) throws OrderException, WalletException {
        requireNonNull(userId, "userId");
        requireNonNull(orderId, "orderId");

        Optional<OrderDTO> orderOptional = findOrder(orderId);
        if (!orderOptional.isPresent()) {
            throw new OrderException(String.format("Заказ №%s не существует", orderId));
        }

        OrderDTO order = orderOptional.get();

        if (!order.getUserId().equals(userId)) {
            throw new OrderException(String.format("Заказ №%s не существует", orderId));
        }

        if (!order.getStatus().equals(Status.PAID)) {
            throw new OrderException(String.format("Оформить возврат можно только по оплаченному заказу\n" +
                    "Статус заказа №%s: %s", orderId, order.getStatus()));
        }

        AmountDTO totalAmount = order.getTotalAmount();
        walletService.deposit(userId, totalAmount);
        saveOrder(OrderDTO.builder()
                .setId(orderId)
                .setUserId(userId)
                .setStatus(Status.REFUNDED)
                .setItems(order.getItems())
                .setTotalAmount(totalAmount)
                .build());
    }

    @Override
    public Optional<OrderDTO> findOrder(Long orderId) {
        return orderRepository.findById(orderId).map(MAPPER_TO_DTO);
    }

    @Override
    public List<OrderDTO> getUserOrdersSet(Long userId) {
        requireNonNull(userId, "userId");
        Optional<List<Order>> userOrdersOptional = Optional.ofNullable(orderRepository.findOrdersByUserId(userId));
        return userOrdersOptional.map(orders -> orders.stream()
                .map(MAPPER_TO_DTO)
                .collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    @Override
    public Collection<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(MAPPER_TO_DTO).collect(Collectors.toList());
    }

}
