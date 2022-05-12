package services.api;


import model.Amount;
import model.Currency;
import model.Wallet;

import java.util.Map;

public interface WalletService {

    /**
     * Пополнение кошелька
     *
     * @param userId идентификатор пользователя
     * @param amount сумма и валюта пополнения
     */
    void deposit(Long userId, Amount amount) throws Exception;

    /**
     * Снятие денежных средств с кошелька
     *
     * @param userId идентификатор пользователя
     * @param amount сумма и валюта пополнения
     */
    void withdraw(Long userId, Amount amount) throws Exception;

    /**
     * Создание кошелька для пользователя
     *
     * @param userId идентификатор пользователя
     * @return кошелек
     */
    Wallet createWallet(Long userId);

    /**
     * Получение баланса кошелька
     *
     * @param userId идентификатор пользователя
     * @return мапа валюта-сумма
     */
    Map<Currency, Double> getBalance(Long userId);


}
