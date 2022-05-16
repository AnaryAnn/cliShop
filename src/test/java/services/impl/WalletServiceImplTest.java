package services.impl;

import model.Amount;
import model.Wallet;
import org.testng.Assert;
import org.testng.annotations.Test;
import services.api.WalletService;

import static model.Currency.RUB;
import static org.testng.Assert.*;
//todo: [Review] комменты в тестах тож нужны, хотя бы перед классами, если названия методов говорящие
public class WalletServiceImplTest {

    @Test
    public void testDeposit() {

        WalletService walletService = WalletServiceImpl.getInstance();
        Wallet wallet = walletService.createWallet(32131l); ////todo: [Review] почему L в нижнем регистре?
        try {
            walletService.deposit(32131l, new Amount(RUB, 100d));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(wallet.getBalance().get(RUB), 100d);
    }

    //todo: [Review] ниже мусор
    @Test
    public void testWithdraw() {
    }

    @Test
    public void testCreateWallet() {

    }

    @Test
    public void testGetBalance() {
    }
}