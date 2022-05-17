package services.impl;

import model.Amount;
import model.Wallet;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.api.WalletService;

import static model.Currency.RUB;
import static org.testng.Assert.*;

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