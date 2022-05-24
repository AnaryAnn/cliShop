package application.service.api;


import exceptions.WalletException;
import model.Amount;
import model.Currency;
import model.Wallet;

import java.util.Map;

/**
 * Интерфейс для работы с кошельком
 */
public interface WalletService {

    /**
     * Пополнение кошелька
     *
     * @param userId идентификатор пользователя
     * @param amount сумма и валюта пополнения
     */
    void deposit(Long userId, Amount amount) throws WalletException;

    /**
     * Снятие денежных средств с кошелька
     *
     * @param userId идентификатор пользователя
     * @param amount сумма и валюта пополнения
     */
    void withdraw(Long userId, Amount amount) throws WalletException;

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
    Map<Currency, Double> getUserBalance(Long userId);


}
