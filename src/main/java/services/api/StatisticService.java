package services.api;

import model.Category;
import model.Item;
import model.Order;

import java.util.Map;
import java.util.Set;

public interface StatisticService {

    Set<Order> getOrderHistory(Long userId);

    Map<Item, Long> getBestSellers();

    Map<Category, Long> getBestSellerCategory();

    void getCirculationMoney();
}
