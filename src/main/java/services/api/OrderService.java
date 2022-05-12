package services.api;

import exceptions.OrderException;
import model.Item;
import model.Order;

import java.util.Collection;

public interface OrderService {

    /**
     * Создание заказа
     *
     * @param userId идентификатор пользователя
     * @param items список товаров
     * @return созданный заказ
     */
    Order createOrder(Long userId, Collection<Item> items) throws OrderException;

    void payment();
    void refund();

}
