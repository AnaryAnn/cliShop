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

public class StatisticServiceImplTest {

    @Test
    public void testGetBestSellers() throws OrderException {
        Category category = Category.builder()
                .setId(65464L)
                .setName("Data Storage")
                .build();

        Collection<Item> items = new ArrayList<>();
        items.add(Item.builder()
                .setId(456464L)
                .setName("SAMSUNG 970 EVO")
                .setCategory(category)
                .setAmount(new Amount(RUB, 1d))
                .build());

        items.add(Item.builder()
                .setId(4567894L)
                .setName("Seagate Portable 2TB")
                .setCategory(category)
                .setAmount(new Amount(RUB, 2d))
                .build());

        items.add(Item.builder()
                .setId(12894L)
                .setName("SanDisk 128GB Ultra Flair ")
                .setCategory(category)
                .setAmount(new Amount(RUB, 3d))
                .build());

        items.add(Item.builder()
                .setId(12894L)
                .setName("SanDisk 128GB Ultra Flair ")
                .setCategory(category)
                .setAmount(new Amount(RUB, 3d))
                .build());

        items.add(Item.builder()
                .setId(12894L)
                .setName("SanDisk 128GB Ultra Flair ")
                .setCategory(category)
                .setAmount(new Amount(RUB, 3d))
                .build());

        OrderService orderService = OrderServiceImpl.getInstance(); //todo: [Review] это нам нужно? и ниже
        Long orderId = orderService.createOrder(123321L, items);

        StatisticServiceImpl statisticService = new StatisticServiceImpl();
        Map<Item, Long> bestSellers = statisticService.getBestSellers();
        assertNotNull(bestSellers);
    }
}