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

        WalletService walletService = WalletServiceImpl.getInstance();
        walletService.createWallet(userId);
        walletService.deposit(userId, new Amount(RUB, 100d));

        new CommandLineInterfaceImpl().run();
    }
}
