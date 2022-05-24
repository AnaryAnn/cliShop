package application.service.impl;

import application.service.api.WalletService;
import model.Amount;
import model.Wallet;
import org.testng.Assert;
import org.testng.annotations.Test;

import static model.Currency.RUB;

/**
 * Тестирование работы с кошельком
 */
public class WalletServiceImplTest {

    @Test
    public void testDeposit() {

        WalletService walletService = WalletServiceImpl.getInstance();
        Wallet wallet = walletService.createWallet(32131L);
        try {
            walletService.deposit(32131l, new Amount(RUB, 100d));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(wallet.getBalance().get(RUB), 100d);
    }

}