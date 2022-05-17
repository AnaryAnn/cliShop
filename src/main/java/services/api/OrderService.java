package services.api;

import exceptions.OrderException;
import exceptions.WalletException;
import model.Item;
import model.Order;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * Интерфейс для работы с заказами
 */
public interface OrderService {

    /**
     * Создание заказа
     *
     * @param userId идентификатор пользователя
     * @param items список товаров
     * @return созданный заказ
     */
    Order createOrder(Long userId, Collection<Item> items) throws OrderException;

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

    /**
     * Получение списка всех заказов пользователя
     * @param userId идентификатор пользователя
     * @return набор свех заказов пользователя
     */
    Set<Order> getUserOrdersSet(Long userId);

    /**
     * Получение списка всех заказов
     * @return набор свех заказов
     */
    Collection<Order> getAllOrders();

}
