package services.impl;

import exceptions.WalletException;
import model.Amount;
import model.Currency;
import model.Wallet;
import services.api.WalletService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WalletServiceImpl implements WalletService {

    private static WalletServiceImpl walletService;
    private final Map<Long, Wallet> walletUserMap;

    private WalletServiceImpl() {
        this.walletUserMap = new HashMap<>();
    }

    public static WalletServiceImpl getInstance() {
        if (walletService == null) {
            walletService = new WalletServiceImpl();
        }
        return walletService;
    }

    @Override
    public void deposit(Long userId, Amount amount) throws WalletException {

        if (!isAmountPositive(amount)) {
            throw new WalletException("Сумма не может быть отрицательной");
        }

        if (amount.getSum() > Double.MAX_VALUE){
            throw new WalletException(String.format("Баланс не может быть больше %s", Double.MAX_VALUE));
        }

        Wallet userWallet = findWallet(userId).orElseGet(() -> createWallet(userId));

        Map<Currency, Double> walletBalance = userWallet.getBalance();

        if (walletBalance.containsKey(amount.getCurrency())) {
            Double currencyBalance = walletBalance.get(amount.getCurrency());
            if (currencyBalance + amount.getSum() > Double.MAX_VALUE){
                throw new WalletException(String.format("Баланс не может быть больше %s", Double.MAX_VALUE));
            }
            walletBalance.put(amount.getCurrency(), currencyBalance + amount.getSum());
        } else {
            walletBalance.put(amount.getCurrency(), amount.getSum());
        }
    }

    private Optional<Wallet> findWallet(long userId) {
        return Optional.ofNullable(walletUserMap.get(userId));
    }

    @Override
    public void withdraw(Long userId, Amount amount) throws WalletException {
        if (!isAmountPositive(amount)) {
            throw new WalletException("Сумма не может быть отрицательной");
        }

        Optional<Wallet> userWalletOptional = findWallet(userId);
        if (userWalletOptional.isEmpty()) {
            throw new WalletException("Кошелек не найден");
        }

        Wallet userWallet = userWalletOptional.get();
        Map<Currency, Double> walletBalance = userWallet.getBalance();

        if (!isEnoughMoneyWallet(userWallet, amount)) {
            throw new WalletException("Недостаточно средств на счете");
        }

        Double currencyBalance = walletBalance.get(amount.getCurrency());
        walletBalance.put(amount.getCurrency(), currencyBalance - amount.getSum());
    }

    private boolean isEnoughMoneyWallet(Wallet userWallet, Amount amount) {
        Map<Currency, Double> walletBalance = userWallet.getBalance();
        if (!walletBalance.containsKey(amount.getCurrency())) {
            return false;
        }
        return walletBalance.get(amount.getCurrency()) >= amount.getSum();
    }

    private boolean isAmountPositive(Amount amount) {
        return amount != null && amount.getSum() >= 0;
    }

    @Override
    public Wallet createWallet(Long userId) {
        Wallet userWallet = walletUserMap.get(userId);
        if (userWallet == null) {
            userWallet = Wallet.builder().setUserId(userId).build();
            walletUserMap.put(userId, userWallet);
        }
        return userWallet;
    }

    @Override
    public Map<Currency, Double> getUserBalance(Long userId) {
        return findWallet(userId)
                .map(Wallet::getBalance)
                .orElse(Collections.emptyMap());
    }
}
