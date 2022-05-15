import exceptions.OrderException;
import exceptions.WalletException;
import gui.impl.CommandLineInterfaceImpl;
import model.*;
import services.api.OrderService;
import services.api.WalletService;
import services.impl.OrderServiceImpl;
import services.impl.WalletServiceImpl;

import java.util.ArrayList;
import java.util.Collection;

import static model.Currency.RUB;

public class Task {
    public static void main(String[] args) throws WalletException, OrderException {

        Long userId = 1L;

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
        walletService.createWallet(userId);
        walletService.deposit(userId, new Amount(RUB, 100d));

        OrderService orderService = OrderServiceImpl.getInstance();
        Long orderId = orderService.createOrder(userId, items);

        orderService.payment(userId, orderService.findOrder(orderId).get().getId());

        items.add(Item.builder()
                .setId(12894L)
                .setName("SanDisk 128GB Ultra Flair ")
                .setCategory(category)
                .setAmount(new Amount(RUB, 3d))
                .build());
        orderId = orderService.createOrder(userId, items);
        orderService.payment(userId, orderService.findOrder(orderId).get().getId());


        new CommandLineInterfaceImpl().run();
    }
}
