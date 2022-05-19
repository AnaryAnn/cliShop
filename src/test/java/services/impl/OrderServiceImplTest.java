package services.impl;

import exceptions.OrderException;
import exceptions.WalletException;
import model.Amount;
import model.Item;
import model.Status;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.api.ItemService;
import services.api.OrderService;
import services.api.WalletService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static model.Currency.RUB;

public class OrderServiceImplTest {
    
    private final WalletService walletService = WalletServiceImpl.getInstance();
    private final OrderService orderService = OrderServiceImpl.getInstance();
    private final ItemService itemService = new ItemServiceImpl();

    private Long userId;


    @Test
    public void testCreateOrder() throws OrderException, WalletException {

        userId = 1L;
        walletService.createWallet(userId);
        walletService.deposit(userId, new Amount(RUB, 100d));
        List<Item> items = new ArrayList<>();
        items.add(itemService.findItemById(1L).get());

        Long orderId = orderService.createOrder(userId, Collections.singleton(items.get(0).getId())).getId();
        Assert.assertEquals(orderService.findOrder(orderId).get().getTotalAmount().getSum(), 83d);
        Assert.assertEquals(orderService.findOrder(orderId).get().getStatus(), Status.CREATED);
    }

    @Test
    public void testPayment() throws OrderException, WalletException {

        userId = 2L;
        walletService.createWallet(userId);
        walletService.deposit(userId, new Amount(RUB, 100d));

        List<Item> items = new ArrayList<>();
        items.add(itemService.findItemById(1L).get());

        Long orderId = orderService.createOrder(userId, Collections.singleton(items.get(0).getId())).getId();

        orderService.payment(userId, orderService.findOrder(orderId).get().getId());
        Assert.assertEquals(orderService.findOrder(orderId).get().getStatus(), Status.PAID);

    }

    @Test
    public void testRefund() throws WalletException, OrderException {

        userId = 3L;
        walletService.createWallet(userId);
        walletService.deposit(userId, new Amount(RUB, 100d));

        List<Item> items = new ArrayList<>();
        items.add(itemService.findItemById(1L).get());

        Long orderId = orderService.createOrder(userId, Collections.singleton(items.get(0).getId())).getId();

        orderService.payment(userId, orderService.findOrder(orderId).get().getId());
        Assert.assertEquals(orderService.findOrder(orderId).get().getStatus(), Status.PAID);
        Assert.assertEquals(walletService.getUserBalance(userId).get(RUB), 17d);

        orderService.refund(userId, orderService.findOrder(orderId).get().getId());
        Assert.assertEquals(orderService.findOrder(orderId).get().getStatus(), Status.REFUNDED);
        Assert.assertEquals(walletService.getUserBalance(userId).get(RUB), 100d);

    }
}