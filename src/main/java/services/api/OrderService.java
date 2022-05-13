package services.api;

import exceptions.OrderException;
import exceptions.WalletException;
import model.Item;
import model.Order;

import java.util.Collection;
import java.util.Optional;

public interface OrderService {

    /**
     * Создание заказа
     *
     * @param userId идентификатор пользователя
     * @param items список товаров
     * @return созданный заказ
     */
    Long createOrder(Long userId, Collection<Item> items) throws OrderException;

    /**
     * Поиск заказа
     * @param orderId идентификатор заказа
     * @return Заказ
     */
    Optional<Order> findOrder(Long orderId);

    /**
     * Оплата созданного заказа
     *  @param userId идентификатор пользователя
     * @param orderId идентификатор заказа
     */
    void payment(Long userId, Long orderId) throws WalletException, OrderException;

    /**
     * Возврат оплаченного созданного заказа
     *  @param userId идентификатор пользователя
     * @param orderId идентификатор заказа
     */
    void refund(Long userId, Long orderId) throws OrderException, WalletException;



}
