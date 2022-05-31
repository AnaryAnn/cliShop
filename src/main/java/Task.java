import exceptions.OrderException;
import exceptions.WalletException;
import gui.impl.CommandLineInterfaceImpl;

public class Task {
    public static void main(String[] args) throws WalletException, OrderException {

       /* Long userId = 1L;

        WalletService walletService = WalletServiceImpl.getInstance();
        walletService.createWallet(userId);
        walletService.deposit(userId, new AmountDTO(RUB, 1000d));
        */
        new CommandLineInterfaceImpl().run();
    }
}
