package services.impl;

import exceptions.OrderException;
import exceptions.WalletException;
import model.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.api.OrderService;
import services.api.WalletService;

import java.util.ArrayList;
import java.util.Collection;

import static model.Currency.RUB;

public class OrderServiceImplTest {

    @Test
    public void testCreateOrder() throws OrderException, WalletException {

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

        OrderService orderService = OrderServiceImpl.getInstance();
        Long orderId = orderService.createOrder(123321L, items);
        Assert.assertEquals(orderService.findOrder(orderId).get().getTotalAmount().getSum(), 6d);
        Assert.assertEquals(orderService.findOrder(orderId).get().getStatus(), Status.CREATED);
    }

    @Test
    public void testPayment() throws OrderException, WalletException {

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

        WalletService walletService = WalletServiceImpl.getInstance();
        walletService.createWallet(123321L);
        walletService.deposit(123321L, new Amount(RUB, 100d));

        OrderService orderService = OrderServiceImpl.getInstance();
        Long orderId = orderService.createOrder(123321L, items);

        orderService.payment(123321L, orderService.findOrder(orderId).get().getId());
        Assert.assertEquals(orderService.findOrder(orderId).get().getStatus(), Status.PAID);

    }

    @Test
    public void testRefund() throws WalletException, OrderException {

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

        WalletService walletService = WalletServiceImpl.getInstance();
        walletService.createWallet(123321L);
        walletService.deposit(123321L, new Amount(RUB, 100d));

        OrderService orderService = OrderServiceImpl.getInstance();
        Long orderId = orderService.createOrder(123321L, items);

        orderService.payment(123321L, orderService.findOrder(orderId).get().getId());
        Assert.assertEquals(orderService.findOrder(orderId).get().getStatus(), Status.PAID);
        Assert.assertEquals(walletService.getBalance(123321L).get(RUB), 94d);

        orderService.refund(123321L, orderService.findOrder(orderId).get().getId());
        Assert.assertEquals(orderService.findOrder(orderId).get().getStatus(), Status.REFUNDED);
        Assert.assertEquals(walletService.getBalance(123321L).get(RUB), 100d);

    }
}