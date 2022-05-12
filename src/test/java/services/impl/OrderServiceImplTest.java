package services.impl;

import exceptions.OrderException;
import model.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.api.OrderService;

import java.util.ArrayList;
import java.util.Collection;

import static model.Currency.RUB;

public class OrderServiceImplTest {

    @Test
    public void testCreateOrder() throws OrderException {

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

        OrderService orderService = new OrderServiceImpl();
        Order order = orderService.createOrder(123321L, items);
        Assert.assertEquals(order.getTotalAmount().getSum(), 6d);
        Assert.assertEquals(order.getStatus(), Status.CREATED);
    }

    @Test
    public void testPayment() {
    }

    @Test
    public void testRefund() {
    }
}