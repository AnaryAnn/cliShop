package services.api;

import model.Category;
import model.Item;
import model.Order;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
    Map<Item, Long> getBestSellers();

    /**
     * Получение категорий-бестселлеров и кол-во продаж в них
     *
     * @return мапа категория/кол-во продаж
     */
    Map<Category, Long> getBestSellCategory();

    /**
     * Получение категорий и денежный оборот в них
     *
     * @return мапа категория/денежный оборот
     */
    Map<Category, Double> getFinancialIncome();
}
