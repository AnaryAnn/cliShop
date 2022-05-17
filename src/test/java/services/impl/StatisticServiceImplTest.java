package services.impl;

import exceptions.OrderException;
import model.Amount;
import model.Category;
import model.Item;
import org.testng.annotations.Test;
import services.api.OrderService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static model.Currency.RUB;
import static org.testng.Assert.*;

/**
 * Тестирование сервиса статистики
 */
public class StatisticServiceImplTest {

    @Test
    public void testGetBestSellers() throws OrderException {

        StatisticServiceImpl statisticService = new StatisticServiceImpl();
        Map<Item, Long> bestSellers = statisticService.getBestSellers();
        assertNotNull(bestSellers);
    }
}