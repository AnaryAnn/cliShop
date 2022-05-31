package application.service.api;

import application.model.OrderDTO;
import exceptions.OrderException;
import exceptions.WalletException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    OrderDTO createOrder(Long userId, Collection<Long> items) throws OrderException;

    /**
     * Поиск заказа
     * @param orderId идентификатор заказа
     * @return Заказ
     */
    Optional<OrderDTO> findOrder(Long orderId);

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
    List<OrderDTO> getUserOrdersSet(Long userId);

    /**
     * Получение списка всех заказов
     * @return набор свех заказов
     */
    Collection<OrderDTO> getAllOrders();

}
