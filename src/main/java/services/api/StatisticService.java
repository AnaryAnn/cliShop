package services.api;

import model.Order;

import java.util.Map;
import java.util.Set;

/**
 * Интерфейс сбора статистики
 */
public interface StatisticService {

    /**
     * Получение списка заказов пользователя
     *
     * @param userId идентификатор пользователя
     * @return список заказов пользователя
     */
    Set<Order> getOrderHistory(Long userId);

    /**
     * Получение товаров-бестселлеров и кол-во их продаж
     *
     * @return мапа товар/кол-во продаж
     */
    Map<String, Long> getBestSellers();

    /**
     * Получение категорий-бестселлеров и кол-во продаж в них
     *
     * @return мапа категория/кол-во продаж
     */
    Map<String, Long> getBestSellCategory();

    /**
     * Получение категорий и денежный оборот в них
     *
     * @return мапа категория/денежный оборот
     */
    Map<String, Double> getFinancialIncome();
}
