import application.service.api.WalletService;
import application.service.impl.WalletServiceImpl;
import exceptions.OrderException;
import exceptions.WalletException;
import gui.impl.CommandLineInterfaceImpl;
import model.Amount;

import static model.Currency.RUB;

public class Task {
    public static void main(String[] args) throws WalletException, OrderException {

        Long userId = 1L;

        WalletService walletService = WalletServiceImpl.getInstance();
        walletService.createWallet(userId);
        walletService.deposit(userId, new Amount(RUB, 1000d));

        new CommandLineInterfaceImpl().run();
    }
}
